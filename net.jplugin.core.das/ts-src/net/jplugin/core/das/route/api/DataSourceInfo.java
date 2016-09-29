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
}