package net.jplugin.core.mtenant.api;

/**
 * <BR>这个接口被调用的频次比较高，请注意做缓存等策略。
 * <BR>因为策略不会修改，所以启动的时候加载所有的，中间找不到的时候再增量获取一下。
 * @author LiuHang
 */
public interface ITenantStoreStrategyProvidor {
	public Strategy getTenantStrategy(String tid);
	
	enum Mode{SHARE,ONESELF}
	
	public static class Strategy{
		String finalDataSource;//暂时不用
		String finalSchema;
		Mode mode;
		
		public String getFinalDataSource() {
			return finalDataSource;
		}
		public void setFinalDataSource(String finalDataSource) {
			this.finalDataSource = finalDataSource;
		}
		public String getFinalSchema() {
			return finalSchema;
		}
		public void setFinalSchema(String finalSchema) {
			this.finalSchema = finalSchema;
		}
		public Mode getMode() {
			return mode;
		}
		public void setMode(Mode mode) {
			this.mode = mode;
		}
		
	}
}

