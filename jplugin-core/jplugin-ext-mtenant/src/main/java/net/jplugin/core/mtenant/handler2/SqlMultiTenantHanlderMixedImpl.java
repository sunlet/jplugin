package net.jplugin.core.mtenant.handler2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.das.dds.impl.DummyConnection;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor.Mode;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor.Strategy;
import net.sf.jsqlparser.JSQLParserException;

public class SqlMultiTenantHanlderMixedImpl implements ISqlRefactor {
	/**
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */
	private static Logger logger = LogFactory.getLogger(SqlMultiTenantHanlderMixedImpl.class);
	private boolean allDataSource;
	private String[] dataSources = null;
	
	boolean init;
	public void init(){
		if (!init){
			init = true;

			String datasource = ConfigFactory.getStringConfig("mtenant.datasource", "ALL");
			if ("ALL".equals(datasource)){
				allDataSource = true;
			}else{
				allDataSource = false;	
				dataSources = StringKit.splitStr(datasource, ",");
			}
		}
	}
	


//	@Override
	public  String refactSql(String dataSourceName, String sql,Connection conn) {
		init();
		String result = handleInner(dataSourceName, sql,conn);
		if (logger.isDebugEnabled()){
			if (!sql.equals(result)){
				logger.debug("BeforeSQL = "+sql);
				logger.debug("After SQL = "+result);
			}
		}
		return result;
	}
	public  String handleInner(String dataSourceName, String sql,Connection conn) {
		if (!this.allDataSource && !inDataSourceList(dataSourceName))
			return sql;
		
		//router connection数据源不能配置为多租户
		boolean isRouter =false;
		try{
			isRouter = conn.isWrapperFor(DummyConnection.class);
		}catch(Exception e){
			throw new RuntimeException("Error while call isWrapper",e);
		}
		if (isRouter) {
			handleRouterConnectionConfigured(dataSourceName);
//			throw new RuntimeException("Router connection can't be configed with multinant."+conn.getClass().getName());
			return sql;
		}
		
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		
		//在列表中，必须能够处理
		String schemaPrefix = ConfigFactory.getStringConfig("mtenant.schema-prefix."+dataSourceName);
		if (StringKit.isNull(schemaPrefix)){
			throw new RuntimeException("The multi tenant datasource ["+dataSourceName+"] must be configed with a [schema-prefix."+dataSourceName+"] key");
		}
		
		if (StringKit.isNull(tid)){
			throw new RuntimeException("The multi tenant datasource ["+dataSourceName+"] must be called with a tenantid request attribute");
		}
		
		//重写sql
		return handle(sql, dataSourceName,schemaPrefix, tid);
	}

	
	boolean firstTimeMatchRouteConnection = true;
	private void handleRouterConnectionConfigured(String dataSourceName) {
		if (firstTimeMatchRouteConnection){
			firstTimeMatchRouteConnection = false;
			PluginEnvirement.getInstance().getStartLogger().log("$$$ Router connection is configured for mtenant, noticed. "+dataSourceName);
		}
	}

	private boolean inDataSourceList(String dataSourceName) {
		for (String s:this.dataSources){
			if (s.equals(dataSourceName)) return true;
		}
		return false;
	}
	

	private static String handle(String sql, String dataSourceName, String schemaPrefix,String tid) {
		Strategy stg = getStrategy(sql, dataSourceName);
		
		SqlHandlerVisitorForMixed v;
//		String finalSchema = schemaPrefix +"_"+stg.getSchemaPostfix();
		String finalSchema = makeFinalSchema(schemaPrefix ,stg.getSchemaPostfix());
		if (stg.getMode()==Mode.SHARE){
			v = new SqlHandlerVisitorForMixed(finalSchema, tid);
		}else{
			v = new SqlHandlerVisitorForMixed(finalSchema);
		}
		
		try {
			return v.handle(sql);
		} catch (JSQLParserException e) {
			throw new RuntimeException("SQL gremma error."+e.getMessage() +"  "+sql,e);
		}
	}

	private static String makeFinalSchema(String schemaPrefix, String schemaPostfix) {
		if (Strategy.NO_POST_PREFIX.equals(schemaPostfix))
			return schemaPrefix;
		else
			return schemaPrefix +"_"+schemaPostfix;
	}

	private static Strategy getStrategy(String sql, String dataSource) {
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (StringKit.isNull(tid))
			throw new RuntimeException("Can't find tenantid when handle sql");
		
		Strategy stragegy=null;
		if (TenantStoreStrategyManager.instance.isProviderExist()){
			stragegy = TenantStoreStrategyManager.instance.getStragegy(tid,dataSource);
			if (stragegy==null)
				throw new RuntimeException("Can't get tenant store stragegy for tenent:"+tid);
		}else{
			stragegy = getDefaultStrategy(sql,dataSource,tid);
		}
		return stragegy;
	}

	private static Strategy getDefaultStrategy(String sql, String dataSource, String tid) {
		Strategy s = new Strategy();
		s.setMode(Mode.ONESELF);
		s.setSchemaPostfix(tid);
		return s;
	}

}
