package net.jplugin.core.kernel.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.impl.PropertyUtil;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 上午11:43:22
 **/

public class Extension {
	public static IPropertyFilter propertyFilter=null;
	String refExtensionPoint;
	String name;
	Class clazz;
	Vector<Property> propertyList=new Vector<Property>(1);
	
	Object extensionObject;
	
	public String getExtensionPointName(){
		return this.refExtensionPoint;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Class getClazz(){
		return this.clazz;
	}
	
	public List<Property> getProperties(){
		return this.propertyList;
	}
	
	public Object getObject(){
		return extensionObject;
	}
	
	public static class Property{
		String key;
		String value;
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String v){
			value = v;
		}
		
	}
	
	public synchronized void load() throws Exception{
		if (propertyFilter!=null){
			filterProperty(this.propertyList);
		}
		if (this.extensionObject == null){
			if (clazz.equals(String.class)){
				//字符串类型采用特殊加载方式
				if (this.propertyList.size()!=1){
					throw new RuntimeException("String type extension must has one property with the val");
				}
				this.extensionObject = this.propertyList.get(0).getValue();
			}else{
				this.extensionObject = clazz.newInstance();
	
				//带属性的加载方式
				if (this.propertyList.size()>0){
					setProperty(this.extensionObject,this.propertyList);
				}
			}
		}
	}
	
	private void filterProperty(Vector<Property> list) {
		for (Property p:list){
			p.setValue(propertyFilter.filte(p.getValue()));
		}
	}

	/**
	 * @param extensionObject2
	 * @param propertyList2
	 */
	private static void setProperty(Object o,
			Vector<Property> p) {
		//看能否找到method
		Method method = null;
		try {
			method = o.getClass().getMethod("setExtensionProperty", new Class[]{java.util.List.class});
		} catch (Exception e){
		}
		
		if (method != null){
			ReflactKit.invoke(o,"setExtensionProperty",new Object[]{p});
		}else{
			PropertyUtil.setProperties(o,p);
		}
	}

	public static Extension createStringExtension(String aPointName,String value){
		return create(aPointName,"",String.class,new String[][]{{"StringValue",value}});
	}
	
	public static Extension create(String aPointName,Class cls){
		return create(aPointName,"",cls);
	}
	
	public static Extension create(String aPointName,String aName,Class cls){
		return create(aPointName,aName,cls,null);
	}

	public static Extension create(String aPointName,Class cls,String[][] property){
		return create(aPointName,"",cls,property);
	}
	public static Extension create(String aPointName,String aName,Class cls,String[][] property){
		Extension ext = new Extension();
		ext.name = aName;
		ext.clazz = cls;
		ext.refExtensionPoint = aPointName;
		if (property!=null){
			for (int i=0;i<property.length;i++){
				Property p = new Property();
				p.key = property[i][0];
				p.value = property[i][1];
				ext.propertyList.add(p);
			}
		}
		return ext;
	}
}
