package test.net.jplugin.ext.webasic;
import java.io.IOException;

import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.rclient.ExtendsionClientHelper;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import test.net.jplugin.ext.webasic.restclient.IService;
import test.net.jplugin.ext.webasic.restclient.ServiceBean;
import test.net.jplugin.ext.webasic.restclient.ServiceFilterTest;
import test.net.jplugin.ext.webasic.restclient.TestDefaultValClient;
import test.net.jplugin.ext.webasic.restclient.TestRemoteClient;
import test.net.jplugin.ext.webasic.restclient.TestRestClient;
import test.net.jplugin.ext.webasic.restclient.WebCtrlFilterTest;
import test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午04:57:02
 **/

public class Plugin extends AbstractPluginForTest{

	public Plugin(){
		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class);
//		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo.nopara", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class,"nopara");
		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo.nopara", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class);

		ExtensionWebHelper.addRemoteCallExtension(this, "/testremoteclient", ServiceBean.class);
		ExtensionWebHelper.addRestMethodExtension(this, "/testrestclient", ServiceBean.class);
		
		ExtendsionClientHelper.addClientProxyExtension(this, IService.class,"http://localhost:8080/demo/testrestclient" ,Client.PROTOCOL_REST);
		
		ExtensionWebHelper.addServiceFilterExtension(this, ServiceFilterTest.class);
		ExtensionWebHelper.addWebCtrlFilterExtension(this, WebCtrlFilterTest.class);
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.WEBSERVICE +1;
	}

	public void test() throws IOException, HttpStatusException {
		RestMethod4Pojo.test();
		new TestRestClient().test();
		new TestRestClient().testProxyFactory();
		new TestDefaultValClient().test();
		
		new TestRemoteClient().test();
	}
}
