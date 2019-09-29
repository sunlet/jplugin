package net.jplugin.core.das.route.impl.conn;

import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser;
import net.sf.jsqlparser.statement.Statement;

public class SqlHandleResult {
	public enum CommandType{SELECT,UPDATE,INSERT,DELETE}
	
	String sourceTable; 
	/**
	 * 单表情况下：表示目标sql，当中的表名用固定常亮。
	 * 多表情况下：表示目标sql
	 */
	String resultSql;
	
	DataSourceInfo[] dataSourceInfos;

	CommandType commandType;
	
	TableConfig tableConfig;
	
	Statement statement;//原始sql的statement

	/**
	 * 判断是否只有一张表
	 * @return
	 */
	public boolean singleTable(){
		return dataSourceInfos.length==1 && dataSourceInfos[0].getDestTbs().length==1;
	}
	
	public String getSourceTable() {
		return sourceTable;
	}


	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}


	public String getResultSql() {
		return resultSql;
	}


	public void setResultSql(String resultSql) {
		this.resultSql = resultSql;
	}


	public DataSourceInfo[] getDataSourceInfos() {
		return dataSourceInfos;
	}


	public void setDataSourceInfos(DataSourceInfo[] dataSourceInfos) {
		this.dataSourceInfos = dataSourceInfos;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}
	
	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}
	
	public TableConfig getTableConfig() {
		return tableConfig;
	}
	
	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}
	
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	public Statement getStatement() {
		return statement;
	}

	public String getEncodedSql() {
		CombinedSqlParser.Meta meta = new CombinedSqlParser.Meta();
		meta.setDataSourceInfos(dataSourceInfos);
		meta.setSourceTb(this.sourceTable);
		return CombinedSqlParser.combine(this.resultSql, meta);
	}

	
}
