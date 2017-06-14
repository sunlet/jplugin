package net.jplugin.core.das.api.monitor;

import java.util.HashMap;
import java.util.Map;

public abstract class SqlMonitorListenerContext{
	String sql;
	String dataSourceName;
	private Exception exception;
	Map<String,Object> attributes=null;
	
	public String getSql() {
		return this.sql;
	}

	public void setSql(String s) {
		this.sql = s;
	}

	public void setException(Exception e) {
		this.exception = e;
	}

	public Exception getException() {
		return exception;
	}

//	public static SqlMonitorListenerContext create() {
//		return new SqlMonitorListenerContext();
//	}

	public void setDataSource(String ds) {
		this.dataSourceName = ds;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setAttribute(String key,Object val){
		if (attributes == null){
			attributes = new HashMap<String, Object>();
		}
		attributes.put(key,val);
	}
	
	public String getStringAttribute(String key){
		return (String) attributes.get(key);
	}
	
	public Object getAttribute(String key){
		return attributes==null? null:attributes.get(key);
	}

	public static StatementContext createStatementCtx() {
		return new StatementContext();
	}

	public static ResultSetContext createResultContext() {
		return new ResultSetContext();
	}

}
