package net.luis.testautosearch.extensionid;

import net.jplugin.core.kernel.api.BindBean;

@BindBean(id = "mybean1")
public class BeanTest {

	String name() {
		return "hahaha";
	}
}
