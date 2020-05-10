package net.jplugin.core.das.dds.api;

import java.util.Map;

public abstract class AbstractRouterDataSource {
	public abstract void init(Map<String,String> config);
	
	/**
	 * <pre>
	 * 如果这里返回true，则后面会在有Sql时调用getTargetResult。
	 * 否则在获取Connection时调用getTargetDataSource。
	 * 后者速度更快，成本更低.
	 * </pre>
	 * @return
	 */
	public abstract boolean resolveWithSql();
	
	/**
	 * 在获取Connection时调用，需要配合resolveWithSql返回false
	 * @return
	 */
	public abstract String getTargetDataSource();
	
	/**
	 * 在有sql时调用，需要配合resolveWithSql返回true
	 * @param sql
	 * @return
	 */
	public abstract Result getTargetResult(String sql);
	
	public static class Result{
		String targetDs;
		String targetSql;
		public String getTargetDs() {
			return targetDs;
		}
		public void setTargetDs(String targetDs) {
			this.targetDs = targetDs;
		}
		public String getTargetSql() {
			return targetSql;
		}
		public void setTargetSql(String targetSql) {
			this.targetSql = targetSql;
		}
		
	}
}
