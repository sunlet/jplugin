package net.jplugin.core.kernel.kits;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.SetExtensionId;
import net.jplugin.core.kernel.api.SetExtensionPriority;

import java.lang.annotation.Annotation;

public class ExtensionBindKit {
    public static void handleIdAndPriority(AbstractPlugin p, Class c) {
        {
            SetExtensionId anno = (SetExtensionId) c.getAnnotation(SetExtensionId.class);
            if (anno != null) {
                if (StringKit.isNull(anno.value())) {
                    throw new RuntimeException("SetExtensionId can't have null value. class:" + c.getName());
                }
                Extension.setLastExtensionId(anno.value());
            }
        }
        {
            SetExtensionPriority anno = (SetExtensionPriority) c.getAnnotation(SetExtensionPriority.class);
            if (anno != null) {
                Extension.setLastExtensionPriority(anno.value());
            }
        }
    }

    public static void checkBindBeanNoSetExtensionIdAnno(AbstractPlugin plugin, Class clazz) {
        if (clazz.getAnnotation(SetExtensionId.class)!=null){
            throw new RuntimeException("BindBean anno can't worked with SetExtensionId together.  "+clazz.getName());
        }
        if (clazz.getAnnotation(SetExtensionPriority.class)!=null){
            throw new RuntimeException("BindBean anno can't worked with SetExtensionPriority together. "+clazz.getName());
        }
    }
}
