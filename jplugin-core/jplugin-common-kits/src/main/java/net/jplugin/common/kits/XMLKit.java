/*
 * Created on 2004-7-31
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.jplugin.common.kits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author LiuHang 从XmlUtil和以前的XmlUtil整理.
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class XMLKit {

	final static String ENCODING_STR = "encoding";
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();
	static {
		dbf.setIgnoringElementContentWhitespace(true);
	}

	// empty dom 为整个vm唯一的dom,所有一定不能给它增加儿子节点，否则会导致内存益处
	private static Document emptyDom = createDocument("EMPTY");

	public final static Document getSystemEmptyDom() {
		return emptyDom;
	}

	/**
	 * 创建新的Document,以rootElement为根节点的名称
	 * 
	 * @param rootElement
	 * @return
	 */
	public final static Document createDocument(String rootElement) {
		Document doc = newDocument();
		Element el = doc.createElement(rootElement);
		doc.appendChild(el);
		return doc;
	}

	/**
	 * 创建空Dom
	 * 
	 * @return
	 */
	public final static Document createDocument() {
		return newDocument();
	}

	private static Document newDocument() throws XMLRunTimeException {
		return getXMLBuilder().newDocument();
	}

	private final static DocumentBuilder getXMLBuilder()
			throws XMLRunTimeException {
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db;
		} catch (ParserConfigurationException e) {
			throw new XMLRunTimeException(e.getMessage());
		}
	}

	public final static Document parseStreamWithEncode(ClassLoader cl,
			String resource, String encode) throws XMLException {
		InputStream stream = cl.getResourceAsStream(resource);

		try {
			String sdom = StringKit.changeStreamToString(stream, encode);
			return parseString(sdom);
		} catch (IOException e) {
			e.printStackTrace();
			throw new XMLException("Parse Stream With Encode Error! msg="
					+ e.getMessage());
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
			}
		}
	}

	public final static Document parseStreamWithEncode(Class cls,
			String resourceName, String encode) throws XMLException {
		InputStream stream = cls.getResourceAsStream(resourceName);
		try {
			String sdom = StringKit.changeStreamToString(stream, encode);
			return parseString(sdom);
		} catch (IOException e) {
			e.printStackTrace();
			throw new XMLException("Parse Stream With Encode Error! msg="
					+ e.getMessage());
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
			}
		}
	}

	public final static Document parseFile(String filePath) throws XMLException {
		try {
			Document doc = parseXMLFile(filePath);
			if (doc == null)
				throw new XMLRunTimeException("Can't parse file:" + filePath);
			return doc;
		} catch (Exception e) {
			throw new XMLException("解析文件错误 filePath=[" + filePath + "]  "
					+ e.getMessage());
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	private static Document parseXMLFile(String file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			Document document = getXMLBuilder().parse(is);
			return document;
		} catch (Exception e) {
			throw new XMLRunTimeException(file == null ? "file is null"
					: new java.io.File(file).getAbsolutePath());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public final static Document parseString(String xmlString)
			throws XMLException {
		try {
			if (xmlString == null) {
				return null;
			}
			// 如果字符串没有加encoding 则parseString会出异常，所以默认加utf-8的encoding
			final String encoding = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

			if (!xmlString.startsWith("<?")) {
				// 没有XML头字符串
				xmlString = encoding + xmlString;
			} else {
				// 没有encoding属性
				String oneStr = xmlString.substring(xmlString.indexOf("<?"),
						xmlString.indexOf(">") + 1);
				if (oneStr.indexOf("encoding") == -1) {
					xmlString = encoding + xmlString.substring(oneStr.length());
				}
			}

			return innerParseString(xmlString);
		} catch (Exception e) {
			throw new XMLException("解析字符串错误 xmlString=[" + xmlString + "]  "
					+ e.getMessage());
		}
	}

	/**
	 * @param xmlString
	 * @return
	 */
	private static Document innerParseString(String xml) {
		String header = stringXMLHeader(xml);
		String encoding = xmlEncoding(header);
		return parseStringThrowsException(xml, encoding);
	}

	private final static Document parseStringThrowsException(String xml,
			String encoding) throws RuntimeException {
		try {
			ByteArrayInputStream bais = null;
			if (encoding == null) {
				bais = new ByteArrayInputStream(xml.getBytes());
			} else {
				bais = new ByteArrayInputStream(xml.getBytes(encoding));
			}

			return getXMLBuilder().parse(bais);
		} catch (Exception e) {
			throw new XMLRunTimeException(e);
		}
	}

	private static String stringXMLHeader(String xml) {
		int start = xml.indexOf("<?");
		if ((start != -1) && (xml.substring(0, start).trim().length() <= 2)) {
			// 后一个子式之所以取2是为了兼容Unicode的BOM头,虽然parse未必支持BOM,但是在这里仍然写的健壮些
			int end = xml.indexOf("?>", start);
			if (end != -1) {
				return xml.substring(start, end + 2);
				// end + 2 刚好将?>包括进去了
			}
		}
		return null;
	}

	private static String xmlEncoding(String header) {
		if ((header == null) || header.equals("")) {
			return null;
		}

		int op = header.indexOf(ENCODING_STR);
		if (op == -1) {
			return null;
		}
		op = header.indexOf("\"", op);
		if (op == -1) {
			return null;
		}

		String tmp = header.substring(op + "\"".length());
		op = tmp.indexOf("\"");
		if (op == -1) {
			return null;
		}
		tmp = tmp.substring(0, op);
		return tmp.trim();
	}

	/***************************************************************************************
	 * 
	 * 获取和设定节点的值 一共6个方法
	 * 
	 **************************************************************************************/

	/**
	 * 设置节点值,节点必须为Attribute Node 或者 Element Node
	 * 
	 * @param node
	 * @param value
	 * @throws XMLException
	 * @throws XMLException
	 */
	public static void setNodeValue(Node nd, String value)
			throws XMLRunTimeException {

		if (nd.getNodeType() == Node.ATTRIBUTE_NODE)
			nd.setNodeValue(value);
		else if (nd.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nl = nd.getChildNodes();

			for (int i = 0; i < nl.getLength(); i++) {
				nd.removeChild(nl.item(i));
			}

			nd.appendChild(nd.getOwnerDocument().createTextNode(value));
		} else {
			throw new XMLRunTimeException(
					"设定节点值错误,必须为AttributeNode 或者Element Node");
		}
	}

	/**
	 * 获取节点的值. 实现:如果为Element Node获取第一个文本节点的值.如果找不到,返回 ""
	 * 
	 * @param nd
	 * @return
	 * @throws XMLException
	 */
	public final static String getNodeValue(Node nd) throws XMLRunTimeException {
		if (nd.getNodeType() == Node.ATTRIBUTE_NODE)
			return nd.getNodeValue();
		else if (nd.getNodeType() == Node.ELEMENT_NODE) {
			Node child = nd.getFirstChild();
			if (child == null)
				return "";
			if (child.getNodeType() == Node.TEXT_NODE)
				return child.getNodeValue();
			else {
				NodeList nl = nd.getChildNodes();
				for (int i = 0; i < nl.getLength(); i++) {
					if (nl.item(i).getNodeType() == Node.TEXT_NODE)
						return nl.item(i).getNodeValue();
				}
				return "";
			}
		} else
			throw new XMLRunTimeException(
					"获取节点值错误,必须为Attribute Node 或者Element Node. but nodetype = "
							+ nd.getNodeType());
	}

	/**
	 * 设定儿子节点的值
	 * 
	 * @param doc
	 * @param node
	 *            父亲节点
	 * @param childName
	 *            儿子节点的名称
	 * @param value
	 *            儿子节点的值
	 * @throws XMLException
	 */
	public static void setChildNodeValue(Element node, String childName,
			String value) {
		NodeList nl = node.getChildNodes();

		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(childName)) {
					setNodeValue((Element) nl.item(i), value);
					return;
				}
			}
		}
		appendChild(node, childName, value);
	}

	/***************************************************************************************
	 * 
	 * 创建和删除节点 一共6个方法
	 * 
	 **************************************************************************************/

	/**
	 *删除全部的儿子
	 * 
	 * @param node
	 */
	public static void removeAllChild(Node node) {
		if (node != null) {
			NodeList nl = node.getChildNodes();
			for (int i = nl.getLength(); i > 0; i--) {
				node.removeChild(nl.item(i - 1));
			}
		}
	}

	/**
	 * 创建一个儿子节点,并且设定值
	 * 
	 * @param xdoc
	 * @param ndT
	 *            父亲节点
	 * @param childName
	 *            儿子节点名称
	 * @param childValue
	 *            儿子节点值
	 * @return
	 * @throws XMLException
	 */

	public static Element appendChild(Element elem, String childName,
			String childValue) {
		Element nd = (Element) elem.appendChild(elem.getOwnerDocument()
				.createElement(childName));
		if (childValue != null)
			setNodeValue(nd, childValue);
		return (Element) nd;
	}

	/**
	 * create a child element Node(Do not set it's value)
	 */
	public static Element appendChild(Node ndT, String childName) {
		Node nd = ndT.getOwnerDocument().createElement(childName);
		ndT.appendChild(nd);
		return (Element) nd;
	}

	/***************************************************************************************
	 * 
	 * 简单查询节点 一共8个方法
	 * 
	 **************************************************************************************/

	public static Element getFirstChildElement(Element nd) {
		NodeList nl = nd.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				return (Element) nl.item(i);
		}
		return null;
	}

	/**
	 * This method is used to find first child node by the name
	 */
	public static Element getElement(Element parentNode, String nodeName) {
		if (parentNode == null)
			return null;
		NodeList nl = parentNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(nodeName))
					return (Element) nl.item(i);
			}
		}
		return null;
	}

	/**
	 * This method is used to find last child node by the name
	 */
	public static Element getLastElement(Element parentNode, String nodeName) {
		if (parentNode == null)
			return null;
		NodeList nl = parentNode.getChildNodes();
		for (int i = nl.getLength() - 1; i >= 0; i--) {

			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(nodeName))
					return (Element) nl.item(i);
			}
		}
		return null;
	}

	public static List getAllChildElements(Element parentNode) {
		NodeList nl = parentNode.getChildNodes();
		List retList = new ArrayList();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				retList.add(nl.item(i));
			}
		}
		return retList;
	}

	/**
	 * This method is used to find named child node vector
	 */
	public static List getChildElements(Element parentNode, String nodeName) {
		if (parentNode == null)
			return null;
		List retList = new ArrayList();
		NodeList nl = parentNode.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (nl.item(i).getNodeName().equals(nodeName))
					retList.add(nl.item(i));
			}
		}
		return retList;
	}

	/**
	 * This method is used to find first child node it's attribute named
	 * AttrName has value AttrValue
	 */
	public static Element getElementByAttribute(Element parentNode,
			String nodeName, String AttrName, String AttrValue) {
		if (parentNode == null)
			return null;
		List ve = getChildElements(parentNode, nodeName);
		for (int i = 0; i < ve.size(); i++)
			if (((Element) ve.get(i)).getAttribute(AttrName).equals(AttrValue)) {
				return (Element) ve.get(i);
			}

		return null;
	}

	/**
	 * 通过儿子节点的值来选择儿子节点
	 * 
	 * @param parentNode
	 * @param nodeName
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 * @throws XMLException
	 */
	public static Element getElementByFieldValue(Element parentNode,
			String nodeName, String fieldName, String fieldValue)
			throws XMLException {
		if (parentNode == null)
			return null;
		List ve = getChildElements(parentNode, nodeName);
		for (int i = 0; i < ve.size(); i++) {
			if (cNull(getNodeValue((Element) ve.get(i), fieldName)).equals(
					fieldValue)) {
				return (Element) ve.get(i);
			}
		}
		return null;
	}

	/**
	 * @param element
	 * @param fieldName
	 * @return
	 */
	private static String getNodeValue(Element element, String fieldName) {
		Element child = getElement(element, fieldName);
		if (child == null)
			return null;
		else {
			return getNodeValue(element);
		}
	}

	/**
	 * 遍历整个节点的全部子节点，使用XO接口进行处理
	 * 
	 * @param nd
	 * @param xo
	 */
	public static void travelNode(Node nd, XMLNodeOperation xo) {
		if (nd == null) {
			return;
		}
		xo.disposeNode(nd);
		if (nd.getNodeType() == Node.ELEMENT_NODE) {
			NamedNodeMap nnm = nd.getAttributes();
			int nnmlen = nnm.getLength();
			for (int i = 0; i < nnmlen; i++) {
				Node nnmi = nnm.item(i);
				travelNode(nnmi, xo);
			}
			NodeList nl = nd.getChildNodes();
			int nll = nl.getLength();
			for (int i = 0; i < nll; i++) {
				Node nli = nl.item(i);
				travelNode(nli, xo);
			}
		} else if (nd.getNodeType() == Node.DOCUMENT_NODE) {
			travelNode(nd.getChildNodes().item(0), xo);
		}
	}

	private static String cNull(String s) {
		if (s == null)
			return "";
		else
			return s;
	}

	/**
	 * @param node
	 * @return
	 */
	public static Node importOrCloneNode(Document dom, Node node) {
		if (dom == node.getOwnerDocument())
			return node.cloneNode(true);
		else
			return dom.importNode(node, true);
	}

	/**
	 * @param ownerDocument
	 * @param node
	 * @return
	 */
	public static Node condImportNode(Document ownerDocument, Node node) {
		if (ownerDocument == node.getOwnerDocument())
			return node;
		else
			return ownerDocument.importNode(node, true);
	}

	/**
	 * 把Element 下面的所有text节点解析为 xpath->value的HashMap 1.Element可以根据name属性进行区分
	 * 2.如果某text节点有兄弟节电，则忽略该节点
	 * 
	 * @param rootNode
	 * @return
	 */
	public static HashMap getTextPathValues(final Element rootNode) {
		final HashMap hm = new HashMap();
		travelNode(rootNode, new XMLNodeOperation() {
			public String getNodePath(Element root, Node nd) {
				String path = "";
				Node pathElem = nd;

				// 以下循环，每一步把pathElem的父亲节点对应的pathStep加入到path前面
				do {
					pathElem = pathElem.getParentNode();

					String pathStep = "/";
					pathStep = pathStep + ((Element) pathElem).getTagName();
					if (((Element) pathElem).hasAttribute("name")) {
						pathStep = pathStep + "[@name='"
								+ ((Element) pathElem).getAttribute("name")
								+ "']";
					}
					path = pathStep + path;
				} while (pathElem != root);
				return path;
			}

			public void disposeNode(Node nd) {
				if (nd.getNodeType() == nd.TEXT_NODE) {
					if (nd.getParentNode().getChildNodes().getLength() == 1) {
						hm.put(getNodePath(rootNode, nd), nd.getNodeValue());
					}
				}
			}
		});
		return hm;
	}

	public static String node2String(Node nd) {
		String xmlStr = "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("encoding", "GBK");// 解决中文问题，xml转换过程保证中文不乱码，与文件存储格式无关
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(nd), new StreamResult(bos));
			xmlStr = bos.toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return xmlStr;
	}	

	public static void main(String[] args) {
		Document dom = createDocument("root");
		// Element docElem = dom.getDocumentElement();
		System.out.println(dom);
		//		
	}
}
