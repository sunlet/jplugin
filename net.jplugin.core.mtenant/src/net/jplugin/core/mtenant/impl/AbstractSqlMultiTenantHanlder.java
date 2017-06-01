package net.jplugin.core.mtenant.impl;

import java.sql.Connection;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.mtenant.handler.SqlMultiTenantHanlderSchemaImpl;
import net.jplugin.mtenant.impl.kit.SqlMultiTenantHanlderMergeImpl;

public abstract class AbstractSqlMultiTenantHanlder {
	static AbstractSqlMultiTenantHanlder instance;
	
	public static String handleSql(String dataSourceName, String sql,Connection conn) {
		return instance.handle(dataSourceName, sql,conn);
	}
	
	public static void initInstance(){
		String s = ConfigFactory.getStringConfig("mtenant.db.strategy");
		if (s==null){
//			s = "schema";
			s = "merge";
		}
		if ("schema".equals(s)){
			instance = new SqlMultiTenantHanlderSchemaImpl();
		}else{
			instance = new SqlMultiTenantHanlderMergeImpl();
		}
		PluginEnvirement.getInstance().getStartLogger().log("多租户数据库策略:"+s+"  "+instance.getClass().getName());

	};
	
	/**
	 * 真正实现handle的方法
	 * @param dataSourceName
	 * @param sql
	 * @return
	 */
	public abstract String handle(String dataSourceName, String sql,Connection conn);
}
