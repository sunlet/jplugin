package net.jplugin.core.kernel.kits;

import net.jplugin.core.kernel.api.PluginEnvirement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class RefExtensionPointInferenceKit {
    public static String inference(Object theObject, Field f, String annoName) {
        Class<?> type = f.getType();
        String pointName;
        if (List.class.isAssignableFrom(type)){
            ParameterizedType ptype = (ParameterizedType)f.getGenericType();
            Type[] args = ptype.getActualTypeArguments();
            Class objType = (Class) args[0];
            pointName =  getPointName(theObject, f, annoName, objType);
        }else if (Map.class.isAssignableFrom(type)){
            ParameterizedType ptype = (ParameterizedType)f.getGenericType();
            Type[] args = ptype.getActualTypeArguments();
            Class objType = (Class) args[1];
            pointName = getPointName(theObject, f, annoName, objType);
        }else{
            Class objType = f.getType();
            pointName = getPointName(theObject, f, annoName, objType);
        }
        return pointName;
    }

    private static String getPointName(Object theObject, Field f, String annoName, Class objType) {
        boolean hasPoint = PluginEnvirement.getInstance().hasExtensionPoint(objType.getName());
        if (hasPoint)
            return objType.getName();
        else{
            throw new RuntimeException("Can't inference the pointTo attribute for "+ annoName +", obj is:"+ theObject.getClass().getName()+"  field:"+ f.getName());
        }
    }
}
