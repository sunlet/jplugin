package net.jplugin.ext.webasic.impl;

import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.impl.WebDriver.ControllerMeta;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper.ObjectAndMethod;
import net.jplugin.ext.webasic.impl.restm.RestMethodControllerSet4Invoker;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;
import net.jplugin.ext.webasic.impl.rmethod.RmethodController;
import net.jplugin.ext.webasic.impl.rmethod.RmethodControllerSet;

public class ESFHelper {
	/**
	 * 根据URI获取到对应的JavaBean
	 * @param cm
	 * @param arg
	 * @return
	 */
	public static Object getObject(String uri) {
		ControllerMeta cm = WebDriver.INSTANCE.parseControllerMeta(uri);
		
		IControllerSet cs = cm.getControllerSet();
		if (cs instanceof RestMethodControllerSet4Invoker) {
			IServiceInvoker si = ServiceInvokerSet.instance.getServiceInvoker(cm.getServicePath());
			return si.getObjectCallHelper().getObject();
		} else if (cs instanceof RmethodControllerSet) {
			RmethodControllerSet rcs = (RmethodControllerSet) cm.getControllerSet();
			RmethodController rc = rcs.getRMethodController(cm.getServicePath());
			return rc.getObjectCallHelper().getObject();
		} else
			return null;
	}
}
