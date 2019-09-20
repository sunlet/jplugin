package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.RouterDataSourceConfig;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SchemaCheckUtil {
	private static final String E_M_P_T_Y = "E_M_P_T_Y";
	/**
	 * 为了提高速度，去除的同时取出，然后验证
	 * @param conn
	 * @param stmt
	 */
	public static void checkAndRemoveSchema(RouterConnection conn, Statement stmt,String sqlToLog) {
		Set<String> schemas = extractAndRemoveSchema(stmt);
		if (!checkSchema(schemas,conn)){
			throw new TablesplitException("schema check error. "+sqlToLog);
		}
	}

	/**
	 * 由于Shcmne在一个句子里面很少，这些写没有问题
	 */
	private static boolean checkSchema(Set<String> schemas, RouterConnection conn) {
		RouterDataSourceConfig dscfg = conn.getDataSource().getConfig();
		for (String s:schemas){
			if (E_M_P_T_Y.equals(s)){
				//如果是空，则判断是否允许空，不允许则退出
				if (!dscfg.getIsAllowNoSchema()){
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

	private static Set<String> extractAndRemoveSchema(Statement stmt) {
		SchemaNameFinderAndRemover snf = new SchemaNameFinderAndRemover();
		return snf.getSchemas(stmt);
	}
	
	static class SchemaNameFinderAndRemover extends TablesNamesFinder{
		
		Set<String> schemaNames = new HashSet();
		
		public Set<String> getSchemas(Statement stmt){
			getTableList(stmt);
			return this.schemaNames;
		}
		
		/**
		 * <PRE>
		 * 这里为了获取schemaname ，所以对这个方法进行了重载。
		 * 并且tablename已经是底层，不需要继续调用父类的visit。
		 * </PRE>
		 */
	    public void visit(Table tableName) {
	    	String name = tableName.getSchemaName();
	        if(StringKit.isNull(name)){
	        	name = E_M_P_T_Y;
	        }else{
	        	tableName.setSchemaName(null);
	        }
	        schemaNames.add(name.toUpperCase());
	    }
	}

}
