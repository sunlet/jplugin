package net.jplugin.core.kernel.api.compositeapp;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.util.*;

public class ComponentModeConfig {
    public static boolean ENABLED = "true".equalsIgnoreCase(System.getProperty("use-component-mode"));
    /**
     * 能对应到composite app code的plugin才放进map当中
     */
    private static Map<Class,String> plugin2AppcodeMap=new HashMap<>();

    public Set<String>  getComponentAppCodes(){
        HashSet ret = new HashSet();
        ret.addAll(plugin2AppcodeMap.values());
        return ret;
    }

    public static void initPluginAppCodeMapping(){
        if (!ENABLED)
            return;

        List<Class> list = PluginEnvirement.getInstance().getPluginRegistry().getPluginClasses();
        for (Class pluginClazz:list){
            String appcode = ComponentModeConfigHelper.getAppCode(pluginClazz);
            //获取到非空的appcode，才放入map
            if (StringKit.isNotNull(appcode)){
                plugin2AppcodeMap.put(pluginClazz,appcode);
            }
        }

        PluginEnvirement.getInstance().getStartLogger().log("After init composite app plugin mapping, result:");
        PluginEnvirement.getInstance().getStartLogger().log(getMappingString());

    }

    private static Object getMappingString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Class,String> en:plugin2AppcodeMap.entrySet()){
            sb.append("\n").append(en.getKey().getName()).append("-->").append(en.getValue());
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String getCurrThreadAppCode(){
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        /**
         * 逐层查找类对应的Composite
         */
        for (StackTraceElement ste:st){
            Class compositePlugin = getOwnerPlugin(ste.getClassName());
            if (compositePlugin!=null)
                return plugin2AppcodeMap.get(compositePlugin);
        }
        return null;
    }

    private static Class getOwnerPlugin(String className) {
        Class founded = null;
        for (Class pclazz:plugin2AppcodeMap.keySet()){
            String pkg = pclazz.getPackage().getName();
            if (className.startsWith(pkg)){
                if (founded==null){
                    founded = pclazz;
                }else{
                    //选择包名更长的哪一个Plugin
                    if (pclazz.getPackage().getName().length() > founded.getClass().getPackage().getName().length()){
                        founded = pclazz;
                    }
                }
            }
        }
        return founded;
    }
}
