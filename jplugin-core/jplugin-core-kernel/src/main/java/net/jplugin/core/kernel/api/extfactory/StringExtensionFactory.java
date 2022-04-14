package net.jplugin.core.kernel.api.extfactory;

import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.IExtensionFactory;

public class StringExtensionFactory implements IExtensionFactory {
    String value;

    public static StringExtensionFactory createFactory(String v){
        StringExtensionFactory o = new StringExtensionFactory();
        o.value = v;
        return o;
    }

    @Override
    public String create() {
        return value;
    }

    @Override
    public Class getTargetClass() {
        return String.class;
    }

    @Override
    public boolean contentEqual(IExtensionFactory f) {
        return  (f instanceof StringExtensionFactory)
                &&
                value.equals(((StringExtensionFactory)f).value);
    }
}
