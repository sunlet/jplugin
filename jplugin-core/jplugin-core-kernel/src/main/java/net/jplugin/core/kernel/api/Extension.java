package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.extfactory.ObjectFactory;
import net.jplugin.core.kernel.api.extfactory.StringExtensionFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 上午11:43:22
 **/

public class Extension {
	public static IPropertyFilter propertyFilter=null;
	static Extension lastAdded=null;
	private String refExtensionPoint;
	private String name;
//	Class clazz; //目前必须有值
	private IExtensionFactory factory;//目前必须有值
	private int priority;
//	Vector<Property> propertyList=new Vector<Property>(1);
	
	Object extensionObject;

	void setRefExtensionPoint(String nm){
		if (StringKit.isNotNull(refExtensionPoint))
			throw new RuntimeException("can't set");
		refExtensionPoint = nm;
	}

	public static void setLastExtensionId(String id){
		if (lastAdded!=null) {
			lastAdded.setId(id);
		}else {
			throw new RuntimeException("Last extension is null.");
		}
	}
	public static void setLastExtensionPriority(int priority){
		if (lastAdded!=null) {
			lastAdded.setPriority(priority);
		}else {
			throw new RuntimeException("Last extension is null.");
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("refPoint:"+refExtensionPoint+" clazz:"+factory.getImplClass().getName()+" name:"+name);
//		sb.append(" property:[");
//		for (int i=0;i<propertyList.size();i++) {
//			sb.append(propertyList.get(i).key+"-"+propertyList.get(i).value);
//			sb.append("  ");
//		}
//		sb.append("]");
		sb.append(" factory:[").append(this.factory).append("]");
		return sb.toString();
	}
	/**
	 * 实现规则，对象Equal时，必须有相同的hashCode.
	 * 这里 name为null也不影响结果
	 */
	@Override
	public int hashCode() {
//		return (refExtensionPoint+clazz.getName()+name).hashCode();
		return (refExtensionPoint+name).hashCode();
	}

	public IExtensionFactory getFactory() {
		return factory;
	}

	/**
	 * 重复 Extension的标准：重复的refExtensionPoint、clazz、name、propertyList
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Extension))
			return false;
		else {
			Extension e = (Extension) obj;
			return
//					clazz.equals(e.getClazz())
//					&&
					(refExtensionPoint.equals(e.refExtensionPoint))
					&&
					 StringKit.eqOrNull(name, e.name)
					&&
//					 checkPropertyDup(propertyList,e.propertyList)
					 factory.contentEqual(e.factory)
					&&
					 StringKit.eqOrNull(this.id,e.id);
		}
	}
		
//
//	private boolean checkPropertyDup(Vector<Property> p1, Vector<Property> p2) {
//		//長度不同
//		if (p1.size()!=p2.size())
//			return false;
//
//		//長度相同，對每一個屬性看能否找到
//		for (Property item:p1) {
//
//			boolean found=false;
//			for (Property o:p2) {
//				if (StringKit.eqOrNull(item.key,o.key) && StringKit.eqOrNull(item.value, o.value)) {
//					found = true;
//					break;
//				}
//			}
//			//如果上面的循環執行完畢，仍然沒有找到
//			if (!found)
//				return false;
//		}
//		//相同
//		return true;
//	}

	public String getExtensionPointName(){
		return this.refExtensionPoint;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Class getClazz(){
//		return this.clazz;
		return this.factory.getImplClass();
	}

//	/**
//	 * to bo removed!!!!
//	 * @return
//	 */
//	public List<Property> getProperties(){
//			List<ObjectFactory.Property> props = ((ObjectFactory) this.factory).__debugGetPropertys();
//			if (props == null){
//				return new ArrayList<>();
//			}
//			List<Property> rets = new ArrayList<>();
//			for (ObjectFactory.Property p:props){
//				Property o = new Property();
//				o.key = p.getKey();
//				o.value = p.getValue();
//				rets.add(o);
//			}
//			return rets;
//	}
	
	public Object getObject(){
		return extensionObject;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
//	public static class Property{
//		String key;
//		String value;
//		public String getKey() {
//			return key;
//		}
//		public String getValue() {
//			return value;
//		}
//		public void setValue(String v){
//			value = v;
//		}
//
//	}
//
	public synchronized void load() throws Exception{
		this.extensionObject = factory.create(this);
		if (!(this.extensionObject instanceof String)){
			PluginEnvirement.getInstance().resolveRefAnnotation(this.extensionObject);
		}
//		if (propertyFilter!=null){
//			filterProperty(this.propertyList);
//		}
//		if (this.extensionObject == null){
//			if (clazz.equals(String.class)){
//				//字符串类型采用特殊加载方式
//				if (this.propertyList.size()!=1){
//					throw new RuntimeException("String type extension must has one property with the val");
//				}
//				this.extensionObject = this.propertyList.get(0).getValue();
//			}else{
//				this.extensionObject = clazz.newInstance();
//				//处理extension工厂机制
//				this.extensionObject = resolveFactory(this.extensionObject);
//
//				PluginEnvirement.getInstance().resolveRefAnnotation(this.extensionObject);
//
//				//带属性的加载方式
//				if (this.propertyList.size()>0){
//					setProperty(this.extensionObject,this.propertyList);
//				}
//			}
//		}
	}
	
//	private Object resolveFactory(Object o) {
//		if (o instanceof IExtensionFactory) {
//			return ((IExtensionFactory)o).create();
//		}else {
//			return o;
//		}
//	}
	
//	private void filterProperty(Vector<Property> list) {
//		for (Property p:list){
//			p.setValue(propertyFilter.filte(p.getValue()));
//		}
//	}


//	private static void setProperty(Object o,
//			Vector<Property> p) {
//		//看能否找到method
//		Method method = null;
//		try {
//			method = o.getClass().getMethod("setExtensionProperty", new Class[]{java.util.List.class});
//		} catch (Exception e){
//		}
//
//		if (method != null){
//			ReflactKit.invoke(o,"setExtensionProperty",new Object[]{p});
//		}else{
//			PropertyUtil.setProperties(o,p);
//		}
//	}

	public static Extension createStringExtension(String aPointName,String value){
		return create(aPointName,"", StringExtensionFactory.createFactory(value));
	}
	
	public static Extension create(String aPointName,Class cls){
		return create(aPointName,"",cls);
	}
	
	public static Extension create(String aPointName,String aName,Class cls){
//		return create(aPointName,aName,cls,null);
		return create(aPointName,aName, ObjectFactory.createFactory(cls));
	}

	public static Extension create(String aPointName,Class cls,String[][] property){
		if (cls.equals(String.class))
			throw new RuntimeException("Please call createStrignExtension");
		return create(aPointName,ObjectFactory.createFactory(cls,property));
	}
	public static Extension create(String aPointName,IExtensionFactory fac){
		return create(aPointName,"",fac);
	}

	public static Extension create(String aPointName,String aName,Class cls,String[][] property){
		if (cls.equals(String.class))
			throw new RuntimeException("Please call createStrignExtension");
		return create(aPointName,aName,ObjectFactory.createFactory(cls,property));
	}
	public static Extension create(String aPointName,String aName,IExtensionFactory fac){
		Extension ext = new Extension();
		ext.name = aName;
//		ext.clazz = cls;
		ext.refExtensionPoint = aPointName;
		ext.factory = fac;
//		if (property!=null){
//			for (int i=0;i<property.length;i++){
//				Property p = new Property();
//				p.key = property[i][0];
//				p.value = property[i][1];
//				ext.propertyList.add(p);
//			}
//		}
		return ext;
	}
	
	/**
	 * Extension Id相关的方法和属性
	 */
	private  String id;
	
	public String getId() {
		return id;
	}
	
	/**
	 * 不提供public方法，请调用ExtensionFactory的方法
	 * @param o
	 */
	void setId(String o) {
		this.id = o;
	}
	

	
	public static void main(String[] args) {
		Extension e1 = Extension.create("a", Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		Extension e2 = Extension.create("a", Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		AssertKit.assertTrue(e1.equals(e2));

		e1 = Extension.create("a", Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		e2 = Extension.create("a", Extension.class,new String[][] {{"a1","b1"},{"a2","v3"}});
		AssertKit.assertFalse(e1.equals(e2));

		e1 = Extension.create("a", null,Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		e2 = Extension.create("a", null,Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		AssertKit.assertTrue(e1.equals(e2));

		e1 = Extension.create("a", null,Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		e2 = Extension.create("a", "b",Extension.class,new String[][] {{"a1","b1"},{"a2","v2"}});
		AssertKit.assertFalse(e1.equals(e2));

		e1 = Extension.create("a", Extension.class);
		e2 = Extension.create("a", Extension.class);
		AssertKit.assertTrue(e1.equals(e2));

		e1 = Extension.create("a",Extension.class);
		e2 = Extension.create("a", Extension.class);
		AssertKit.assertTrue(e1.equals(e2));
		
	}
}
