package net.luis.testautosearch.extensionid;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.ExtensionObjects;
import net.jplugin.core.kernel.api.IStartup;

public class ExtensionIdTest {

	public static void assertExceptionInCreateService() {
		AssertKit.assertException(()->{
			ExtensionObjects.get("WebControllerTest");
		});
	}
	public static void test() {
		Object ext = ExtensionObjects.get("theidabcde");
		AssertKit.assertNotNull(ext, "ext");
		ext = ExtensionObjects.get("theidabcde",ExportTest1.class);
		((ExportTest1)ext).a();
		
		
		Object ext2 = ExtensionObjects.get("theidabcde");
		AssertKit.assertNotNull(ext2, "ext2");
		ext = ExtensionObjects.get("theExportTest2",ExportTest2.class);
		((ExportTest2)ext).a();
		
		Object o = ExtensionObjects.get("WebControllerTest");
		AssertKit.assertNotNull(o, "o");
		((WebControllerTest)o).aaa(null,null);
		
		o = ExtensionObjects.get("WebExControllerTest");
		AssertKit.assertNotNull(o, "o");
		((WebExControllerTest)o).aa();
		
		
		IService1ForId svc = ExtensionObjects.get("IService1ForId",IService1ForId.class);
		svc.a();
		
		IRuleServiceForId rs = ExtensionObjects.get("IRuleServiceForId",IRuleServiceForId.class);
		rs.a();
		
		IStartup bs = ExtensionObjects.get("BindStartUpForId",IStartup.class);
		AssertKit.assertNotNull(bs, "startup");
		
		
		IExtensionForIdTest efit = ExtensionObjects.get("ExtensionForIdTest",IExtensionForIdTest.class);
		AssertKit.assertNotNull(efit, "efit");
		efit.aaa();
		
		
		RuleMethodInterceptorForIdTest rmift = ExtensionObjects.get("RuleMethodInterceptorForIdTest1",RuleMethodInterceptorForIdTest.class);
		AssertKit.assertNotNull(rmift, "rmift");
		rmift.aaa();
		
//		RuleMethodInterceptorForIdTest rmift2 = Beans.get("RuleMethodInterceptorForIdTest2",RuleMethodInterceptorForIdTest.class);
//		AssertKit.assertTrue(rmift==rmift2);
		
	}

}
