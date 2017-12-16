package net.jplugin.core.das.route.api;

public interface ITsAlgorithm {
	
	/**
	 * Get the corresponded datasource and tablename for the given key. 
	 * @param compondDataSource
	 * @param tableBaseName
	 * @param vt
	 * @param key
	 * @return
	 */
	public Result getResult(RouterDataSource compondDataSource,String tableBaseName,ValueType vt,Object key);
	
	/**
	 * The method only need be implemented when you want use Span Table Query.
	 * @param dataSource
	 * @param tableName
	 * @param type 
	 * @param kva 
	 * @return
	 */
	public DataSourceInfo[] getMultiResults(RouterDataSource dataSource, String tableName, ValueType type, KeyValueForAlgm kva);
	
	public enum ValueType{
		LONG,STRING,DATE, TIMESTAMP
	}
	
	public static class Result{
		String dataSource;
		String tableName;
		public static Result create(){
			return new Result();
		}
		private Result(){}
		public String getDataSource() {
			return dataSource;
		}
		public void setDataSource(String dataSource) {
			this.dataSource = dataSource;
		}
		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
	}


}
