package net.jplugin.core.ctx.impl;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.ctx.Plugin;
import net.jplugin.core.ctx.api.ITransactionManagerListener;

public class TxMgrListenerManager {

	private static ITransactionManagerListener[] listeners;

	public static void init(){
		listeners = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_TXMGR_LISTENER,ITransactionManagerListener.class);
	}
	
	public static void beforeBegin(){
		for ( ITransactionManagerListener l:listeners){
			l.beforeBegin();
		}
	}
	public static void afterBegin(){
		for ( ITransactionManagerListener l:listeners){
			l.afterBegin();
		}
	}
	public static void beforeCommit(){
		for ( ITransactionManagerListener l:listeners){
			l.beforeCommit();
		}
	}
	public static void afterCommit(boolean success){
		for ( ITransactionManagerListener l:listeners){
			l.afterCommit(success);
		}
	}

	public static void beforeRollback() {
		for ( ITransactionManagerListener l:listeners){
			l.beforeRollback();
		}
	}
}
