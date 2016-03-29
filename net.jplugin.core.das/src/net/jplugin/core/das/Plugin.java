package net.jplugin.core.das;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;

public class Plugin extends AbstractPlugin {
	public static final String EP_DBSPLIT_ALG = "EP_DBSPLIT_ALG";
	public static final String EP_DATASOURCE = "EP_DATASOURCE";
	public static final String EP_UM_DATASOURCE = "EP_UM_DATASOURCE";

	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_DATASOURCE, DataSourceDefinition.class,true));
	}


	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS;
	}
	
	public void init() {
		DataSourceFactory.init();
	}

}
