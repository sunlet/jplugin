package net.jplugin.ext.webasic.impl;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.impl.esf.ESFRPCContext;

public class ESFRPCRequestInfoFillerMt {
    public static void fill(ESFRPCContext ctx){

        RequesterInfo info = ThreadLocalContextManager.getRequestInfo();
        
        //tenantid
        String tid = ctx.getTenantId();
        MtInvocationFilterHandler.instance.checkAndSet(info, tid);
    }
}
