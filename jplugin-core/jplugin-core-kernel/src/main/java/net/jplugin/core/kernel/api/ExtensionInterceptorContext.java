package net.jplugin.core.kernel.api;

import net.jplugin.core.kernel.impl_incept.IInstanceLevelInfo;

import java.lang.reflect.Method;

public class ExtensionInterceptorContext {
    IInstanceLevelInfo instanceLeveInfo;
    private Method method;
    private Object[] args;

    //继续执行需要执行的方法，接口代理情况下就是method，类代理情况下是另一个方法
    private Method procceedMethod;

//    /**
//     * 无接口实现调用这个
//     * @param e
//     * @param aMethod
//     * @param aArgs
//     * @param aProcceedMethod
//     */
//    public void init(Extension e, Method aMethod, Object[] aArgs,Method aProcceedMethod) {
//        init(e,aMethod,aArgs);
//        this._procceedMethod = aProcceedMethod;
//    }

//    public Method _get_procceedMethod() {
//        return _procceedMethod;
//    }

//    /**
//     * 有接口实现调用这个
//     * @param e
//     * @param aMethod
//     * @param aArgs
//     */
    public void init(Method aMethod, Object[] aArgs,IInstanceLevelInfo ili,Method aProceedMethod){
        if (this.method!=null)
            throw new RuntimeException("can't init twice");
        else {
            this.instanceLeveInfo = ili;
            this.method = aMethod;
            this.args = aArgs;
            this.procceedMethod = aProceedMethod;
        }
    }

    /**
     * 获取当前调用的方法。接口情况下是接口的方法。
     * @return
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 获取当前调用的参数
     * @return
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 获取当前对应的扩展的ID
     * @return
     */
    public String getExtensionId() {
//        return extension.getId();
        return this.instanceLeveInfo.getExtension().getId();
    }


    /**
     * 获取当前对应的扩展点名字
     * @return
     */
    public String getExtensionPointName() {
//        return extension.getExtensionPointName();
        return this.instanceLeveInfo.getExtension().getExtensionPointName();
    }

    /**
     * 获取当前对应的Extension对象
     * @return
     */
    public Extension getExtension() {
        return this.instanceLeveInfo.getExtension();
    }

    /**
     * 获取继续执行需要调用方法。此方法一般不需要使用，请调用filter.next(ctx)继续后续过滤。
     * @return
     */
    public Method getProceedMethod(){
        return this.procceedMethod;
    }

    /**
     * 获取继续执行需要调用方法。此方法一般不需要使用，请调用filter.next(ctx)继续后续过滤。
     * @return
     */
    public Object getProceedObject(){
        return this.instanceLeveInfo.getProceedObject();
    }
}
