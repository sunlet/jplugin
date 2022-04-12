package net.luis.main;

import net.jplugin.core.rclient.api.ClientCallContext;
import net.jplugin.core.rclient.api.IClientFilter;

public class TestClientFilter implements IClientFilter{

	public void filterStart(ClientCallContext ctx) {
		System.out.println("@@@@@@@@client filter startr:"+ctx.getMethod().getName());
	}

	public void filterEnd(ClientCallContext ctx) {
		System.out.println("@@@@@@@@client filter end:"+ctx.getMethod().getName()+" dural="+ctx.getDural()+ " ret = "+ctx.getReturnObject());
	}

}
