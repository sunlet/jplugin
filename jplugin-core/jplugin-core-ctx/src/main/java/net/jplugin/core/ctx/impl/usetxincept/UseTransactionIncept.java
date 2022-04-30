package net.jplugin.core.ctx.impl.usetxincept;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.impl.RuleLoggerHelper;
import net.jplugin.core.ctx.impl.TransactionManagerAdaptor;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;
import net.jplugin.core.kernel.api.ExtensionInterceptorContext;
import net.jplugin.core.service.api.RefService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UseTransactionIncept extends AbstractExtensionInterceptor {
    @RefService(name = "net.jplugin.core.ctx.api.TransactionManager")
    TransactionManagerAdaptor txm;

    @Override
    protected Object execute(FilterChain fc, ExtensionInterceptorContext ctx) throws Throwable {
        Method method = ctx.getMethod();
        TransactionManager.Status initTxStatus = txm.getStatus();

        if (initTxStatus == TransactionManager.Status.NOTX) {
            txm.begin(method.getName());
        } else {
            if (TransactionManagerAdaptor.isLogTx)
                RuleLoggerHelper.dolog("tx no need begin -" + method.getName());
        }
        try {
            Object ret = fc.next(ctx);

            if (initTxStatus == TransactionManager.Status.NOTX) {
                txm.commit(method.getName());
            } else {
                if (TransactionManagerAdaptor.isLogTx)
                    RuleLoggerHelper.dolog("tx no need commit -" + method.getName());
            }

            return ret;
        } catch (Throwable th) {
            if (initTxStatus == TransactionManager.Status.NOTX) {
                txm.rollback(method.getName());
            } else {
                if (TransactionManagerAdaptor.isLogTx)
                    RuleLoggerHelper.dolog("tx no need rollback -" + method.getName());
            }
            throw getRethrow(th);
        }
    }

    private static Throwable getRethrow(Throwable th) throws Throwable {
        if (th instanceof InvocationTargetException){
            return ((InvocationTargetException)th).getTargetException();
        }else{
            return th;
        }
    }
}
