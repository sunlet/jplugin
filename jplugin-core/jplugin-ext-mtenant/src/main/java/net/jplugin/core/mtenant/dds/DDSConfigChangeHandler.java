package net.jplugin.core.mtenant.dds;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;

public class DDSConfigChangeHandler implements IConfigChangeHandler{

	private static List<MultiTenantDDSHelper> list=new ArrayList<MultiTenantDDSHelper>();
	static void addTarget(MultiTenantDDSHelper o) {
		list.add(o);
	}
	@Override
	public void onChange(IConfigChangeContext ctx) {
		for (MultiTenantDDSHelper o:list) {
			o.clearCache();
		}
	}
}

