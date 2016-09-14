package net.jplugin.core.das.route.impl.sqlhandler;

import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public class InsertHandler extends AbstractCommandHandler{

	static class KeyResult {
		boolean isParamedKey;
		String keyConstValue;
		int keyParamIndex;
	}
	
	@Override
	public SqlHandleResult handle(RouterConnection conn, String sql, List<Object> params, SqlWordsWalker walker) {
		if (!walker.nextUntilIgnoreCase("INTO")) 
			throw new TablesplitException("Can't find INTO word in insert sql."+sql);
		if (!walker.next())
			throw new TablesplitException("Can't find TableName in insert sql."+sql);
		if (StringKit.isNull(walker.word)) 
			throw new TablesplitException("Found null TableName in insert sql."+sql);
		String tableName = walker.word;
		TableConfig tableCfg = super.getTableCfg(conn,tableName);
		if (tableCfg==null) 
			throw new TablesplitException("Table not configed in the router databse."+tableName);
		String keyField = tableCfg.getKeyField();
		String algm = tableCfg.getSplitAlgm();
		
		KeyResult keyResult = walkToGetKeyColumn(walker,keyField);
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
		result.setResultSql(replaceTableName(walker,tableName,algmResult.getTableName()));
		return result;
	}

	private String replaceTableName(SqlWordsWalker walker, String tableName,String newTableName) {
		return StringKit.repaceFirst(walker.sql,tableName,newTableName);
	}

	private KeyResult walkToGetKeyColumn(SqlWordsWalker walker, String keyField) {
		//碰到 ) 就停止
		if (!walker.nextUntil("(")) throw new TablesplitException("Can't found fields segment in sql."+walker.sql); 
		
		boolean success = false;
		int index=0;
		while(walker.next()){
			if (")".equals(walker.word))
				break;
			if (",".equals(walker.word))
				index++;
			if (keyField.equalsIgnoreCase(keyField)){
				success = true;
				break;
			}
		}
		if (!success)
			throw new TablesplitException("Can't find the key field in sql."+walker.sql); 
		int keyFieldIndex = index;
		
		if (!walker.nextUntilIgnoreCase("values"))
			throw new TablesplitException("Can't find values sql."+walker.sql); 
		if (!walker.nextUntilIgnoreCase("("))
			throw new TablesplitException("Can't find '(' after values. sql "+walker.sql); 
		
		index = 0;
		int paramIndex = 0;
		while (walker.next()){
			if (index == keyFieldIndex){
				break;//此时刚好walker到这个field的值了！
			}
			if ("?".equals(walker.word)){
				paramIndex ++;
			}
			if (",".equals(walker.word)){
				index ++;
			}
			if ("(".equals(walker.word)){
				walker.nextUntilMatchingBracket();
			}
		}
		
		KeyResult result= new KeyResult();
		if (walker.word.equals("?")){
			result.isParamedKey = true;//此时 paramIndex表示问号的次序，0表示第一个
			result.keyParamIndex = paramIndex;
		}else{
			result.isParamedKey = false;
			if (!(",".equals(walker.getNextWord())))
				throw new TablesplitException("key value value only support a Const value or a Parameter."+walker.sql); 
			result.keyConstValue = walker.word;
		}
		
		return result;
	}

	

}
