package net.luis.testautosearch.extensionid;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.IStartup;

public class ExtensionIdTest {

	public static void assertExceptionInCreateService() {
		AssertKit.assertException(()->{
			Beans.get("WebControllerTest");
		});
	}
	public static void test() {
		Object ext = Beans.get("theidabcde");
		AssertKit.assertNotNull(ext, "ext");
		ext = Beans.get("theidabcde",ExportTest1.class);
		((ExportTest1)ext).a();
		
		
		Object ext2 = Beans.get("theidabcde");
		AssertKit.assertNotNull(ext2, "ext2");
		ext = Beans.get("theExportTest2",ExportTest2.class);
		((ExportTest2)ext).a();
		
		Object o = Beans.get("WebControllerTest");
		AssertKit.assertNotNull(o, "o");
		((WebControllerTest)o).aaa(null,null);
		
		o = Beans.get("WebExControllerTest");
		AssertKit.assertNotNull(o, "o");
		((WebExControllerTest)o).aa();
		
		
		IService1ForId svc = Beans.get("IService1ForId",IService1ForId.class);
		svc.a();
		
		IRuleServiceForId rs = Beans.get("IRuleServiceForId",IRuleServiceForId.class);
		rs.a();
		
		IStartup bs = Beans.get("BindStartUpForId",IStartup.class);
		AssertKit.assertNotNull(bs, "startup");
		
		
		IExtensionForIdTest efit = Beans.get("ExtensionForIdTest",IExtensionForIdTest.class);
		AssertKit.assertNotNull(efit, "efit");
		efit.aaa();
		
		
		RuleMethodInterceptorForIdTest rmift = Beans.get("RuleMethodInterceptorForIdTest1",RuleMethodInterceptorForIdTest.class);
		AssertKit.assertNotNull(rmift, "rmift");
		rmift.aaa();
		
		RuleMethodInterceptorForIdTest rmift2 = Beans.get("RuleMethodInterceptorForIdTest2",RuleMethodInterceptorForIdTest.class);
		AssertKit.assertTrue(rmift==rmift2);
		
	}

}
