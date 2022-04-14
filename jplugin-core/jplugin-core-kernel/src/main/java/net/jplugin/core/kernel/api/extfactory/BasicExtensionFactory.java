package net.jplugin.core.kernel.api.extfactory;

import net.jplugin.core.kernel.api.IExtensionFactory;

public class BasicExtensionFactory implements IExtensionFactory<T> {
    private final Class clazz;

aaaaaaaaaaaaaaaa
    要实现equals，hascode

    public BasicExtensionFactory(Class implClazz){
        this.clazz = implClazz;
    }
    @Override
    public T create() {
        return null;
    }
}
