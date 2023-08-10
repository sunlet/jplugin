package net.jplugin.core.config.comp;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.config.api.CloudEnvironment;
import net.jplugin.core.kernel.api.Component;
import net.jplugin.core.kernel.api.PluginEnvirement;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentInfoManager {

    private static Map<String, Class> package2PluginMap=new HashMap<>();
    private static Map<Class, Component> plugin2ComponentMap=new HashMap<>();
    private static boolean init;

    /**
     * 这个用法用来检查，是否在非平台的Component当中调用了 平台的配置！ 这种做法是严重不建议的！
     */
    static AtomicInteger totalCnt=new AtomicInteger(0);
    static AtomicInteger errorCnt=new AtomicInteger(0);

    /**
     * 下面两个方法用来做一些检查，就是本来应该使用CompConfigFacotry，但是使用了ConfigFactory
     */
    public static void checkErrorCallPlatformConfig(){
        if (!init) return;
        if (totalCnt.get()>=10000 || errorCnt.get()>=30) return;

        totalCnt.incrementAndGet();
        Component comp = getComponentFromThreadStack();

        if (comp==null){
            errorCnt.incrementAndGet();
            StackTraceElement elem = getTheStackElement();
            elem.getClassName();
            elem.getMethodName();
            elem.getLineNumber();
            PluginEnvirement.INSTANCE.getStartLogger().log("!!!!Call ConfigFactory, But component not found! Perhaps you need a Plugin.java file!" + elem.getClassName()+"."+elem.getMethodName()+" , "+elem.getLineNumber());
        }else if (!comp.isPlatform()){
            errorCnt.incrementAndGet();
            StackTraceElement elem = getTheStackElement();
            elem.getClassName();
            elem.getMethodName();
            elem.getLineNumber();
            PluginEnvirement.INSTANCE.getStartLogger().log("!!!!!Call ConfigFactory, You shoud use CompConfigFactory." + elem.getClassName()+"."+elem.getMethodName()+" , "+elem.getLineNumber());
        }
    }

    /**
     * 返回用来判断调用方所在Component的堆栈位置
     * @return
     */
    private static StackTraceElement getTheStackElement(){
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        /**
         * 逐层查找类对应的Composite
         */
        for (int i=1;i<st.length;i++){
            StackTraceElement ste = st[i];


            String cname = ste.getClassName();
            if (cname.startsWith("net.jplugin.core.config") || cname.startsWith("jdk.") ||  cname.startsWith("java."))
                continue;

//            if (cname.startsWith("java.")){
//                System.out.println("aaa");
//            }

            return ste;
        }
        return null;
    }


    public static Component getComponentFromThreadStack(){
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        /**
         * 逐层查找类对应的Composite
         */
        for (int i=1;i<st.length;i++){
            StackTraceElement ste = st[i];

            String cname = ste.getClassName();
            if (cname.startsWith("net.jplugin.core.config") || cname.startsWith("jdk.") ||  cname.startsWith("java."))
                continue;

            try {
                String name = ste.getClassName();
                return getComponentFromClass(Class.forName(name));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    /**
     * 从任何一个类，获取对应的Component
     * @param c
     * @return
     */
    public static Component getComponentFromClass(Class c){
        if (!CloudEnvironment.isUseComponentMode()) {
            throw new RuntimeException("state not right");
        }

        String pkgname = c.getPackage().getName();
        while(true){
            Class plugClass = package2PluginMap.get(pkgname);
            if (plugClass!=null){
                //找到了匹配的包，返回
                return plugin2ComponentMap.get(plugClass);
            }else{
                int pos = pkgname.lastIndexOf('.');
                if (pos<0) {
                    //没有找到，返回null
                    return null;
                }else {
                    //获取父包，继续判断
                    pkgname = pkgname.substring(0,pos);
                }
            }
        }
    }


    public static void init() {
        if (!CloudEnvironment.isUseComponentMode()) {
           throw new RuntimeException("state not right");
        }
        
        init = true;

        Map<String,Component> path2Comp= new HashMap<>();
        List<Class> classes = PluginEnvirement.INSTANCE.getPluginRegistry().getPluginClasses();
        for (Class clazz : classes){
            Tuple2<String, Integer> tp = ComponentModeConfigHelper.getStorePathAndType(clazz);
            //如果path2Comp
            if (path2Comp.containsKey(tp.first)){
                //获取旧的
                Component comp = plugin2ComponentMap.get(path2Comp.get(tp.first));
                //放入
                plugin2ComponentMap.put(clazz, comp);
            }else{
                //创建一个新的
                Component comp = createComponent(tp.first,tp.second);

                //放入
                plugin2ComponentMap.put(clazz, comp);
                path2Comp.put(tp.first, comp);
//                if (comp.isPlatform() || StringKit.isNotNull(comp.getComponentCode())) {
//                    //放入
//                    plugin2ComponentMap.put(clazz, comp);
//                    path2Comp.put(tp.first, comp);
//                }else{
////                    throw new RuntimeException("The component must has two status: either has appcode, or is a platform component. " + comp.getStorePath());
//                }
            }

            //
            package2PluginMap.put(clazz.getPackage().getName(), clazz);
        }

        PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Plugin to Component info:");
        PluginEnvirement.INSTANCE.getStartLogger().log(getComponentMappingString());
    }


    private static Object getComponentMappingString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Class,Component> en:plugin2ComponentMap.entrySet()){
            sb.append("\n").append(en.getKey().getName()).append("-->").append(en.getValue());
        }
        sb.append("\n");
        return sb.toString();
    }




    private static Component createComponent(String path, Integer type) {
        Component comp = Component.create(path, type);
        comp.setComponentCode(ComponentModeConfigHelper.getAppCode(path,type));
        comp.setPlatform(PlatformJarSetChecker.checkPlatform(path,type));
        return comp;
    }

}
