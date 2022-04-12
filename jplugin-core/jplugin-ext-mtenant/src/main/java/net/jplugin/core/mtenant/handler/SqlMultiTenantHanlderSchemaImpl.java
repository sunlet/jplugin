package net.jplugin.core.mtenant.handler;

import java.sql.Connection;
import java.sql.SQLException;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.api.sqlrefactor.ISqlRefactor;
import net.jplugin.core.das.dds.impl.DummyConnection;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;

public class SqlMultiTenantHanlderSchemaImpl implements ISqlRefactor {
	/**
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */
	private static Logger logger = LogFactory.getLogger(SqlMultiTenantHanlderSchemaImpl.class);
	private boolean allDataSource;
	private String[] dataSources = null;
	
//	/**
//	 * 下面配置支持 共用表方案
//	 */
//	private String mergeTableSchema = null;
//	private Set<String> mergeTableTenants= null;
//	private String mergetTableField = null;

	 /* #mtenent.merge-table.schema=uuu
	 * #mtenent.merge-table.tenants=1001,1002,1003
	 */
	
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
		if (isRouter) 
			throw new RuntimeException("Router connection can't be configed with multinant."+conn.getClass().getName());
		
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
		String schema = schemaPrefix + "_"+ tid;
		return MultiDbSqlHelper.handle(sql, schema);
//		try {
////			setSchema(conn,schema);
////			conn.setSchema(schema);
////			return sql+ ("/*schema="+schema+"*/");
//		} catch (Exception e) {
//			throw new RuntimeException("The multi tenant datasource ["+dataSourceName+"] set schema to ["+schema+"] error!",e);
//		}
		
	}

	private void setSchema(Connection conn, String schema) throws SQLException {
//		while(conn.isWrapperFor(Connection.class)){
//			conn = conn.unwrap(Connection.class);
//		}
		conn.setSchema(schema);
	}

	private boolean inDataSourceList(String dataSourceName) {
		for (String s:this.dataSources){
			if (s.equals(dataSourceName)) return true;
		}
		return false;
	}


}
