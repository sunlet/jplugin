package jplugincoretest.testUseTransaction;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.UseTransaction;
import net.jplugin.core.ctx.impl.usetxincept.UseTransactionIncept;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.service.api.BindService;
import net.jplugin.core.service.api.RefService;

@BindService(accessClass = TestNotUseTransactionBean.class)
public class TestNotUseTransactionBean implements Initializable {

    @RefService
    TransactionManager txm;

    public void method1(){
        AssertKit.assertEqual(txm.getStatus(),TransactionManager.Status.NOTX);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        AssertKit.assertStackTraceNotHas(TestNotUseTransactionBean.class,UseTransactionIncept.class);
        AssertKit.assertStackTraceNotHas(UseTransactionIncept.class);

    }

    @Override
    public void initialize() {
        this.method1();
    }
}
