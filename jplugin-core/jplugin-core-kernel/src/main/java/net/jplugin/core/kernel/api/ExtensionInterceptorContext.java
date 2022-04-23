package net.jplugin.core.kernel.api;

import java.lang.reflect.Method;

public class ExtensionInterceptorContext {
    Extension extension;
    private Method method;
    private Object[] args;
    //这个是用来服务与javassisit代理的无接口实现的
    private Method _procceedMethod;

    /**
     * 无接口实现调用这个
     * @param e
     * @param aMethod
     * @param aArgs
     * @param aProcceedMethod
     */
    public void init(Extension e, Method aMethod, Object[] aArgs,Method aProcceedMethod) {
        init(e,aMethod,aArgs);
        this._procceedMethod = aProcceedMethod;
    }

    public Method _get_procceedMethod() {
        return _procceedMethod;
    }

    /**
     * 有接口实现调用这个
     * @param e
     * @param aMethod
     * @param aArgs
     */
    public void init(Extension e, Method aMethod, Object[] aArgs){
        if (this.extension!=null)
            throw new RuntimeException("can't init twice");
        else {
            this.extension = e;
            this.method = aMethod;
            this.args = aArgs;
        }
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getExtensionId() {
        return extension.getId();
    }

    public String getExtensionName() {
        return extension.getName();
    }

    public String getExtensionPointId() {
        return extension.getExtensionPointName();
    }
}
