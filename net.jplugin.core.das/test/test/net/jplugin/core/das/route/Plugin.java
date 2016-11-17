package test.net.jplugin.core.das.route;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import test.net.jplugin.core.das.route.date.DateTest;
import test.net.jplugin.core.das.route.date.DbCreateDate;
import test.net.jplugin.core.das.route.stringint.DbCreateStringInt;
import test.net.jplugin.core.das.route.stringint.DeleteTest;
import test.net.jplugin.core.das.route.stringint.InsertSelectTest;
import test.net.jplugin.core.das.route.stringint.UpdateTest;

public class Plugin extends AbstractPluginForTest {

	public Plugin(){
		//已经有着两个数据源 "database","db_2";
//		ExtensionDasHelper.addDataSourceExtension(this,"router-db" , "router-db");
//		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-1" , "router-ds-1");
//		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-2" , "router-ds-2");
	}
	
	
	@Override
	public void test() throws Throwable {
		DbCreateStringInt.create();
		new InsertSelectTest().test();
		DbCreateStringInt.create();
		new UpdateTest().test();
		DbCreateStringInt.create();
		new DeleteTest().test();
		DbCreateDate.create();
		new DateTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_TS+1;
	}

}
