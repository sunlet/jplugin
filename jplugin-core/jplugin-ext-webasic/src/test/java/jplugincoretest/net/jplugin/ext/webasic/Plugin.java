package jplugincoretest.net.jplugin.ext.webasic;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jplugincoretest.net.jplugin.ext.webasic.restclient.ServiceBean;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.rclient.ExtendsionClientHelper;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import jplugincoretest.net.jplugin.ext.webasic.annotation.AnnoTest;
import jplugincoretest.net.jplugin.ext.webasic.annotation.IRuleTestForAnno;
import jplugincoretest.net.jplugin.ext.webasic.annotation.RuleTestForAnno;
import jplugincoretest.net.jplugin.ext.webasic.annotation.ServiceExportTest;
import jplugincoretest.net.jplugin.ext.webasic.annotation.WebControllerTest;
import jplugincoretest.net.jplugin.ext.webasic.annotation.WebExControllerTest;
import jplugincoretest.net.jplugin.ext.webasic.bind.TestBind;
import jplugincoretest.net.jplugin.ext.webasic.dynamicmethod.DynamicMethodTest;
import jplugincoretest.net.jplugin.ext.webasic.dynamicmethod.TestDynamicMethodClient;
import jplugincoretest.net.jplugin.ext.webasic.mapreturn.ExportForMapBeanTest;
import jplugincoretest.net.jplugin.ext.webasic.mapreturn.ExportForMapReturn;
import jplugincoretest.net.jplugin.ext.webasic.mttest.MtTestClient;
import jplugincoretest.net.jplugin.ext.webasic.mttest.MtTestForRequest;
import jplugincoretest.net.jplugin.ext.webasic.restclient.IService;
import jplugincoretest.net.jplugin.ext.webasic.restclient.ServiceFilterTest;
import jplugincoretest.net.jplugin.ext.webasic.restclient.TestDefaultValClient;
import jplugincoretest.net.jplugin.ext.webasic.restclient.TestRestClient;
import jplugincoretest.net.jplugin.ext.webasic.restclient.WebCtrlFilterTest;
import jplugincoretest.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo;
import jplugincoretest.net.jplugin.ext.webasic.webctrl.DemoWebCtrl;
import jplugincoretest.net.jplugin.ext.webasic.webctrl.DemoWebExCtrl;
import jplugincoretest.net.jplugin.ext.webasic.webctrl.WebCtrlTestClient;
import jplugincoretest.net.jplugin.ext.webasic.webctrl.WebExCtrlTestClient;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午04:57:02
 **/

@PluginAnnotation
public class Plugin extends AbstractPluginForTest{

	public Plugin(){
		ExtensionWebHelper.addWebControllerExtension(this,"/demowebctrl", DemoWebCtrl.class);
		ExtensionWebHelper.addWebExControllerExtension(this,"/demowebexctrl", DemoWebExCtrl.class);


		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo", RestMethod4Pojo.class);
//		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo.nopara", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class,"nopara");
		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo.nopara", RestMethod4Pojo.class);

//		ExtensionWebHelper.addRemoteCallExtension(this, "/testremoteclient", ServiceBean.class);
		ExtensionWebHelper.addRestMethodExtension(this, "/testrestclient", ServiceBean.class);
		
		ExtendsionClientHelper.addClientProxyExtension(this, IService.class,"http://localhost:8080/demo/testrestclient" ,Client.PROTOCOL_REST);
		
		ExtensionWebHelper.addServiceFilterExtension(this, ServiceFilterTest.class);
		ExtensionWebHelper.addWebCtrlFilterExtension(this, WebCtrlFilterTest.class);
		
		ExtensionWebHelper.addServiceExportExtension(this, "/dynamic-method", DynamicMethodTest.class);
		
		ExtensionWebHelper.addWebControllerExtension(this, "/webanno/web",WebControllerTest.class);
		ExtensionWebHelper.addWebExControllerExtension(this, "/webanno/webex", WebExControllerTest.class);
		ExtensionWebHelper.addServiceExportExtension(this, "/webanno/service", ServiceExportTest.class);
		ExtensionCtxHelper.addRuleExtension(this,IRuleTestForAnno.class,RuleTestForAnno.class);
		ExtensionCtxHelper.addRuleExtension(this,"rule1346",IRuleTestForAnno.class,RuleTestForAnno.class);

		ExtensionWebHelper.addServiceExportExtension(this,"/mttestclient",MtTestForRequest.class);

//		ExtensionWebHelper.autoBindControllerExtension(this, ".bind");
//		ExtensionWebHelper.autoBindServiceExportExtension(this, ".bind");
		
		ExtensionWebHelper.addServiceExportExtension(this, "/mapreturn", ExportForMapReturn.class);
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.WEBSERVICE +1;
	}

	public void test() throws IOException, HttpStatusException, InterruptedException, ExecutionException {
		new WebCtrlTestClient().test();
		new WebExCtrlTestClient().test();

		new ExportForMapBeanTest().test();
		
		RestMethod4Pojo.test();
		new TestRestClient().test();
		new TestRestClient().testProxyFactory();
		new TestDefaultValClient().test();
		
//		new TestRemoteClient().test();
		new TestDynamicMethodClient().test();
		new TestPathSearch().test();
		new AnnoTest().test();
		new MtTestClient().test();
//		TestReqIdService.calltest();
		
		new TestBind().test();
	}
	
	@Override
	public void onDestroy() {
		System.out.println("### on destroy");
	}

	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return true;
	}
}
