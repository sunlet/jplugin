package net.jplugin.core.das.route;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.impl.TxManagedDataSource;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.algms.DateAlgm;
import net.jplugin.core.das.route.impl.algms.HashAlgm;
import net.jplugin.core.das.route.impl.algms.WightHashAlgm;
import net.jplugin.core.das.route.impl.algms.MonthAlgm;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;

public class Plugin extends AbstractPlugin {
	public static String EP_TS_ALGM="EP_TS_ALGM";
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_TS_ALGM, ITsAlgorithm.class,true));
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"hash", HashAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"weightHash", HashAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"date", DateAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"month", MonthAlgm.class);
	}
	@Override
	public void init() {
		TsAlgmManager.init();
		
		for (String s:DataSourceFactory.getDataSourceNames()){
			DataSource ds = DataSourceFactory.getDataSource(s);
			if (ds instanceof TxManagedDataSource){
				ds = ((TxManagedDataSource)ds).getInner();
				if (ds instanceof RouterDataSource){
					((RouterDataSource)ds).getConfig().valid();
				}
			}
		}
		
//		DataSourceFactory.g
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_TS;
	}

}
