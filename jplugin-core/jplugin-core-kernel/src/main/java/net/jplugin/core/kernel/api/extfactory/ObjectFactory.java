package net.jplugin.core.kernel.api.extfactory;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.IExtensionFactory;

import java.util.Vector;

public class ObjectFactory implements IExtensionFactory {
    private Class clazz;
    private String[][] properties;

    public static ObjectFactory createFactory(Class c){
        ObjectFactory o = new ObjectFactory();
        o.clazz = c;
        return o;
    }

    @Override
    public Object create() {
        return null;
    }

    @Override
    public Class getTargetClass() {
        return clazz;
    }

    @Override
    public boolean contentEqual(IExtensionFactory f) {
        return (f instanceof  ObjectFactory) &&
                ((ObjectFactory)f).clazz==clazz &&
                propertyEquals(((ObjectFactory)f).properties,properties);
    }

    private boolean propertyEquals(String[][] properties, String[][] properties1) {
    }


    private boolean checkPropertyDup(Vector<Extension.Property> p1, Vector<Extension.Property> p2) {
        //長度不同
        if (p1.size()!=p2.size())
            return false;

        //長度相同，對每一個屬性看能否找到
        for (Extension.Property item:p1) {

            boolean found=false;
            for (Extension.Property o:p2) {
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
}
