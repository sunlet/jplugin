package net.jplugin.ext.webasic.impl.filter.service;

import java.util.ArrayList;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.impl.filter.MethodFilterManager;

public class ServiceFilterManager extends MethodFilterManager {
	public static ServiceFilterManager INSTANCE  = new ServiceFilterManager();
	
	@Override
	public void init() {
		IInvocationFilter[] arr = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_SERVICEFILTER,IInvocationFilter.class);
		filters = new ArrayList<IInvocationFilter>(arr.length);
		for (int i=0;i<arr.length;i++){
			filters.add(arr[i]);
			PluginEnvirement.getInstance().resolveRefAnnotation(arr[i]);
		}
	}

}
