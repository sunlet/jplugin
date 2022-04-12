package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.dds.api.IRouterDataSource;
import net.jplugin.core.das.dds.api.TablesplitException;
import net.jplugin.core.das.dds.impl.kits.SchemaCheckKit;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig;
//import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SchemaCheckUtil {
//	private static final String E_M_P_T_Y = "E_M_P_T_Y";
	/**
	 * 为了提高速度，去除的同时取出，然后验证
	 * @param conn
	 * @param stmt
	 */
	public static void checkAndRemoveSchema(IRouterDataSource routeDs, Statement stmt,String sqlToLog) {
		Set<String> schemas = SchemaCheckKit.extractAndRemoveSchema(stmt);
		if (!checkSchema(schemas,routeDs)){
			throw new TablesplitException("schema check error. "+sqlToLog);
		}
	}

	/**
	 * 由于Shcmne在一个句子里面很少，这些写没有问题
	 */
	private static boolean checkSchema(Set<String> schemas, IRouterDataSource routeDs) {
		RouterDataSourceConfig dscfg = ((RouterDataSource)routeDs).getConfig();
		for (String s:schemas){
			if (SchemaCheckKit.E_M_P_T_Y.equals(s)){
				//如果是空，则判断是否允许空，不允许则退出
				if (!dscfg.isAllowNoSchema()){
					return false;
				}
			}else{
				//不是空，则判断是否允许该schema 或者允许 所有，不允许则退出
				Set<String> allowed = dscfg.getAllowedSchemas();
				if (!(allowed.contains(s) || allowed.contains("*"))){
					return false;
				}
			}
		}
		//未退出，返回true
		return true;
	}


}
