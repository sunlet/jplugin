package test.net.jplugin.core.das.route;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.das.route.ExtensionDasRouteHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import test.net.jplugin.core.das.route.date.DateTest;
import test.net.jplugin.core.das.route.date.DbCreateDate;
import test.net.jplugin.core.das.route.dynamicds.DynamicDsTest;
import test.net.jplugin.core.das.route.groupby.DBGroupByTest;
import test.net.jplugin.core.das.route.groupby.DbGroupByDDL;
import test.net.jplugin.core.das.route.kit.NameReplaceKitTest;
import test.net.jplugin.core.das.route.notExistTable.NotExistsTableTest;
import test.net.jplugin.core.das.route.span_dml.MultiExecuteTest;
import test.net.jplugin.core.das.route.span_dml.SpanInsertTest;
import test.net.jplugin.core.das.route.span_dml.SpanUpdateDeleteTest;
import test.net.jplugin.core.das.route.stringint.DbCreateStringInt;
import test.net.jplugin.core.das.route.stringint.DeleteTest;
import test.net.jplugin.core.das.route.stringint.InsertSelectTest;
import test.net.jplugin.core.das.route.stringint.UpdateTest;
import test.net.jplugin.core.das.route.where.InSectUtilTest;
import test.net.jplugin.core.das.route.where.VisitorExpressionManagerTest;
import test.net.jplugin.core.das.route.where.WhereExpressionVisitorTest;
import test.net.jplugin.core.das.route.where.funcs.MytestFunctionHandler;
import test.net.jplugin.core.das.sqlhandler.date.DateHandleTest;
import test.net.jplugin.core.das.sqlhandler.date.HashHandlerTest;

public class Plugin extends AbstractPluginForTest {

	public Plugin(){
		//已经有着两个数据源 "database","db_2";
//		ExtensionDasHelper.addDataSourceExtension(this,"router-db" , "router-db");
//		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-1" , "router-ds-1");
//		ExtensionDasHelper.addDataSourceExtension(this,"router-ds-2" , "router-ds-2");
		
		ExtensionDasHelper.addSqlListenerExtension(this, SqlMonitorListenerTest.class);
		
		ExtensionDasRouteHelper.addSqlFunctionExtension(this,"Mytest",MytestFunctionHandler.class);
		
//		ExtensionDasHelper.addDynamicDataSourceProviderExtension(this, "testdynamicds",DynamicDsTestProvider.class);
	}
	
	
	@Override
	public void test() throws Throwable {
		new HashHandlerTest().test();
		new DateHandleTest().test();
		
		DbCreateStringInt.drop();
		new NotExistsTableTest().test();
		
		DbCreateStringInt.create();
		new MultiExecuteTest().test();
		
		DbCreateStringInt.create();
		new SpanUpdateDeleteTest().test();
		
		DbCreateStringInt.create();
		new SpanInsertTest().test();
		
		DbGroupByDDL.create();
		new DBGroupByTest().test();
		
		DbCreateStringInt.create();
		new InsertSelectTest().test();

		DbCreateStringInt.create();
		new UpdateTest().test();
		DbCreateStringInt.create();
		new DeleteTest().test();
		DbCreateDate.create();
		new DateTest().test();
		new WhereExpressionVisitorTest().test();
		new VisitorExpressionManagerTest().test();
		new InSectUtilTest().test();
		new DynamicDsTest().test();
		new NameReplaceKitTest().test();
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_TS+1;
	}

}
