package net.jplugin.core.das.route.impl.sqlhandler;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.OrderParam;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public abstract class AbstractCommandHandler{

	protected static class KeyResult {
		boolean isParamedKey;
		Object keyConstValue;
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

		boolean spanAll = tryWalkSpanAll(walker);
		String tableName = walkTableName(walker);
		
		if (spanAll){
			CombinedSqlParser.Meta meta = new Meta();
			meta.setSourceTb(tableName);
			meta.setDataSourceInfos(TsAlgmManager.getDataSourceInfos(conn.getDataSource(),tableName));
			meta.setOrderParam(getOrderParam(walker));
			meta.setCountStar(getCountStar(walker)? 1:0);
			String newSql = CombinedSqlParser.combine(sql, meta);
			SqlHandleResult result = new SqlHandleResult();
			result.setResultSql(newSql);
			result.setTargetDataSourceName(CombinedSqlParser.SPANALL_DATASOURCE);
			return result;
		}
		
		TableConfig tableCfg = getTableCfg(conn,tableName);
		if (tableCfg==null) 
			throw new TablesplitException("Table not configed in the router databse."+tableName);
		String keyField = tableCfg.getKeyField();
		String algm = tableCfg.getSplitAlgm();
		
		KeyResult keyResult = walkToGetKeyColumnInfo(walker,tableName,keyField);
		Object keyValue;
		if (keyResult.isParamedKey){
			if (keyResult.keyParamIndex>=params.size())
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


	//当前位置在span 后面，开始检查是否 count(*)  count(0)  count(1)
	private boolean getCountStar(SqlWordsWalker walker) {
		int oldPos = walker.getPosition();
		walker.setPosition(0);
		if (!walker.nextUntil("/")) throw new RuntimeException("can't be here");
		if (!walker.nextUntil("/")) throw new RuntimeException("can't be here");
		//到达  /*spantable*/
		boolean ret;
		String w3 = walker.getNextWord(3);
		if (!("0".equals(w3) || "1".equals(w3) || "*".equals(w3))) 
			ret = false;
		else{
			if  ("count".equalsIgnoreCase(walker.getNextWord(1))
				&&
				"(".equals(walker.getNextWord(2))
				&&
				")".equals(walker.getNextWord(4))
				&&
				"from".equalsIgnoreCase(walker.getNextWord(5))
				) 
				ret = true;
			else ret = false;
		}
		walker.setPosition(oldPos);
		return ret;
	}

	private List<String> getOrderParam(SqlWordsWalker walker) {
		List<String> order = null;
		int oldPos = walker.getPosition();
		if (walker.nextUntilIgnoreCase("order")){
			if (walker.next() && walker.word.equalsIgnoreCase("by")){
				order = new ArrayList<String>();
				
				//目前只支持两个字段的orderby,按照语法，后面就是with语句的部分了，或者结束了
				while (walker.next() && !("with".equalsIgnoreCase(walker.word))){
					order.add(walker.word);
				}

			}
		}
		
		//复原position
		walker.setPosition(oldPos);
		
		if (order!=null && order.size()>0) 
			return order;
		else
			return null;
	}

	private boolean tryWalkSpanAll(SqlWordsWalker walker) {
		if ( "/".equals(walker.getNextWord(1))
		&& "*".equals(walker.getNextWord(2))
		&& "spantable".equalsIgnoreCase(walker.getNextWord(3))
		&& "*".equals(walker.getNextWord(4))
		&& "/".equals(walker.getNextWord(5))){
			walker.next(5);
			return true;
		}else {
			return false;
		}
	}

	private TableConfig getTableCfg(RouterConnection conn,String tableName) {
		TableConfig tableCfg = conn.getDataSource().getConfig().findTableConfig(tableName);
		if (tableCfg ==null) new TablesplitException("The table is configed as a splate table."+tableName);
		return tableCfg;
	}
	
	/**
	 * 'ABC' or  123
	 * @param s
	 * @return
	 */
	protected Object getConstKeyValueFromSql(String s) {
		if (s==null) throw new RuntimeException("The const value can't be null");
		if (s.startsWith("'") && s.endsWith("'")){
			if (s.length()<2)
				throw new RuntimeException("The const value is not correct");
			return s.substring(1,s.length()-1);
		}
		try{
			return Long.valueOf(s);
		}catch(NumberFormatException e){
			throw new RuntimeException("The const must be a int or long style:"+s);
		}
	}
}
