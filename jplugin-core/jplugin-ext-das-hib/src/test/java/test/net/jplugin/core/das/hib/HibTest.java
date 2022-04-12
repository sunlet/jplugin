package test.net.jplugin.core.das.hib;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.service.api.ServiceFactory;

public class HibTest {

	public void test() {
		IDataService das = ServiceFactory.getService(IDataService.class);

		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		String id;

		try {
			txm.begin();
			DBHibTest o = new DBHibTest();
			o.setName("name");
			das.insert(o);
			id = o.getId();
		} finally {
			txm.commit();
		}

		try {
			txm.begin();
			DBHibTest o = das.findById(DBHibTest.class, id);
			AssertKit.assertEqual(o.getName(), "name");

		} finally {
			txm.commit();
		}
	}
}
