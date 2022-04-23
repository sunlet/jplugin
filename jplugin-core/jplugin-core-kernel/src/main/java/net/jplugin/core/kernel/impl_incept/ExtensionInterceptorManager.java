package net.jplugin.core.kernel.impl_incept;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.Plugin;
import net.jplugin.core.kernel.api.*;

import java.util.ArrayList;
import java.util.List;

import static net.jplugin.core.kernel.api.PluginEnvirement.*;

public class ExtensionInterceptorManager {
    private static List<Extension> list=new ArrayList<>();
    private static List<Extension> extensionsNeedIntercept=new ArrayList<>();


    public static void setInterceptorsToFactories(){
        ExtensionPoint point = getInstance().getExtensionPoint(Plugin.EP_EXTENSION_INTERCEPTOR);
        //重新设置list非常重要，之所以重新设置List，是因为wire过程中进行了排序
        List<Extension> sortedList = point.__debugGetExtensions();
        AssertKit.assertEqual(sortedList.size(),list.size());
        list = sortedList;

        for (Extension ext:extensionsNeedIntercept){
            List<IExtensionInterceptor> interceptorList = getInterceptorList(ext);
            ((IExtensionFactoryInterceptAble)ext.getFactory()).setInterceptors(interceptorList);
        }
    }

    public static void setNeedIntercept(){
        List<AbstractPlugin> plugins = getInstance().getPluginRegistry().getPluginList();
        //第一遍循环，找到拦截器扩展
        for (AbstractPlugin plugin:plugins){
            List<Extension> exts = plugin.getExtensions();
            for (Extension ext:exts){
                if (ext.getExtensionPointName().equals(Plugin.EP_EXTENSION_INTERCEPTOR)){
                   list.add(ext);
                }
            }
        }
        
        //第二遍，找到需要拦截的扩展
        for (AbstractPlugin plugin:plugins){
            List<Extension> exts = plugin.getExtensions();
            for (Extension ext:exts){
                if (checkNeedIntercept(ext)) {
                    if (ext.getFactory() instanceof IExtensionFactoryInterceptAble) {
                        IExtensionFactoryInterceptAble efia = (IExtensionFactoryInterceptAble) ext.getFactory();
                        extensionsNeedIntercept.add(ext);
                        efia.setNeedIntercept();
                    }else{
                        PluginEnvirement.getInstance().getStartLogger().log("Extension "+ext.getClazz().getName()+" not support intercept.");
                    }
                }
            }
        }
    }

    /**
     * 获取匹配的拦截器列表，这个方法和后一个方法checkNeedIntercept 调用时机不同，所以需要有两个方法
     * @param ext
     * @return
     */
    private static List getInterceptorList(Extension ext) {
        List<IExtensionInterceptor> result = new ArrayList<>(list.size());
        for (Extension interceptorExtension:list){
            IExtensionFactory fac = interceptorExtension.getFactory();
            if (((ExtensionInterceptorFactory)fac).matchExtension(ext)) {
                result.add((IExtensionInterceptor) interceptorExtension.getObject());
            }
        }
        AssertKit.assertTrue(result.size()>0);
        return result;
    }

    /**
     * 判断是否有匹配的拦截器
     * @param ext
     * @return
     */
    private static boolean checkNeedIntercept(Extension ext) {
        for (Extension interceptorExtension:list){
            IExtensionFactory fac = interceptorExtension.getFactory();
            if (((ExtensionInterceptorFactory)fac).matchExtension(ext)) {
                return true;
            }
        }
        return false;
    }

}
