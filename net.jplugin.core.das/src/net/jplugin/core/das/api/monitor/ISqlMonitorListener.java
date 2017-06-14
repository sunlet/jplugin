package net.jplugin.core.das.api.monitor;

public interface ISqlMonitorListener {
	public  boolean beforeExecute(StatementContext ctx);
	public  boolean beforeNext(ResultSetContext ctx);
	
	public  void afterExecute(StatementContext ctx);
	public  void afterNext(ResultSetContext ctx);
}
