package net.jplugin.core.service.impl.esf;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.Plugin;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.core.service.impl.esf.api.IRPCHandler;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ESFHelper2 {
    static boolean init = false;
    static Map<String, Object> objectsMap = null;


    public static int SERVICE_TIME_LIMIT = 6000;

    static {
        SERVICE_TIME_LIMIT = ConfigFactory.getIntConfig("platform.service-time-limit", 6000);
        PluginEnvirement.getInstance().getStartLogger().log("$$$ platform.service-time-limit is " + SERVICE_TIME_LIMIT);
    }


    /**
     * 默认采用无webasic情况下的rpc调用，可以被设置
     */
    static IRPCHandler rpcHandler = new DefaultRpcHandler();

    public static void init() {
        if (!init) {
            synchronized (ESFHelper2.class) {
                if (!init) {
                    //在ServiceFactory中初始化
                    List<Extension> list = PluginEnvirement.getInstance().getExtensionList(Plugin.EP_SERVICE_EXPORT);
                    for (Extension ext:list){
                        ServiceFactory._addMapping("", ext.getObject(), ext.getFactory().getImplClass());
                    }

                    //初始化ObjectsMap
                    objectsMap = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_SERVICE_EXPORT);
                    init = true;
                }
            }
        }
    }

    public static void setRPCHandler(IRPCHandler h) {
        rpcHandler = h;
    }

    public static Object invokeRPC(ESFRPCContext ctx, String servicePath, final Object obj, final Method method, final Object[] args) throws Throwable {
        return rpcHandler.invokeRPC(ctx, servicePath, obj, method, args);
    }

    public static Object getObject(String uri) {
        init();
        return objectsMap.get(uri);
    }

    public static Map<String, Object> getObjectsMap() {
        init();
        return Collections.unmodifiableMap(objectsMap);
    }

    public static class DefaultRpcHandler implements IRPCHandler {
        @Override
        public Object invokeRPC(ESFRPCContext ctx, String servicePath, Object obj, Method method, Object[] args) throws Throwable {
            try {
                ThreadLocalContextManager.instance.createContext();

                checkTimeOut(ctx.getMsgReceiveTime());
                ESFRPCRquestInfoFillerBasic.fill(ctx);
                return method.invoke(obj, args);
            } finally {
                ThreadLocalContextManager.instance.releaseContext();
            }
        }
    }

    private static void checkTimeOut(long msgReceiveTime) {
        if (msgReceiveTime > 0 && (System.currentTimeMillis() - msgReceiveTime) > SERVICE_TIME_LIMIT) {
            throw new RuntimeException("执行超时. limit=" + SERVICE_TIME_LIMIT);
        }
    }
}
