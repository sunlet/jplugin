package net.jplugin.core.das.hib.impl;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.XMLKit;
import net.jplugin.core.das.api.DataException;
import net.jplugin.core.das.hib.api.Entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-24 上午11:52:19
 **/

public class EntityXMLBuilderHelper {

	/**
	 * @param c
	 * @return
	 */
	public static Document getHbmDom(Class c) {
		Entity entity = (Entity) c.getAnnotation(Entity.class);
		return getDocument(c,entity);
	}

	/**
	 * @param c
	 * @param entity
	 * @return
	 */
	private static Document getDocument(Class c, Entity entity) {
		 Map<String, Class<?>> propertyMapping = ReflactKit.getPropertiesAndType(c);
		 Document dom = XMLKit.createDocument("hibernate-mapping");
		 dom.getDocumentElement().setAttribute("package", c.getPackage().getName());
		 dom.getDocumentElement().appendChild(buildClassElement(dom,propertyMapping,ReflactKit.getShortName(c),entity));
//		 System.out.println(XMLKit.node2String(dom));
		 return dom;
	}

	/**
	 * @param dom
	 * @param propertyMapping
	 * @param clzShortName 
	 * @param entity
	 * @return
	 */
	private static Node buildClassElement(Document dom, Map<String, Class<?>> propertyMapping, String clzShortName, Entity entity) {
		//metadata
		String tablename;
		String idfield;
		String generator;
		if (entity!=null){
			tablename = StringKit.isNull(entity.tableName())? clzShortName.toLowerCase() : entity.tableName();
			idfield = entity.idField();
			generator = entity.idgen();
		}else{
			tablename = clzShortName.toLowerCase();
			idfield = "id";
			generator = "native";
		}
		//check idfield exists
		if (!propertyMapping.containsKey(idfield)){
			throw new DataException("id field not found in bean:"+clzShortName);
		}
		//make the text fields map
		HashSet<String> textFieldsMap = new HashSet<String>();
		if (entity!=null){
			String[] arr = StringKit.splitStr(entity.textFields()," ");
			if (arr!=null){
				for (String stemp:arr){
					textFieldsMap.add(stemp);
				}
			}
		}
		
		
		//class elem
		Element clsElem = dom.createElement("class");
		clsElem.setAttribute("name",clzShortName );
		clsElem.setAttribute("dynamic-insert", "true");
		clsElem.setAttribute("dynamic-update", "true");
		clsElem.setAttribute("table",tablename );
		
		//add id element
		Element idElem = dom.createElement("id");
		idElem.setAttribute("name", idfield);
		idElem.setAttribute("column", idfield.toLowerCase());
		idElem.setAttribute("type", propertyMapping.get(idfield).getName());
		Element generatorElem = dom.createElement("generator");
		generatorElem.setAttribute("class", generator);
		idElem.appendChild(generatorElem);
		clsElem.appendChild(idElem);
		
		//add properties
		for (Map.Entry<String, Class<?>> m:propertyMapping.entrySet()){
			if (idfield.equals(m.getKey())){
				continue;
			}
			
			Element propElem = dom.createElement("property");
			propElem.setAttribute("name", m.getKey());
			propElem.setAttribute("column", m.getKey().toLowerCase());
			String type =null;
			if (textFieldsMap.contains(m.getKey())){
				type = "text";
			}else{
				type = getColumnType(m.getValue(),m.getKey());
			}

			propElem.setAttribute("type", type);
			clsElem.appendChild(propElem);
		}
		
		//移动租户字段到最后
		Element tenantElem = XMLKit.getElementByAttribute(clsElem, "property", "name", "tenantId");
		if (tenantElem!=null){
			clsElem.removeChild(tenantElem);
			clsElem.appendChild(tenantElem);
		}
		
		return clsElem;
	}

	static final Map<Class,String> typeMapping = new HashMap<Class, String>();
	static{
		typeMapping.put(java.lang.String.class, "string");
		typeMapping.put(java.lang.Integer.class, "int");
		typeMapping.put(int.class, "int");
		
		typeMapping.put(java.lang.Long.class, "long");
		typeMapping.put(long.class, "long");
		
		typeMapping.put(Double.class, "double");
		typeMapping.put(double.class, "double");
		
		typeMapping.put(float.class, "float");
		typeMapping.put(Float.class, "float");
		
		typeMapping.put(Boolean.class, "boolean");
		typeMapping.put(boolean.class, "boolean");
		
	}
	/**
	 * @param value
	 * @param key
	 * @return
	 */
	private static String getColumnType(Class<?> value, String key) {
		String ret = typeMapping.get(value);
		if (ret!=null){
			return ret;
		}
		
		//日期类型特殊处理
		if (value.equals(Date.class)) {
			if (key.toLowerCase().endsWith("date")) {
				return "date";
			} else {
				return "timestamp";
			}
		}
		throw new DataException("Not supported field type:"+value.getName());
	}

//	<hibernate-mapping package="com.actiz.bcs.authority.application.domain">
//	<!--class name="AuthorityRole" table="bcs_authorization_roles" dynamic-insert="true" dynamic-update="true"-->
//	<class name="AuthorityRole" table="bc_auth_roles" dynamic-insert="true" dynamic-update="true">
//		<id name="id" column="id" type="java.lang.Long">
//			<generator class="assigned"/>
//		</id>
//        <property name="isPublic" column="ispublic" type="java.lang.Integer"/>
//        <property name="isEvolve" column="isevolve" type="java.lang.Integer"/>
//		<property name="moduleId" column="module_id" type="java.lang.Long"/>
//		
//	 <property name="updatetime" type="timestamp"  node="boss服务器路径" >
//     <column name="updatetime"  />
// </property>
}
