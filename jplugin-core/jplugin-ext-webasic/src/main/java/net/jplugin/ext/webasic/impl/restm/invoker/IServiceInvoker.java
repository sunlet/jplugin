package net.jplugin.ext.webasic.impl.restm.invoker;

import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper;

public interface IServiceInvoker {
	public void call(CallParam cp)throws Throwable;
	public ObjectCallHelper getObjectCallHelper();
}
