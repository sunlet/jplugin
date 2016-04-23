package net.jplugin.core.das.mybatis.api;

public class ExtensionDefinition4Mapping {
	String dataSource;
	String interfOrResource;
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSourceName) {
		this.dataSource = dataSourceName;
	}
	public String getInterfOrResource() {
		return interfOrResource;
	}
	public void setInterfOrResource(String interfOrResource) {
		this.interfOrResource = interfOrResource;
	}
}
