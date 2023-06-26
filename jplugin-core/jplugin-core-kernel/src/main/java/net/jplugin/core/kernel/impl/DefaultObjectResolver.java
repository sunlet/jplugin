package net.jplugin.core.kernel.impl;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;
import net.jplugin.core.kernel.api.IObjectResolver;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class DefaultObjectResolver implements IObjectResolver {
    private IAnnoForAttrHandler[] handlers;
    private Class[] annoClassArr;

    private void init() {
        handlers = PluginEnvirement.getInstance().getExtensionPoint(net.jplugin.core.kernel.Plugin.EP_ANNO_FOR_ATTR)
                .getExtensionObjects(IAnnoForAttrHandler.class);
        annoClassArr = new Class[handlers.length];
        for (int i = 0; i < handlers.length; i++) {
            annoClassArr[i] = handlers[i].getAnnoClass();
        }
    }
    @Override
    public void resolve(Object obj) {

        // 初始化以后就操作
        if (handlers == null)
            init();

        if (String.class.equals(obj.getClass()))
            return;

        List<Field> fields = ReflactKit.getAllFields(obj);
        for (Field field : fields) {
            for (int i = 0; i < annoClassArr.length; i++) {
                if (field.isAnnotationPresent(annoClassArr[i])) {
                    try {
                        handleAnnoted(obj, field, handlers[i]);
                    } catch (Exception e) {
                        throw new RuntimeException("Error when resolve annotation ref: class =" + obj.getClass().getName()
                                + "  field=" + field.getName()+"   Caused by:"+e.getMessage(), e);
                    }
                }
            }
        }
    }

    private void handleAnnoted(Object obj, Field field, IAnnoForAttrHandler h) {
        Object oldV;
        oldV = ReflactKit.getFieldValueForce(field, obj);

        if (oldV != null) {
            if (Modifier.isStatic(field.getModifiers()))
                return;
            else
                throw new RuntimeException("None static JPlugin annotated attribute value must be init as null."
                        + obj.getClass().getName());
        }

        if (!h.getAttrClass().isAssignableFrom(field.getType()))
            throw new RuntimeException(
                    "The type of annotated attribute " + h.getAnnoClass().getName() + " must be subclass of "
                            + h.getAttrClass() + " ," + obj.getClass().getName() + "." + field.getName());

        Annotation anno = field.getAnnotation(h.getAnnoClass());
        Object newV = h.getValue(obj, field.getType(),field, anno);
        if (newV == null)
            throw new RuntimeException("Annotated attribute value retrived null: " + h.getAnnoClass().getName() + " ,"
                    + obj.getClass().getName() + "." + field.getName());
        else {
            ReflactKit.setFieldValueForce(field, obj, newV);
        }
    }
}
