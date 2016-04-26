package net.jplugin.ext.webasic.impl.filter.webctrl;

import java.util.ArrayList;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IMethodFilter;
import net.jplugin.ext.webasic.impl.filter.MethodFilterManager;

public class WebCtrlFilterManager extends MethodFilterManager {
	public static WebCtrlFilterManager INSTANCE  = new WebCtrlFilterManager();
	
	@Override
	public void init() {
		IMethodFilter[] arr = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_WEBCTRLFILTER,IMethodFilter.class);
		filters = new ArrayList<IMethodFilter>(arr.length);
		for (int i=0;i<arr.length;i++){
			filters.add(arr[i]);
		}
	}

}
