package net.jplugin.core.das.route.impl.conn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteResult {
//	private ResultSet rs;
//	private int updCount = -1;
	private Statement statement = null;
	public ResultSet getResult() throws SQLException{
		if (statement!=null) return statement.getResultSet();
		else return null;
	}
	public int getUpdateCount() throws SQLException{
		if (statement!=null) return statement.getUpdateCount();
		return -1;
	}
	
	public void clear() {
//		rs = null;
//		updCount = -1;
		if (statement!=null){
			try{
				if (!statement.isClosed()){
					statement.close();
				}
			}catch(Throwable e){}
			statement = null;
		}
	}
	public void set(Statement stmt) throws SQLException{
		clear();
//		rs = stmt.getResultSet();
//		updCount = stmt.getUpdateCount();
		statement = stmt;
	}
	public boolean getMoreResults() throws SQLException {
		if (this.statement!=null) 
			return statement.getMoreResults();
		else return false;
	}
}