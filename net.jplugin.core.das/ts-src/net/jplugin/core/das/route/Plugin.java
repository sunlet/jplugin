package net.jplugin.core.das.route;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.impl.TxManagedDataSource;
import net.jplugin.core.das.route.api.IAggregationFunctionHandler;
import net.jplugin.core.das.route.api.IFunctionHandler;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.function.FunctionHandlerManager;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.algms.DateAlgm;
import net.jplugin.core.das.route.impl.algms.HashAlgm;
import net.jplugin.core.das.route.impl.algms.WightHashAlgm;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.CountStarWrapperController;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.GroupByWrapperController;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.LimitWrapperController;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperController;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperManager;
import net.jplugin.core.das.route.impl.algms.MonthAlgm;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;

public class Plugin extends AbstractPlugin {
	public static final String EP_MULQRY_RS_WRAPCTRL = "EP_MULQRY_RS_WRAPCTRL";
	public static String EP_TS_ALGM="EP_TS_ALGM";
	
	public static final String EP_SQL_FUNCTION = "EP_SQL_FUNCTION";
	public static final String EP_SQL_AGG_FUNCTION = "EP_SQL_AGG_FUNCTION";
	
	public Plugin(){
		this.addExtensionPoint(ExtensionPoint.create(EP_TS_ALGM, ITsAlgorithm.class,true));
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"hash", HashAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"weightHash", HashAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"date", DateAlgm.class);
		ExtensionDasRouteHelper.addRouteAlgmExtension(this,"month", MonthAlgm.class);
		
		this.addExtensionPoint(ExtensionPoint.create(EP_MULQRY_RS_WRAPCTRL, WrapperController.class));
		ExtensionDasRouteHelper.addMulQryRsWrapperControllerExtension(this, CountStarWrapperController.class);
		ExtensionDasRouteHelper.addMulQryRsWrapperControllerExtension(this, GroupByWrapperController.class);
		ExtensionDasRouteHelper.addMulQryRsWrapperControllerExtension(this, LimitWrapperController.class);

		this.addExtensionPoint(ExtensionPoint.create(EP_SQL_FUNCTION, IFunctionHandler.class,true));
		this.addExtensionPoint(ExtensionPoint.create(EP_SQL_AGG_FUNCTION, IAggregationFunctionHandler.class,true));

	}
	@Override
	public void onCreateServices() {
		TsAlgmManager.init();
		WrapperManager.INSTANCE.init();
		FunctionHandlerManager.INSTANCE.init();
		
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
	@Override
	public void init() {
	}

}
