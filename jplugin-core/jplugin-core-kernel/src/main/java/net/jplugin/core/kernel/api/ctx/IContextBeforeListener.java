package net.jplugin.core.kernel.api.ctx;

public interface IContextBeforeListener {
	public void beforeRelease(ThreadLocalContext ctx);
}
