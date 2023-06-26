package net.jplugin.core.service.impl.esf;

import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class ESFRPCRquestInfoFillerBasic {

    public static void fill(ESFRPCContext ctx) {
        RequesterInfo info = ThreadLocalContextManager.getRequestInfo();
        info.setCallerIpAddress(ctx.getCallerIpAddress());
        info.setClientAppCode(ctx.getClientAppCode());
        info.setClientAppToken(ctx.getClientAppToken());
        info.setOperatorId(ctx.getOperatorId());
        info.setOperatorToken(ctx.getOperatorToken());
        info.setRequestUrl(ctx.getRequestUrl());
    }
}
