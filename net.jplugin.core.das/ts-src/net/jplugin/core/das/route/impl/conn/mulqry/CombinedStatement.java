package net.jplugin.core.das.route.impl.conn.mulqry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.CombinedSelectContext;
import net.jplugin.core.das.route.impl.conn.EmptyStatement;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.ParseResult;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperManager;

public class CombinedStatement extends EmptyStatement{
	public enum CommandType{SELECT,UPDATE,INSERT,DELETE}
	
	protected List<Statement> statementList = new ArrayList();
	protected ResultSet theResultSet;
	protected CommandType commandType;
	protected ParseResult sqlParseResult;
	protected CombinedSelectContext selectContext;//只有select语句有这个Context
	
	private boolean closed;
	private Connection conn;
	
	
	public CombinedStatement(Connection c){
		this.conn = c;
	}
	@Override
	public final boolean execute(String sql) throws SQLException {
		this.parseAndComputeTypeAndMakeSelectContext(sql);
		if (CommandType.SELECT.equals(this.commandType)){
			this.executeQueryInner(sql);
			return true;
		}else{
			int cnt = this.executeUpateInner(sql);
			System.out.println("这里的true和false需要商榷！！！！！");
			return true;
		}
	}

	@Override
	public final ResultSet executeQuery(String sql) throws SQLException {
		this.parseAndComputeTypeAndMakeSelectContext(sql);
		AssertKit.assertEqual(this.commandType, CommandType.SELECT);
		return this.executeQueryInner(sql);
	}
	
	protected void parseAndComputeTypeAndMakeSelectContext(String sql){
		//PARSE
		sqlParseResult = CombinedSqlParser.parse(sql);
		//GET TYPE
		commandType = computeCommandType(sqlParseResult.getSql());
		
		//MAKE CONTEXT
		if (CommandType.SELECT.equals(this.commandType))
			this.selectContext = CombinedSelectContext.makeContext(sqlParseResult);	
	}

	private CommandType computeCommandType(String sql) {
		CommandType ct;
		String temp = sql.trim().substring(0,6).toUpperCase();
		if (temp.startsWith("SELECT")){
			ct =  CommandType.SELECT;
		}else if (temp.startsWith("UPDATE")){
			ct =  CommandType.UPDATE;
		}else if (temp.startsWith("DELETE")){
			ct =  CommandType.DELETE;
		}else if (temp.startsWith("INSERT")){
			ct =  CommandType.INSERT;
		}else 
			throw new TablesplitException("not supported command type:"+sql);
		return ct;
	}
	private int executeUpateInner(String sql) {
		throw new RuntimeException("not imp");
	}
	
	private final ResultSet executeQueryInner(String sql) throws SQLException {
		if (this.closed)
			throw new TablesplitException("can't call in a closed statement");

		//获取resultList
		ResultSetList resutSet = genResultSetList();
		
		//根据count(*)模式返回不同的值
		this.theResultSet =  WrapperManager.INSTANCE.wrap(resutSet);
		return this.theResultSet;
	}
	
	private ResultSetList genResultSetList() throws SQLException {
		List<ResultSet> tempList = new ArrayList<ResultSet>();
		try{
			DataSourceInfo[] dataSourceInfos = this.selectContext.getDataSourceInfos();
			String sqlToExecute = this.selectContext.getFinalSql();
			String sourceTableToReplace = this.selectContext.getOriginalTableName();//后面会修改
			
			for (DataSourceInfo dsi:dataSourceInfos){
				Connection conn = DataSourceFactory.getDataSource(dsi.getDsName()).getConnection();
				for (String destTbName:dsi.getDestTbs()){
					Statement stmt = conn.createStatement();
					statementList.add(stmt);
					ResultSet resultSet = stmt.executeQuery(StringKit.repaceFirst(sqlToExecute, sourceTableToReplace, destTbName));
					tempList.add(resultSet);
				}
			}
			ResultSetList ret = new ResultSetList(this,tempList,this.selectContext);
			return ret;
		}catch(Exception e){
			//发生异常的情况下，statement 会在本statement关闭的时候关闭，但是resultSet不会，需要处理一下
			for (ResultSet r:tempList){
				try{
					r.close();
				}catch(Exception e1){}
			}
			if (e instanceof RuntimeException) throw (RuntimeException)e;
			else throw new TablesplitException(e.getMessage()+" sql="+ CombinedSelectContext.get().getFinalSql(),e);
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void close() throws SQLException {
		String message="";
		int errNum = 0;
		for (Statement s:statementList){
			try{
				s.close();
			}catch(Exception e){
				errNum++;
				message = message +" "+e.getMessage();
			}
		}
		//set closed
		this.closed = true;
		//check throw exception
		if (StringKit.isNotNull(errNum+" exception when close,"+message)) 
			throw new TablesplitException(message);
	}


	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.theResultSet;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return -1;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.conn;
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new TablesplitException("update multi table is not supported");
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new TablesplitException("update multi table is not supported");	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new TablesplitException("update multi table is not supported");	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.closed;
	}

	
	
}
