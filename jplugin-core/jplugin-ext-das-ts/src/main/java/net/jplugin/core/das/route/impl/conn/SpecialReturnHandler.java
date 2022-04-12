package net.jplugin.core.das.route.impl.conn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.jplugin.core.das.dds.api.IRouterDataSource.StatementResult;
import net.jplugin.core.das.dds.impl.DummyConnection;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer.MaintainReturn;
//import net.jplugin.core.das.route.impl.conn.RouterStatement.Result;
import net.jplugin.core.das.route.impl.conn.nrs.NoneResultStatement;

public class SpecialReturnHandler {

	public static PreparedStatement hanleSpecialConditionForPreparedStmt(MaintainReturn mr,Connection connForDummySql) throws SQLException {
		//这里两种特殊情况有且只有一个，前面确认过
		if (mr.isReturnZeroRowUpdateStatement()){
			return new NoneResultStatement();
		}else if (mr.getTargetSqlForDummy()!=null){
			return prepareDummyStatement(connForDummySql,mr.getTargetSqlForDummy());
		}else{
			throw new RuntimeException("shoudln't come here");
		}
	}

	private static PreparedStatement prepareDummyStatement(Connection connForDummySql, String targetSqlForDummy) throws SQLException {
		return connForDummySql.prepareStatement(targetSqlForDummy);
	}
	
	private static Statement createDummyStatement(Connection connForDummySql) throws SQLException {
		return connForDummySql.createStatement();
	}

	public static StatementResult hanleSpecialConditionForStatement(MaintainReturn mr, Connection connForDummySql) throws SQLException {
		//这里两种特殊情况有且只有一个，前面确认过
		if (mr.isReturnZeroRowUpdateStatement()){
			NoneResultStatement stmt = new NoneResultStatement();
			StatementResult result = new StatementResult();
//			result.statement = new NoneResultStatement();
//			result.resultSql = " no sql ";
			result.setStatement(new NoneResultStatement());
			result.setResultSql(" no sql ");
			return result;
		}else if (mr.getTargetSqlForDummy()!=null){
			Statement stmt = createDummyStatement(connForDummySql);
			StatementResult result = new StatementResult();
//			result.resultSql = mr.getTargetSqlForDummy();
//			result.statement = stmt;
			result.setStatement(stmt);
			result.setResultSql(mr.getTargetSqlForDummy());
			return result;
		}else{
			throw new RuntimeException("shoudln't come here");
		}
	}

}
