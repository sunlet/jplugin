package net.jplugin.core.das.route.impl.conn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteResult {
	private Statement statement = null;
	boolean closeCalled = true; //表示上次设置过statement以后有没有调用过clear/close方法，初始true
	
	public ResultSet getResult() throws SQLException{
		if (statement!=null) return statement.getResultSet();
		else return null;
	}
	public int getUpdateCount() throws SQLException{
		if (statement!=null) return statement.getUpdateCount();
		return -1;
	}
	
	public void clear() {
		if (statement!=null){ //让它等于null，也只能是调用过clear方法一次造成的
			if (!closeCalled){
				try{
					statement.close();
				}catch(Throwable e){
				}
			}
			statement = null;
			closeCalled = true;
		}
	}
	
	public void set(Statement stmt) throws SQLException{
		clear();
		statement = stmt;
		closeCalled = false;
	}
	public boolean getMoreResults() throws SQLException {
		if (this.statement!=null) 
			return statement.getMoreResults();
		else return false;
	}
}