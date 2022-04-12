package net.jplugin.core.das.dds;

import javax.sql.DataSource;

import net.jplugin.core.das.ExtensionDasHelper;
import net.jplugin.core.das.dds.dsnamed.DsNamedDataSource;
import net.jplugin.core.das.dds.select.SelectRouterDataSource;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;


public class Plugin extends AbstractPlugin {

	
	
	public Plugin() {
		ExtensionDasHelper.addDynamisDataSourceTypeExtension(this,"ds-named-ds",DsNamedDataSource.class);
		ExtensionDasHelper.addDynamisDataSourceTypeExtension(this,"select-ds",SelectRouterDataSource.class);
	}
	@Override
	public void init() {

	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_DDS;
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
