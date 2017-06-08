package net.jplugin.core.das.api;

public class SqlMonitorListenerContext{
	String sql;
	String dataSourceName;
	private Exception exception;
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

	public static SqlMonitorListenerContext create() {
		return new SqlMonitorListenerContext();
	}

	public void setDataSource(String ds) {
		this.dataSourceName = ds;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}


}
