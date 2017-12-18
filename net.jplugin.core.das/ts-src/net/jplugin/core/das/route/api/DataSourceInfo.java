package net.jplugin.core.das.route.api;

public  class DataSourceInfo {
	private String dsName;
	private String[] destTbs;
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public String[] getDestTbs() {
		return destTbs;
	}
	public void setDestTbs(String[] destTbs) {
		this.destTbs = destTbs;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(dsName);
		sb.append("---");
		for (String tb:destTbs){
			sb.append(" , ");
			sb.append(tb);
		}
		return sb.toString();
	}
	
	public static DataSourceInfo build(String ds,String... tbs){
		DataSourceInfo inst = new DataSourceInfo();
		inst.dsName = ds;
		inst.destTbs = tbs;
		return inst;
	}
}