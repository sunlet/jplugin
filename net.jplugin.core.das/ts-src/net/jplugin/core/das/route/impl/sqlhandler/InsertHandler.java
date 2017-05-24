package net.jplugin.core.das.route.impl.sqlhandler;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public class InsertHandler extends AbstractCommandHandler{

	@Override
	String walkTableName(SqlWordsWalker walker) {
		if (!walker.nextUntilIgnoreCase("INTO")) 
			throw new TablesplitException("Can't find INTO word in insert sql."+walker.sql);
		if (!walker.next())
			throw new TablesplitException("Can't find TableName in insert sql."+walker.sql);
		if (StringKit.isNull(walker.word)) 
			throw new TablesplitException("Found null TableName in insert sql."+walker.sql);
		String tableName = walker.word;
		return tableName;
	}

	@Override
	KeyResult walkToGetKeyColumnInfo(SqlWordsWalker walker, String tableName,String keyField) {
		//碰到 ) 就停止
		if (!walker.nextUntil("(")) throw new TablesplitException("Can't found fields segment in sql."+walker.sql); 
		
		boolean success = false;
		int index=0;
		while(walker.next()){
			if (")".equals(walker.word))
				break;
			if (",".equals(walker.word))
				index++;
			if (keyField.equalsIgnoreCase(walker.word)){
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
			result.keyConstValue = getConstKeyValueFromSql(walker.word);
		}
		
		return result;
	}

	@Override
	String getFinalSql(SqlWordsWalker walker, String sourceSql, String finalTableName) {
		String finalSql = StringKit.repaceFirst(walker.sql,sourceSql,finalTableName);;
		return finalSql;
	}


}
