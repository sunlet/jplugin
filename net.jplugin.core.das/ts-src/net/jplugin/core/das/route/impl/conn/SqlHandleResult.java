package net.jplugin.core.das.route.impl.conn;

public class SqlHandleResult {
//	boolean success;
	String targetDataSourceName;
	String resultSql;
//	String message;
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("sql:").append(resultSql).append(" targetDataSourceName:").append(targetDataSourceName);
		return sb.toString();
	}

//	public boolean isSuccess() {
//		return success;
//	}
//
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}

	public String getTargetDataSourceName() {
		return targetDataSourceName;
	}

	public void setTargetDataSourceName(String targetDataSourceName) {
		this.targetDataSourceName = targetDataSourceName;
	}

	public String getResultSql() {
		return resultSql;
	}

	public void setResultSql(String resultSql) {
		this.resultSql = resultSql;
	}

//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//	
	

}
