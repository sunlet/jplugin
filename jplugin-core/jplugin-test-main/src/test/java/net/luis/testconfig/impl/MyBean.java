package net.luis.testconfig.impl;

import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.kernel.api.BindBean;

@BindBean(id = "mybeanid1")
public class MyBean {
	
	@RefConfig(path = "group1.key1",autoRefresh = true,defaultValue = "default1")
	String cfg1;
	
	@RefConfig(path = "group1.key2",autoRefresh = true,defaultValue = "default1")
	String cfg2;
	
	public String getCfg1() {
		return cfg1;
	}
	
	
}
