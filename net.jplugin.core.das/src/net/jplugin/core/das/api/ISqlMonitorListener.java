package net.jplugin.core.das.api;

public interface ISqlMonitorListener {
	public  boolean beforeExecute(SqlMonitorListenerContext ctx);
	public  boolean beforeNext(SqlMonitorListenerContext ctx);
	
	public  void afterExecute(SqlMonitorListenerContext ctx);
	public  void afterNext(SqlMonitorListenerContext ctx);
}
