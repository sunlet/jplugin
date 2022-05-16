package net.jplugin.core.service.impl.esf.api;

import net.jplugin.core.service.impl.esf.ESFRPCContext;

import java.lang.reflect.Method;

public interface IRPCHandler {
    public Object invokeRPC(ESFRPCContext ctx, String servicePath, final Object obj, final Method method, final Object[] args) throws Throwable;

}
