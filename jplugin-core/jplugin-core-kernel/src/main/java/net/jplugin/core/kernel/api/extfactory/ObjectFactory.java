package net.jplugin.core.kernel.api.extfactory;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.IExtensionFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.impl.PropertyUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ObjectFactory implements IExtensionFactory {

    private Class implClazz;
    private Class accessClazz;
    private List<Property> propertyList;

    public ObjectFactory property(String name,String value){
        if (this.propertyList==null)
            this.propertyList= new ArrayList<>();
        Property p = new Property();
        p.key = name;
        p.value = value;
        this.propertyList.add(p);
        return this;
    }

    public static ObjectFactory createFactory(Class c){
        return createFactory(c,c,null);
    }

    public static ObjectFactory createFactory(Class access,Class impl){
        return createFactory(access,impl,null);
    }

    public static ObjectFactory createFactory(Class c,String[][] property){
        return createFactory(c,c,property);
    }

    public static ObjectFactory createFactory(Class access,Class impl,String[][] property){
        ObjectFactory o = new ObjectFactory();
        o.implClazz = impl;
        o.accessClazz = access;

        if (property!=null){
            for (int i=0;i<property.length;i++){
                o.property(property[i][0],property[i][1]);
            }
        }
        return o;
    }

    @Override
    public Object create() {
		if (Extension.propertyFilter!=null && this.propertyList!=null){
			filterProperty(this.propertyList);
		}

        Object extensionObject = null;
        try {
            extensionObject = implClazz.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

		//处理extension工厂机制
		extensionObject = resolveFactory(extensionObject);

        PluginEnvirement.getInstance().resolveRefAnnotation(extensionObject);

				//带属性的加载方式
        if (this.propertyList!=null && this.propertyList.size()>0){
            setProperty(extensionObject,this.propertyList);
		}
        return extensionObject;
    }


    private Object resolveFactory(Object o) {
		if (o instanceof IExtensionFactory) {
			return ((IExtensionFactory)o).create();
		}else {
			return o;
		}
	}

    private static void setProperty(Object o,
                                    List<Property> p) {
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

    private void filterProperty(List<Property> list) {
        for (Property p:list){
            p.setValue(Extension.propertyFilter.filte(p.getValue()));
        }
    }


    @Override
    public Class getAccessClass() {
        return accessClazz;
    }

    @Override
    public boolean contentEqual(IExtensionFactory f) {
        return (f instanceof  ObjectFactory) &&
                ((ObjectFactory)f).accessClazz==accessClazz &&
                ((ObjectFactory)f).implClazz==implClazz &&
                propertyEquals(((ObjectFactory)f).propertyList,propertyList);
    }

    private boolean propertyEquals(List<Property> p1, List<Property> p2) {
        if (p1==null && p2==null)
            return true;

        if (p1==null || p2==null)
            return false;

        //長度不同
		if (p1.size()!=p2.size())
			return false;

		//長度相同，對每一個屬性看能否找到
		for (Property item:p1) {

			boolean found=false;
			for (Property o:p2) {
				if (StringKit.eqOrNull(item.key,o.key) && StringKit.eqOrNull(item.value, o.value)) {
					found = true;
					break;
				}
			}
			//如果上面的循環執行完畢，仍然沒有找到
			if (!found)
				return false;
		}
		//相同
		return true;
    }


    private boolean checkPropertyDup(Vector<Property> p1, Vector<Property> p2) {
        //長度不同
        if (p1.size()!=p2.size())
            return false;

        //長度相同，對每一個屬性看能否找到
        for (Property item:p1) {

            boolean found=false;
            for (Property o:p2) {
                if (StringKit.eqOrNull(item.key,o.key) && StringKit.eqOrNull(item.value, o.value)) {
                    found = true;
                    break;
                }
            }
            //如果上面的循環執行完畢，仍然沒有找到
            if (!found)
                return false;
        }
        //相同
        return true;
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
}
