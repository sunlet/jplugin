package net.jplugin.core.kernel.api.compositeapp;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentModeConfig {
    public static boolean ENABLED = "true".equalsIgnoreCase(System.getProperty("component-mode"));

    /**
     * 能对应到composite app code的plugin才放进map当中
     */
    private static Map<AbstractPlugin,String> plugin2AppcodeMap=new HashMap<>();

    public static void initPluginAppCodeMapping(){
        if (!ENABLED)
            return;

        List<AbstractPlugin> list = PluginEnvirement.getInstance().getPluginRegistry().getPluginList();
        for (AbstractPlugin plugin:list){
            String appcode = ComponentModeConfigHelper.getAppCode(plugin.getClass());
            //获取到非空的appcode，才放入map
            if (StringKit.isNotNull(appcode)){
                plugin2AppcodeMap.put(plugin,appcode);
            }
        }

        PluginEnvirement.getInstance().getStartLogger().log("After init composite app plugin mapping, result:");
        PluginEnvirement.getInstance().getStartLogger().log(getMappingString());

    }

    private static Object getMappingString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<AbstractPlugin,String> en:plugin2AppcodeMap.entrySet()){
            sb.append("\n").append(en.getKey().getClass().getName()).append("-->").append(en.getValue());
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
            AbstractPlugin compositePlugin = getOwnerPlugin(ste.getClassName());
            if (compositePlugin!=null)
                return plugin2AppcodeMap.get(compositePlugin);
        }
        return null;
    }

    private static AbstractPlugin getOwnerPlugin(String className) {
        AbstractPlugin founded = null;
        for (AbstractPlugin p:plugin2AppcodeMap.keySet()){
            String pkg = p.getClass().getPackage().getName();
            if (className.startsWith(pkg)){
                if (founded==null){
                    founded = p;
                }else{
                    //选择包名更长的哪一个Plugin
                    if (p.getClass().getPackage().getName().length() > founded.getClass().getPackage().getName().length()){
                        founded = p;
                    }
                }
            }
        }
        return founded;
    }
}
