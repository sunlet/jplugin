package net.jplugin.common.kits;

/*
 * 修改历史 版本 修订人 修订时间 修订原因
 */

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringKit {

	final static String[] SBC = { "，", "。", "；", "“", "”", "？", "！", "（", "）",
			"：", "——", "、" };

	final static String[] DBC = { ",", ".", ";", "\"", "\"", "?", "!", "(",
			")", ":", "_", "," };

	public static String replaceBatch(String str,String ... arg){
		if (arg==null) return str;
		for (int i=0;i<arg.length;i+=2){
			str = StringKit.replaceStr(str, arg[i], arg[i+1]);
		}
		return str;
	}
	
	/**
	 * 去除字符串两端空格 如果字符串是空的返加null
	 * 
	 * @Param String
	 * @return String
	 */
	public static String trim(String str) {
		if (str == null)
			return null;

		str = str.trim();
		if (str.length() == 0)
			return null;
		return str;
	}

	public static String replaceStr(String str, String oldStr, String newStr) {
		int pos1 = 0;
		int pos2 = 0;
		StringBuffer retu = new StringBuffer();
		if (str != null && str.length() > 0) {
			for (pos2 = str.indexOf(oldStr, pos1); pos2 != -1; pos2 = str
					.indexOf(oldStr, pos1)) {
				retu.append(str.substring(pos1, pos2) + newStr);
				pos1 = pos2 + oldStr.length();
			}

			retu.append(str.substring(pos1));
		}
		return retu.toString();
	}

	/**
	 * 设置字符串首字母为大写
	 */
	public static String cap(String str) {
		if (str == null)
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(Character.toUpperCase(str.charAt(0)));
		sb.append(str.substring(1).toLowerCase());
		return sb.toString();
	}

	/**
	 * 判断字符串是否是包含a-z, A-Z, 0-9, _(下划线)
	 */
	public static boolean isWord(String str) {
		if (str == null)
			return false;

		char[] ch = str.toCharArray();
		int i;
		for (i = 0; i < str.length(); i++) {
			if ((!Character.isLetterOrDigit(ch[i])) && (ch[i] != '_'))
				return false;
		}
		return true;
	}

	//允许是负数
	public static boolean isNumAllowNig(String str){
		if (str.startsWith("-")){
			return isNum(str.substring(1));
		}else{
			return isNum(str);
		}
	}
	
	/**
	 * 判断字符串是否数字
	 */
	public static boolean isNum(String str) {
		if (str == null || str.length() <= 0)
			return false;

		char[] ch = str.toCharArray();

		for (int i = 0; i < str.length(); i++)
			if (!Character.isDigit(ch[i]))
				return false;

		return true;
	}

	/**
	 * 判断字符串是否为实数
	 */
	public static boolean isNumEx(String str) {
		if (str == null || str.length() <= 0)
			return false;

		char[] ch = str.toCharArray();

		// 判断第一个字符是否－号
		int index = 0;
		if (ch[0] == '-')
			index = 1;

		for (int i = index, comcount = 0; i < str.length(); i++) {
			if (!Character.isDigit(ch[i])) {
				if (ch[i] != '.')
					return false;

				else if (i == 0 || i == str.length() - 1)
					return false; // .12122 or 423423. is not a real number

				else if (++comcount > 1)
					return false; // 12.322.23 is not a real number
			}
		}
		return true;
	}

	/**
	 * 替换字符串，sOld sNew的大小必须相同
	 */
	public static String replaceStrEq(String sReplace, String sOld, String sNew) {
		if (sReplace == null || sOld == null || sNew == null)
			return null;

		int iLen = sReplace.length();
		int iLenOldStr = sOld.length();
		int iLenNewStr = sNew.length();

		if (iLen <= 0 || iLenOldStr <= 0 || iLenNewStr <= 0)
			return sReplace;

		if (iLenOldStr != iLenNewStr)
			return sReplace;

		int[] iIndex = new int[iLen];
		iIndex[0] = sReplace.indexOf(sOld, 0);
		if (iIndex[0] == -1)
			return sReplace;

		int iIndexNum = 1;
		while (true) {
			iIndex[iIndexNum] = sReplace.indexOf(sOld,
					iIndex[iIndexNum - 1] + 1);
			if (iIndex[iIndexNum] == -1)
				break;
			iIndexNum++;
		}

		char[] caReplace = sReplace.toCharArray();
		char[] caNewStr = sNew.toCharArray();

		for (int i = 0; i < iIndexNum; i++) {
			for (int j = 0; j < iLenOldStr; j++) {
				caReplace[j + iIndex[i]] = caNewStr[j];
			}
		}
		return new String(caReplace);
	}

	/**
	 * 替换字符串
	 */
	@SuppressWarnings("unchecked")
	public static String replaceStrEx(String sReplace, String sOld, String sNew) {
		if (sReplace == null || sOld == null || sNew == null)
			return null;

		int iLen = sReplace.length();
		int iLenOldStr = sOld.length();
		int iLenNewStr = sNew.length();

		if (iLen <= 0 || iLenOldStr <= 0 || iLenNewStr < 0)
			return sReplace;

		int[] iIndex = new int[iLen];
		iIndex[0] = sReplace.indexOf(sOld, 0);
		if (iIndex[0] == -1)
			return sReplace;

		int iIndexNum = 1;
		while (true) {
			iIndex[iIndexNum] = sReplace.indexOf(sOld,
					iIndex[iIndexNum - 1] + 1);
			if (iIndex[iIndexNum] == -1)
				break;
			iIndexNum++;
		}

		ArrayList vStore = new ArrayList();
		String sub = sReplace.substring(0, iIndex[0]);
		if (sub != null)
			vStore.add(sub);

		int i = 1;
		for (i = 1; i < iIndexNum; i++) {
			vStore.add(sReplace
					.substring(iIndex[i - 1] + iLenOldStr, iIndex[i]));
		}
		vStore.add(sReplace.substring(iIndex[i - 1] + iLenOldStr, iLen));

		StringBuffer sbReplaced = new StringBuffer("");
		for (i = 0; i < iIndexNum; i++) {
			sbReplaced.append(vStore.get(i) + sNew);
		}
		sbReplaced.append(vStore.get(i));

		return sbReplaced.toString();
	}

	/**
	 * 分隔字符串
	 */
	public static String[] splitStr(String sStr, String sSplitter) {
		if (sStr == null || sStr.length() <= 0 || sSplitter == null
				|| sSplitter.length() <= 0)
			return null;

		String[] saRet = null;

		int[] iIndex = new int[sStr.length()];
		iIndex[0] = sStr.indexOf(sSplitter, 0);
		if (iIndex[0] == -1) {
			saRet = new String[1];
			saRet[0] = sStr;
			return saRet;
		}

		int iIndexNum = 1;
		while (true) {
			iIndex[iIndexNum] = sStr.indexOf(sSplitter,
					iIndex[iIndexNum - 1] + 1);
			if (iIndex[iIndexNum] == -1)
				break;
			iIndexNum++;
		}

		Vector vStore = new Vector();
		int iLen = sSplitter.length();
		int i = 0;
		String sub = null;

		for (i = 0; i < iIndexNum + 1; i++) {
			if (i == 0)
				sub = sStr.substring(0, iIndex[0]);
			else if (i == iIndexNum)
				sub = sStr.substring(iIndex[i - 1] + iLen, sStr.length());
			else
				sub = sStr.substring(iIndex[i - 1] + iLen, iIndex[i]);

			if (sub != null && sub.length() > 0)
				vStore.add(sub);
		}

		if (vStore.size() <= 0)
			return null;
		saRet = new String[vStore.size()];
		Enumeration e = vStore.elements();

		for (i = 0; e.hasMoreElements(); i++)
			saRet[i] = (String) e.nextElement();
		return saRet;
	}

	/**
	 * 以sContacter为分隔符连接字符串数组saStr
	 */
	public static String contactStr(String[] saStr, String sContacter) {
		if (saStr == null || saStr.length <= 0 || sContacter == null
				|| sContacter.length() <= 0)
			return null;

		StringBuffer sRet = new StringBuffer("");
		for (int i = 0; i < saStr.length; i++) {
			if (i == saStr.length - 1)
				sRet.append(saStr[i]);
			else
				sRet.append(saStr[i] + sContacter);
		}
		return sRet.toString();
	}

	/**
	 * 转换整型数组为字符串
	 */
	public static String contactStr(int[] saStr, String sContacter) {
		if (saStr == null || saStr.length <= 0 || sContacter == null
				|| sContacter.length() <= 0)
			return null;

		StringBuffer sRet = new StringBuffer("");
		for (int i = 0; i < saStr.length; i++) {
			if (i == saStr.length - 1)
				sRet.append(new Integer(saStr[i]));
			else
				sRet.append(new Integer(saStr[i]) + sContacter);
		}
		return sRet.toString();
	}

	/**
	 * 排序字符串数组
	 */
	public static String[] sortByLength(String[] saSource, boolean bAsc) {
		if (saSource == null || saSource.length <= 0)
			return null;

		int iLength = saSource.length;
		String[] saDest = new String[iLength];

		for (int i = 0; i < iLength; i++)
			saDest[i] = saSource[i];

		String sTemp = "";
		int j = 0, k = 0;

		for (j = 0; j < iLength; j++)
			for (k = 0; k < iLength - j - 1; k++) {
				if (saDest[k].length() > saDest[k + 1].length() && bAsc) {
					sTemp = saDest[k];
					saDest[k] = saDest[k + 1];
					saDest[k + 1] = sTemp;
				} else if (saDest[k].length() < saDest[k + 1].length() && !bAsc) {
					sTemp = saDest[k];
					saDest[k] = saDest[k + 1];
					saDest[k + 1] = sTemp;
				}
			}
		return saDest;
	}

	public static String compactStr(String str) {
		if (str == null)
			return null;

		if (str.length() <= 0)
			return "";

		String sDes = new String(str);
		int iBlanksAtStart = 0;
		int iLen = str.length();

		while (sDes.charAt(iBlanksAtStart) == ' ')
			if (++iBlanksAtStart >= iLen)
				break;

		String[] saDes = splitStr(sDes.trim(), " ");
		if (saDes == null)
			return null;

		int i = 0;
		for (i = 0; i < saDes.length; i++) {
			saDes[i] = saDes[i].trim();
		}

		sDes = contactStr(saDes, " ");
		StringBuffer sBlank = new StringBuffer("");
		for (i = 0; i < iBlanksAtStart; i++)
			sBlank.append(" ");

		return sBlank.toString() + sDes;
	}

	/**
	 * 转换sbctodbc
	 */
	public static String symbolSBCToDBC(String sSource) {
		if (sSource == null || sSource.length() <= 0)
			return sSource;

		int iLen = SBC.length < DBC.length ? SBC.length : DBC.length;
		for (int i = 0; i < iLen; i++)
			sSource = replaceStrEx(sSource, SBC[i], DBC[i]);
		return sSource;
	}

	/**
	 * 转换dbctosbc
	 */
	public static String symbolDBCToSBC(String sSource) {
		if (sSource == null || sSource.length() <= 0)
			return sSource;

		int iLen = SBC.length < DBC.length ? SBC.length : DBC.length;
		for (int i = 0; i < iLen; i++)
			sSource = replaceStrEx(sSource, DBC[i], SBC[i]);
		return sSource;
	}

	/**
	 * 判断是否email地址
	 */
	public static boolean isEmailUrl(String str) {
		if ((str == null) || (str.length() == 0))
			return false;

		if ((str.indexOf('@') > 0)
				&& (str.indexOf('@') == str.lastIndexOf('@'))) {
			if ((str.indexOf('.') > 0)
					&& (str.lastIndexOf('.') > str.indexOf('@'))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否email地址
	 */
	public static boolean isEmailAddress(String str) {
		if (str == null || str.length() <= 0)
			return false;

		int iCommonCount = 0;
		int iAltCount = 0;
		char[] chEmail = str.trim().toCharArray();

		for (int i = 0; i < chEmail.length; i++) {
			if (chEmail[i] == ' ')
				return false;

			else if (chEmail[i] == '.') {
				if (i == 0 || (i == chEmail.length - 1))
					return false;
			}

			else if (chEmail[i] == '@') {
				if (++iAltCount > 1 || i == 0 || i == chEmail.length - 1)
					return false;
			}
		}

		if (str.indexOf('.') < str.indexOf('@'))
			return false;
		return true;
	}

	/**
	 * 格式化日期
	 * 
	 * @param java
	 *            .util.Date date
	 * @param String
	 *            newFormat
	 * @return String example formatDate(date, "MMMM dd, yyyy") = July 20, 2000
	 */
	public static String formatDate(Date date, String newFormat) {
		if (date == null || newFormat == null)
			return null;

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				newFormat);
		return formatter.format(date);
	}

	/**
	 * 将日期字符串转换成java.util.Date类型对象
	 * 
	 * @param dateString
	 * @param format
	 *            默认使用yyyy-MM-dd格式
	 * */
	public final static Date string2Date(String dateString, String format) {

		if (null == dateString || dateString.equals(""))
			return null;

		if (null == format || format.equals(""))
			format = "yyyy-MM-dd";

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		try {
			date = formatter.parse(dateString);
		} catch (Exception ex) {
			// ex.printStackTrace() ;
			date = null;
		}

		return date;
	}

	/**
	 * 将Null的String转换为空字符串
	 */
	public static String cEmpty(String s) {
		if (s == null)
			return "";
		return s;
	}

	/**
	 * 将空字符串转换为Null
	 */
	public static String cNull(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s;
	}

	/**
	 * 如果s为空或Null, 则返回"NUll", 否则给s两边加上单引号返回。用在写数据库的时候。
	 */
	public static String nullString(String s) {
		if (s == null)
			return "Null";
		if (s.trim().length() == 0)
			return "Null";
		return "'" + s.trim() + "'";
	}

	public static String filterString(String s, String t) {
		String a = s;
		int i, j;
		j = t.length();
		while ((i = a.indexOf(t)) != -1) {
			a = a.substring(0, i - 1) + a.substring(i + j);
		}
		return a;
	}

	/**
	 * The method is used to get the bytes number of string
	 */
	public static int getTotalBytes(String srcString) {
		if (srcString == null)
			return 0;

		int result = 0;
		byte[] bytes = srcString.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != 0)
				result++;
			;
		}
		return result;
	}

	/**
	 * check the given string to see if it's null or empty string
	 */
	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * 检查是否匹配,可以带*
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isStrSuite(String ssub, String sparent) {
		ssub = ssub.trim();
		sparent = sparent.trim();

		String[] subarr = StringKit.splitStr(ssub, " ");
		String[] pararr = StringKit.splitStr(sparent, " ");

		return isStringArraySuite(subarr, pararr);
	}

	/**
	 * 检查是否匹配，可以带*
	 * 
	 * @param subarr
	 * @param pararr
	 * @return
	 */
	public static boolean isStringArraySuite(String[] subarr, String[] pararr) {

		if (subarr.length > pararr.length)
			return false;

		for (int i = 0; i < subarr.length; i++) {
			if (subarr[i].equals("*"))
				continue;
			if (pararr[i].equals("*"))
				continue;
			if (!subarr[i].equals(pararr[i]))
				return false;
		}
		return true;
	}

	public static String arrToString(Object arr) {
		String ret = "\nlen=" + Array.getLength(arr);
		for (int i = 0; i < Array.getLength(arr); i++) {
			ret = ret + "\nitem[" + i + "]=" + Array.get(arr, i);
		}
		return ret;
	}

	public static String mapToString(Map map) {
		String ret = "\nlen=" + map.keySet().size();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			ret = ret + "\nkey[" + key + "]" + "  value="
					+ map.get(map.get(key));
		}

		return ret;
	}

	/**
	 * 判断是否为空指针或者空字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNullAndBlank(String str) {
		if (isNullOrBlank(str))
			return false;
		else
			return true;
	}

	/**
	 * 判断是否为空指针或者空字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (isNull(str) || str.equals("") || str.equals("null"))
			return true;
		else
			return false;
	}

	/**
	 * 判断是否为空指针
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为空指针
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		else
			return true;
	}

	/**
	 * 将空指针转成空字符
	 * 
	 * @param str
	 * @return
	 */
	public static String ifNullToBlank(String str) {
		if (isNotNull(str) && !(str.trim().equals("null")))
			return str.trim();
		else
			return "";
	}

	public static String ifNullToBlank(Object obj) {
		String ret = "";
		String s = String.valueOf(obj);
		if (s == null || "".equals(s) || "null".equals(s) || "NULL".equals(s))
			ret = "";
		else
			ret = s;
		return ret;
	}

	public static final String escapeXML(String string) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (string == null || string.length() == 0) {
			return string;
		}
		char[] sArray = string.toCharArray();
		StringBuffer buf = new StringBuffer(sArray.length);
		char ch;
		for (int i = 0; i < sArray.length; i++) {
			ch = sArray[i];
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '"') {
				buf.append("&quot;");
			} else {
				buf.append(ch);
			}
		}

		return buf.toString();
	}

	public static String[] splitStrWithBlank(String str, String delim) {
		if (str == null) {
			return null;
		}
		if (str.trim().equals("")) {
			return null;
		}
		List list = new ArrayList();
		int index = 0;
		while ((index = str.indexOf(delim)) >= 0) {
			list.add(str.substring(0, index));
			str = str.substring(index + 1, str.length());
		}
		list.add(str);
		String[] strs = new String[list.size()];
		list.toArray(strs);
		return strs;

	}

	public static char getCharAtPosDefaultZero(String s, int pos) {
		if (s == null)
			return '0';

		if (s.length() <= pos)
			return '0';
		return s.charAt(pos);
	}

	/**
	 * 设置字符串的制定位置（0表示第一个字符）字符。 如果字符串长度小于 位置+1 ，则首先给字符串 补充0 允许传入参数为null
	 * 
	 * @param extend2
	 * @param pos_is_allow_sendback
	 * @param flag
	 * @return
	 */
	public static String setCharAtPosAppendZero(String s, int pos, char c) {
		if (s == null)
			s = "";

		while (pos > s.length() - 1) {
			s = s + '0';
		}

		String preString, afterString;

		if (pos == 0)
			preString = "";
		else
			preString = s.substring(0, pos);

		if (pos == s.length() - 1)
			afterString = "";
		else
			afterString = s.substring(pos + 1);

		return preString + c + afterString;

	}


	/**
	 * @param stream
	 * @param encode
	 * @throws IOException
	 */
	public static String changeStreamToString(InputStream stream, String encode)
			throws IOException {

		byte[] b100k = new byte[200000];

		int pos = 0;
		while (true) {
			int len = stream.read(b100k, pos, b100k.length - pos);
			if (len <= 0)
				break;
			else
				pos = pos + len;
		}

		if (pos >= b100k.length - 1) {
			throw new IOException("ERROR:The stream size is more than "
					+ b100k.length + " bytes");
		}

		return new String(b100k, 0, pos, encode);
	}

	public static String fillBlank(String s, int n, boolean isLeft) {
		if (s.length() >= n)
			return s;
		for (int i = s.length(); i < n; i++) {
			if (isLeft) {
				s = " " + s;
			} else {
				s = s + " ";
			}
		}
		return s;
	}

	public static String replaceBlanks(String s) {

		Pattern ptn = Pattern.compile("\\t|\r|\n");
		Matcher mt = ptn.matcher(s);
		return mt.replaceAll("");

	}

	public static String replaceScriptKeyword(String src) {

		Pattern ptn = Pattern.compile("<script>");
		Matcher mt = ptn.matcher(src);
		src = mt.replaceAll("<scriptValue>");

		ptn = Pattern.compile("</script>");
		mt = ptn.matcher(src);

		return mt.replaceAll("</scriptValue>");

	}

	/**
	 * 
	 * @param version1
	 * @param version2
	 * @return 1 0 -1
	 */
	public static int compareVersion(String version1, String version2) {
		StringTokenizer st1 = new StringTokenizer(version1, ".");
		StringTokenizer st2 = new StringTokenizer(version2, ".");

		ArrayList al1 = new ArrayList();
		ArrayList al2 = new ArrayList();

		while (st1.hasMoreTokens()) {
			al1.add(st1.nextToken());
		}
		while (st2.hasMoreTokens()) {
			al2.add(st2.nextToken());
		}

		int size1 = al1.size();
		int size2 = al2.size();

		for (int i = 0; i < size1 && i < size2; i++) {
			int v1 = Integer.parseInt((String) al1.get(i));
			int v2 = Integer.parseInt((String) al2.get(i));

			if (v1 > v2)
				return 1;
			if (v1 < v2)
				return -1;
		}

		if (size1 > size2)
			return 1;
		if (size1 < size2)
			return -1;
		return 0;
	}

	/**
	 * 计算String的native字符串长度
	 * 
	 * @return java.lang.String
	 */
	public static int nativeLength(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= 0x100) {
				length = length + 3;
			} else {
				length++;
			}
		}
		return length;
	}

	public final static int MAX_VARCHAR_PREPARE_LENGTH = 1000;

	/**
	 * 2005-11-7 添加对oracle数据库中长字符串字段的处理， 解决oracle jdbc的setString超过1280字节时的异常。
	 * 
	 * @param aValueList
	 * @param aDataTypes
	 * @return
	 */
	public static String varcharSQLString(List retList, String value) {
		if (value != null && (nativeLength(value) > MAX_VARCHAR_PREPARE_LENGTH)) {

			// modify by wangkq 2007/4/4
			// 原来将字符串进行拆分的算法不正确，采用DataUtil.nativeLength中获取
			// 字符串长度方法来进行字符串分割
			String sql = "?";
			StringBuffer buff = new StringBuffer("");
			int len = 0;
			int sLen = value.length();
			for (int i = 0; i < sLen; i++) {
				char c = value.charAt(i);
				if (c >= 0x100) {
					if ((len + 3) > MAX_VARCHAR_PREPARE_LENGTH) {
						retList.add(buff.toString());
						sql = sql.concat(" || ?");
						buff = new StringBuffer(String.valueOf(c));
						len = 3;
						continue;
					}
					len = len + 3;
				} else {
					len++;
				}
				buff.append(c);
				if (len == MAX_VARCHAR_PREPARE_LENGTH) {
					retList.add(buff.toString());
					if (i < (sLen - 1))
						sql = sql.concat(" || ?");
					buff = new StringBuffer("");
					len = 0;
				}
			}
			if (len > 0) {
				retList.add(buff.toString());
			}

			return sql;
		} else {
			retList.add(value);
			return null;
		}
	}

	/**
	 * @param name
	 * @param name2
	 * @return
	 */
	public static int compare(String s1, String s2) {
		for (int i=0;i<s1.length() && i<s2.length();i++){
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			if (c1>c2) return 1;
			if (c1<c2) return -1;
		}
		//字符比完了，还无结果，比长度
		if (s1.length() >s2.length()) return 1;
		if (s1.length() <s2.length()) return -1;
		return 0;
	}
	
	public static void main(String[] args) {
		System.out.println(compare("ab","ab"));
		System.out.println(compare("",""));
		
		System.out.println(compare("ab","ac"));
		System.out.println(compare("ac","ab"));
		
		System.out.println(compare("ab","abc"));
	}

	public static String null2Empty(String hcond) {
		if (hcond==null) return "";
		return hcond;
	}

	public static String repaceFirst(String s, String toReplace, String replacement) {
		int pos = s.indexOf(toReplace);
		if (pos<0) return s;
		return s.substring(0,pos)+replacement + s.substring(pos + toReplace.length());
	}

	public static String repaceFirstIgnoreCase(String s, String toReplace, String replacement) {
		int pos = s.toUpperCase().indexOf(toReplace.toUpperCase());
		if (pos<0) return s;
		return s.substring(0,pos)+replacement + s.substring(pos + toReplace.length());
	}

}