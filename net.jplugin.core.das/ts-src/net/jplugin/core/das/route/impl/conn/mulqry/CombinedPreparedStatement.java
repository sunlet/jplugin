package net.jplugin.core.das.route.impl.conn.mulqry;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.CombinedSelectContext;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.ParseResult;
import net.jplugin.core.das.route.impl.conn.mulqry.rswrapper.WrapperManager;

public class CombinedPreparedStatement extends CombinedStatement implements PreparedStatement{

//	ParseResult sqlParseResult;
	
//	private CombinedSelectContext sqlExeutionContext;

	public CombinedPreparedStatement(Connection conn, String sql){
		super(conn);
		//解析
//		sqlParseResult = CombinedSqlParser.parseAndMakeContext(sql);
//		ParseResult sqlParseResult = CombinedSqlParser.parse(sql);
//		selectContext = CombinedSelectContext.makeContext(sqlParseResult);
		super.parseAndComputeTypeAndMakeSelectContext(sql);
		
		//产生statement 列表
		fillStatementList();
	}
	
	private void fillStatementList(){
		try{
			DataSourceInfo[] dataSourceInfos = this.selectContext.getDataSourceInfos();
			String sqlToExecute = this.selectContext.getFinalSql();
			String sourceTableToReplace = this.selectContext.getOriginalTableName();//后面会修改
			
			for (DataSourceInfo dsi:dataSourceInfos){
				Connection conn = DataSourceFactory.getDataSource(dsi.getDsName()).getConnection();
				//每一个表都获取一个Statement，并不重用
				for (String destTbName:dsi.getDestTbs()){
					Statement stmt = conn.prepareStatement(StringKit.repaceFirst(sqlToExecute,sourceTableToReplace, destTbName));
					statementList.add(stmt);
				}
			}
		}catch(Exception e){
			for (Statement s:statementList){
				try{
					s.close();
				}catch(Exception e1){}
			}
			statementList.clear();
			throw new TablesplitException(e.getMessage() + "  "+ CombinedSelectContext.get().getFinalSql(),e);
		}
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		ResultSetList temp = genResultSetListFromStatementList();
		
		//根据count(*)模式返回不同的值
		this.theResultSet =  WrapperManager.INSTANCE.wrap(temp);
		return this.theResultSet;
		
//		if (sqlParseResult.getMeta().getCountStar()==1){
//			this.theResultSet = new CountStarWrapper(temp);
//			return this.theResultSet;
//		}else{
//			this.theResultSet = temp;
//			return theResultSet;
//		}
	}

	private ResultSetList genResultSetListFromStatementList() {
		List<ResultSet> tempList = new ArrayList<ResultSet>();
		try{
			//逐个执行查询
			for (Statement st:this.statementList){
				tempList.add(((PreparedStatement)st).executeQuery());
			}
//			makeWrapperForDebug(tempList);
			//制造结果
//			ResultSetList ret = new ResultSetList(this,tempList,pr.getMeta().getOrderParam());
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
			else throw new TablesplitException(e.getMessage() + "  "+ CombinedSelectContext.get().getFinalSql(),e);
		}
	}
	private void makeWrapperForDebug(List<ResultSet> list) {
		List<ResultSet> tempList= new ArrayList(list.size());
		tempList.addAll(list);
		list.clear();
		
		for (int i=0;i<tempList.size();i++){
			list.add(new ResultWrapperForDebug(tempList.get(i)));
		}
	}

	private int getSize(ResultSet rs) throws SQLException {
		int size =0;
		while(rs.next()){
			size++;
		}
		return size;
	}

	@Override
	public int executeUpdate() throws SQLException {
		throw new TablesplitException("not support");
	}
	
	@Override
	public boolean execute() throws SQLException {
		this.executeQuery();
		return true;
	}

	@Override
	public void addBatch() throws SQLException {
		throw new TablesplitException("not support");
	}
	
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		if (this.statementList.size()==0) throw new TablesplitException("st list is empty");
		return ((PreparedStatement)this.statementList.get(0)).getMetaData();
	}
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		if (this.statementList.size()==0) throw new TablesplitException("list is empty");
		return ((PreparedStatement)this.statementList.get(0)).getParameterMetaData();
	}

	
	
	////////////////follow default impl

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setNull(parameterIndex, sqlType);
		}
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setBoolean(parameterIndex, x);
		}
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setByte(parameterIndex, x);
		}
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setShort(parameterIndex, x);
		}
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setInt(parameterIndex, x);
		}
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setLong(parameterIndex, x);
		}
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setFloat(parameterIndex, x);
		}
		
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setDouble(parameterIndex, x);
		}
		
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setBigDecimal(parameterIndex, x);
		}
		
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setString(parameterIndex, x);
		}
		
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setBytes(parameterIndex, x);
		}
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setDate(parameterIndex, x);
		}
		
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setTime(parameterIndex, x);
		}
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setTimestamp(parameterIndex, x);
		}
		
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void clearParameters() throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).clearParameters();
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setObject(parameterIndex, x,targetSqlType);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setObject(parameterIndex, x);
		}
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setRef(parameterIndex, x);
		}
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setBlob(parameterIndex, x);
		}
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setClob(parameterIndex, x);
		}
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setArray(parameterIndex, x);
		}
	}



	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setDate(parameterIndex, x, cal);
		}
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setTime(parameterIndex, x, cal);
		}
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setTimestamp(parameterIndex, x, cal);
		}
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setNull(parameterIndex, sqlType, typeName);
		}
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setURL(parameterIndex, x);
		}
		
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setRowId(parameterIndex, x);
		}
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setNString(parameterIndex, value);
		}
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setSQLXML(parameterIndex, xmlObject);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		for (Statement st:this.statementList){
			((PreparedStatement)st).setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		}
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new TablesplitException("not support");
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new TablesplitException("not support");
	}


	
	
	
}
