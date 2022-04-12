package net.luis.testautosearch.extensionid;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.kernel.api.RefBean;

public class BeanTestTest extends RefAnnotationSupport{

	@RefBean(id="mybean1")
	Object bean1;
	
	@RefBean(id="mybean1")
	BeanTest bean2;
	public void test() {
		AssertKit.assertEqual(bean2.name(), "hahaha");
		AssertKit.assertEqual(bean1.getClass(),BeanTest.class);
		AssertKit.assertTrue(bean1==bean2);
	}
}
