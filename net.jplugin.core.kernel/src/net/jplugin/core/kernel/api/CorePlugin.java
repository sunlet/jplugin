package net.jplugin.core.kernel.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CorePlugin {

	public synchronized static Collection<? extends Object> get() {
		HashSet ret = new HashSet<String>();
		// core
		addIfExists(ret,"net.jplugin.core.ctx.Plugin");
		addIfExists(ret,"net.jplugin.core.das.Plugin");
		addIfExists(ret,"net.jplugin.core.das.route.Plugin");
		addIfExists(ret,"net.jplugin.core.event.Plugin");
		addIfExists(ret,"net.jplugin.core.job.Plugin");
		addIfExists(ret,"net.jplugin.core.kernel.Plugin");
		addIfExists(ret,"net.jplugin.core.lock.Plugin");
		addIfExists(ret,"net.jplugin.core.log.Plugin");
		addIfExists(ret,"net.jplugin.core.rclient.Plugin");
		addIfExists(ret,"net.jplugin.core.scheduler.Plugin");
		addIfExists(ret,"net.jplugin.core.service.Plugin");
		addIfExists(ret,"net.jplugin.core.das.hib.Plugin");
		addIfExists(ret,"net.jplugin.core.das.mybatis.Plugin");
		addIfExists(ret,"net.jplugin.core.config.Plugin");

		//web
		addIfExists(ret,"net.jplugin.ext.webasic.Plugin");
		addIfExists(ret,"net.jplugin.ext.token.Plugin");
		addIfExists(ret,"net.jplugin.ext.dict.Plugin");
		addIfExists(ret,"net.jplugin.core.mtenant.Plugin");
		addIfExists(ret,"net.jplugin.ext.staticweb.Plugin");
		//file
		addIfExists(ret,"net.jplugin.ext.filesvr.Plugin");

		// return
		return ret;
	}

	private static void addIfExists(Set<String> ret,String string) {
		try{
			Class.forName(string);
			ret.add(string);
		}catch(Exception e){
		}
		
	}

}
