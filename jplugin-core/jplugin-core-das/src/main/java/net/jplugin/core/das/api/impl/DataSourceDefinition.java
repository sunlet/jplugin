package net.jplugin.core.das.api.impl;

public class DataSourceDefinition {
	String configGroupName;
	boolean managed;
	public String getConfigGroupName() {
		return configGroupName;
	}
	public void setConfigGroupName(String configGroupName) {
		this.configGroupName = configGroupName;
	}
	public boolean getManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	
}
