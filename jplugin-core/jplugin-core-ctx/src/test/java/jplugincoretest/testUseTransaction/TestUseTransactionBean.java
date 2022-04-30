package jplugincoretest.testUseTransaction;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.UseTransaction;
import net.jplugin.core.ctx.impl.usetxincept.UseTransactionIncept;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.service.api.BindService;
import net.jplugin.core.service.api.RefService;

@BindService(accessClass = TestUseTransactionBean.class)
public class TestUseTransactionBean implements Initializable {

    @RefService
    TransactionManager txm;

    @UseTransaction
    public void method1(){
        AssertKit.assertEqual(txm.getStatus(),TransactionManager.Status.INTX);
        AssertKit.assertStackTraceHas(UseTransactionIncept.class);
    }

    @UseTransaction
    private void privateMethod(){
        AssertKit.assertEqual(txm.getStatus(),TransactionManager.Status.NOTX);
        AssertKit.assertStackTraceNotHas(UseTransactionIncept.class);
    }
    @UseTransaction
    protected void protectedMethod(){
        AssertKit.assertEqual(txm.getStatus(),TransactionManager.Status.INTX);
        AssertKit.assertStackTraceHas(UseTransactionIncept.class);
    }

    @UseTransaction
    void defaultMethod(){
        AssertKit.assertEqual(txm.getStatus(),TransactionManager.Status.INTX);
        AssertKit.assertStackTraceHas(UseTransactionIncept.class);
    }
    @Override
    public void initialize() {
        this.method1();
        this.privateMethod();
        this.protectedMethod();
        this.defaultMethod();
    }
}
