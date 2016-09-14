package net.jplugin.core.das.route.impl.sqlhandler;

import java.util.List;

import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public abstract class AbstractCommandHandler{

	protected static class KeyResult {
		boolean isParamedKey;
		String keyConstValue;
		int keyParamIndex;
	}
	
	/**
	 * this  position is at beginning
	 * @param walker
	 * @return
	 */
	abstract String walkTableName(SqlWordsWalker walker);
	
	/**
	 * the position is after got table name
	 * @param walker
	 * @param keyField
	 * @return
	 */
	abstract KeyResult walkToGetKeyColumnInfo(SqlWordsWalker walker,String tableName, String keyField) ;
	
	abstract String getFinalSql(SqlWordsWalker walker, String sourceSql,String finalTableName);
	
	public SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params, SqlWordsWalker walker) {
		
		String tableName = walkTableName(walker);
		TableConfig tableCfg = getTableCfg(conn,tableName);
		if (tableCfg==null) 
			throw new TablesplitException("Table not configed in the router databse."+tableName);
		String keyField = tableCfg.getKeyField();
		String algm = tableCfg.getSplitAlgm();
		
		KeyResult keyResult = walkToGetKeyColumnInfo(walker,tableName,keyField);
		Object keyValue;
		if (keyResult.isParamedKey){
			if (keyResult.keyParamIndex>=params.size()-1)
				throw new TablesplitException("Can't find the params for index "+keyResult.keyParamIndex+" param size="+params.size());
			keyValue = params.get(keyResult.keyParamIndex);
		}else{
			keyValue = keyResult.keyConstValue;
		}
		
		Result algmResult = TsAlgmManager.getResult(conn.getDataSource(), tableName, keyValue);
		
		SqlHandleResult result = new SqlHandleResult();
		result.setTargetDataSourceName(algmResult.getDataSource());
		String finalSql = getFinalSql(walker,tableName,algmResult.getTableName());
		result.setResultSql(finalSql);
		return result;
	}


	private TableConfig getTableCfg(RouterConnection conn,String tableName) {
		TableConfig tableCfg = conn.getDataSource().getConfig().findTableConfig(tableName);
		if (tableCfg ==null) new TablesplitException("The table is configed as a splate table."+tableName);
		return tableCfg;
	}
	
	protected String removeSingleQuote(String s) {
		if (s==null) return null;
		if (s.length()==1) return null;
		if (s.startsWith("'") && s.endsWith("'")){
			return s.substring(1, 2);
		}else
			return s;
	}
}
