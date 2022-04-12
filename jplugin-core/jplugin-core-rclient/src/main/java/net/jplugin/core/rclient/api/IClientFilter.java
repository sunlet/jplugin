package net.jplugin.core.rclient.api;

public interface IClientFilter {

	void filterStart(ClientCallContext ctx);

	void filterEnd(ClientCallContext ctx);
	
}
