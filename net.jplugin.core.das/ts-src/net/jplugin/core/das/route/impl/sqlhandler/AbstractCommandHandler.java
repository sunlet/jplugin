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

	//判断是否是select count(*) from 。。。模式
	private boolean getCountStar(SqlWordsWalker walker) {
		if (!"select".equalsIgnoreCase(walker.wordAt(0)))
				return false;
		boolean hasCount = false;
		int pos=1;
		while (pos < walker.size()) {
			String w = walker.wordAt(pos);
			if (w.startsWith("/*") && w.endsWith("*/")) {
				//注释忽略
				pos++;
			}else if (",".equals(w)){
				//逗号忽略
				pos ++;
			}else if ("from".equalsIgnoreCase(w)){
				//碰到from 跳出循环
				return hasCount;
			} else if ("count".equalsIgnoreCase(w)) {
				//碰到count，进行判断
				String posAdd2 = walker.wordAt(pos + 2);
				if (!("0".equals(posAdd2) || "1".equals(posAdd2) || "*".equals(posAdd2)))
					return false;
				if (!("(".equals(walker.wordAt(pos + 1)) && ")".equals(walker.wordAt(pos + 3))))
					return false;
				hasCount = true;//存在 count
				pos += 4; //跳过 count(1/*/0) 
			}else{
				//都不是，则false
				return false;
			}
		}
		//应该到不了这里，除非：SELECT from之间，除了注释和逗号，没有其他的
		throw new RuntimeException("Sql illegal:"+walker.sql);
	}

	private List<String> getOrderParam(SqlWordsWalker walker) {
		List<String> order = null;
		int oldPos = walker.getPosition();
		if (walker.nextUntilIgnoreCase("order")){
			if (walker.next() && walker.word.equalsIgnoreCase("by")){
				order = new ArrayList<String>();
				
				//目前只支持两个字段的orderby,按照语法，后面就是with语句的部分了，或者结束了
				while (walker.next() &&  !(")".equals(walker.word)) && !("with".equalsIgnoreCase(walker.word))){
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

	/**
	 * 16-11-18修改为从全串检索，不能只看第一个select后面。因为select可能会被框架自动添加select count(*) from (XXXX)
	 * @param walker
	 * @return
	 */
	private boolean tryWalkSpanAll(SqlWordsWalker walker) {
		for (int i=0;i<walker.size();i++){
			if (walker.wordAt(i).startsWith("/*")){
				String tmp = walker.wordAt(i);
				if (tmp.endsWith("*/") && tmp.length()>5 && tmp.substring(2, tmp.length()-2).trim().equalsIgnoreCase("spantable"))
					return true;
			}
		}
		return false;
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
