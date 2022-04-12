package test.net.jplugin.das.hib.mt;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.das.hib.api.IMtDataService;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.service.api.ServiceFactory;

public class MtServiceTest {
	private IMtDataService svc;
	String tid = "t1";

	public void test() {
		String oldid = ThreadLocalContextManager.getRequestInfo()
				.getCurrentTenantId();
		init();
		try {
			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
					this.tid);
			testMtInsertDelete();
			testMtQry();
			testNonMt();
		} finally {
			ThreadLocalContextManager.getRequestInfo()
					.setCurrentTenantId(oldid);
		}
	}

	private void testMtQry() {
		ServiceFactory.getService(IDataService.class).executeDeleteSql(
				"delete from mtpojo where tenantid=?", tid);
		ServiceFactory.getService(TransactionManager.class).begin();
		try {
			MtPojo o = new MtPojo();
			o.setName("zhangsan");
			svc.insert(o);

			o = new MtPojo();
			o.setName("lisi");
			svc.insert(o);
			svc.flush();

			List<MtPojo> list = svc.queryAll(MtPojo.class);
			AssertKit.assertEqual(2, list.size());

			
			try {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("t2");
				list = svc.queryAll(MtPojo.class);
			} finally {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
						tid);
			}
			AssertKit.assertEqual(0, list.size());
			
			list = svc.queryByCond(MtPojo.class,"name=?","zhangsan");
			AssertKit.assertEqual(1, list.size());
			
			try {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("t2");
				list = svc.queryByCond(MtPojo.class,"name=?","zhangsan");
			} finally {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
						tid);
			}
			AssertKit.assertEqual(0, list.size());
			
			list = svc.queryByCondWithPage(MtPojo.class,"name=?",new Object[]{"zhangsan"},null);
			AssertKit.assertEqual(1, list.size());
			
			try {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("t2");
				list = svc.queryByCondWithPage(MtPojo.class,"name=?",new Object[]{"zhangsan"},null);
			} finally {
				ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
						tid);
			}
			AssertKit.assertEqual(0, list.size());
			
		} finally {
			ServiceFactory.getService(TransactionManager.class).commit();
		}
	}

	private void init() {
		this.svc = ServiceFactory.getService(IMtDataService.class);
	}

	private void testMtInsertDelete() {
		MtPojo o = new MtPojo();
		o.setName("zhangsan");
		svc.insert(o);
		long id = o.getId();

		svc.flush();
		o = svc.findById(MtPojo.class, id);
		AssertKit.assertEqual(tid, o.getTenantId());

		svc.flush();
		svc.delete(o);
		svc.flush();

		o = new MtPojo();
		o.setName("zhangsan");
		svc.insert(o);
		svc.flush();

		svc.delete(MtPojo.class, o.getId());
		svc.flush();
		
		
		try {
			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId("t2");
			final MtPojo o2 = new MtPojo();
			o2.setName("zhangsan");
			o2.setTenantId("t2");//必须是空
			AssertKit.assertException(new Runnable() {
				public void run() {
					svc.insert(o2);
				}
			});
			
		} finally {
			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
					tid);
		}
		
		try {
			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(null);
			final MtPojo o2 = new MtPojo();
			o2.setName("zhangsan");
			AssertKit.assertException(new Runnable() {
				
				public void run() {
					svc.insert(o2);
				}
			});
			
		} finally {
			ThreadLocalContextManager.getRequestInfo().setCurrentTenantId(
					tid);
		}
	}

	private void testNonMt() {

	}
}
