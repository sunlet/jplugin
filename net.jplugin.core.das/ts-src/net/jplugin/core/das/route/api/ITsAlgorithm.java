package net.jplugin.core.das.route.api;

public interface ITsAlgorithm {
	
	public Result getResult(RouterDataSource compondDataSource,String tableBaseName,Object key);
	
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
