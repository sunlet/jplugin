package net.jplugin.ext.webasic.impl.filter.webctrl;

import java.util.ArrayList;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.webasic.Plugin;
import net.jplugin.ext.webasic.api.IInvocationFilter;
import net.jplugin.ext.webasic.impl.filter.MethodFilterManager;

public class WebCtrlFilterManager extends MethodFilterManager {
	public static WebCtrlFilterManager INSTANCE  = new WebCtrlFilterManager();
	
	@Override
	public void init() {
		IInvocationFilter[] arr = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_WEBCTRLFILTER,IInvocationFilter.class);
		filters = new ArrayList<IInvocationFilter>(arr.length);
		for (int i=0;i<arr.length;i++){
			filters.add(arr[i]);
		}
	}

}
