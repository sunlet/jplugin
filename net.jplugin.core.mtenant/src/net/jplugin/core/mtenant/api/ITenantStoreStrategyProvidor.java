package net.jplugin.core.mtenant.api;

public interface ITenantStoreStrategyProvidor {
	public Strategy getTenantStrategy(String tid);
	
	enum Mode{SHARE,ONESELF}
	
	public static class Strategy{
		String dataSoruce;
		String schema;
		Mode mode;
		public String getDataSoruce() {
			return dataSoruce;
		}
		public void setDataSoruce(String dataSoruce) {
			this.dataSoruce = dataSoruce;
		}
		public String getSchema() {
			return schema;
		}
		public void setSchema(String schema) {
			this.schema = schema;
		}
		public Mode getMode() {
			return mode;
		}
		public void setMode(Mode mode) {
			this.mode = mode;
		}
		
	}
}

