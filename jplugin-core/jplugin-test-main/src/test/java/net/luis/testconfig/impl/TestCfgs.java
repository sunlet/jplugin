package net.luis.testconfig.impl;

import java.util.ArrayList;

import net.jplugin.core.config.api.ConfigChangeManager;
import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.BindBean;

@BindBean(id = "testconfigbean")
public class TestCfgs {

	public void test() {
		System.out.println(Beans.get("mybeanid1",MyBean.class).getCfg1());
		
		ArrayList list = new ArrayList();
		list.add("group1.key1");
		net.jplugin.core.config.impl.ConfigureChangeManager.instance.fireConfigChange(list);
		
		System.out.println(Beans.get("mybeanid1",MyBean.class).getCfg1());
	}
}
