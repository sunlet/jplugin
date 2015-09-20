package test.net.jplugin.ext.webasic;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import test.net.jplugin.ext.webasic.restclient.RestServiceBean;
import test.net.jplugin.ext.webasic.restclient.TestRestClient;
import test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午04:57:02
 **/

public class Plugin extends AbstractPluginForTest{
	public Plugin(){
		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class);
		ExtensionWebHelper.addRestMethodExtension(this, "/testremotepojo.nopara", test.net.jplugin.ext.webasic.restmethod.RestMethod4Pojo.class,"nopara");
		ExtensionWebHelper.addRestMethodExtension(this, "/testrestclient", RestServiceBean.class);
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.WEBSERVICE +1;
	}

	public void test() {
		RestMethod4Pojo.test();
		new TestRestClient().test();
	}
}
