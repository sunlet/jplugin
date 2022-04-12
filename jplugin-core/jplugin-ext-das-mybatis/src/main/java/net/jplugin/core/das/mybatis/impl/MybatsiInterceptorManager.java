package net.jplugin.core.das.mybatis.impl;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class MybatsiInterceptorManager {

	public static MybatsiInterceptorManager instance = new MybatsiInterceptorManager();
	Interceptor[] inceptList = null;

	public void init(){
		 inceptList = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.das.mybatis.Plugin.EP_MYBATIS_INCEPT, Interceptor.class);
	}
	
	private MybatsiInterceptorManager(){
	}
	
	public void installPlugins(Configuration c){
		for (Interceptor i:inceptList){
			c.addInterceptor(i);
		}
	}

}
