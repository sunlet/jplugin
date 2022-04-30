package net.jplugin.core.kernel.api;

import java.util.List;

public interface IExtensionFactoryInterceptAble {
    /**
     * 在Contruct以后被调用
     */
    void setNeedIntercept();

    /**
     * 在Load以后被调用
     * @param interceptorList
     */
    void setInterceptors(List<AbstractExtensionInterceptor> interceptorList);
}
