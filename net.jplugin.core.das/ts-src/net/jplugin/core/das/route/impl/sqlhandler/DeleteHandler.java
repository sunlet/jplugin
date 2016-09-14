package net.jplugin.core.das.route.impl.sqlhandler;

import java.util.List;

import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public class DeleteHandler extends AbstractCommandHandler{

	@Override
	String walkTableName(SqlWordsWalker walker) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	String getFinalSql(SqlWordsWalker walker, String sourceSql, String finalTableName) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	KeyResult walkToGetKeyColumnInfo(SqlWordsWalker walker, String tableName, String keyField) {
		// TODO Auto-generated method stub
		return null;
	}

}
