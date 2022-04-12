package net.jplugin.core.das.dds.select;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.tuple.Tuple2;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Block;
import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.DescribeStatement;
import net.sf.jsqlparser.statement.ExplainStatement;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.ShowColumnsStatement;
import net.sf.jsqlparser.statement.ShowStatement;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.UseStatement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**extends TablesNamesFinder*/
public class TableNameAndCommandFinder 
		implements StatementVisitor, SelectVisitor, FromItemVisitor {
	public static final String NOT_SUPPORT_YET="not support yet";
	
	private List<String> tables = new ArrayList<String>();
	private String command;
	
	
	public static Tuple2<List<String>, String> find(Statement result){
		TableNameAndCommandFinder finder = new TableNameAndCommandFinder();
		result.accept(finder);
		return Tuple2.with(finder.tables,finder.command);
	}

	private void addTable(Table tableName) {
		this.tables.add(tableName.getFullyQualifiedName());
	}

	private void setCommand(String cmd) {
		if (command!=null) 
			throw new RuntimeException("cmd can't set twice");
		command = cmd;
	}
	/**
	 * FromItemVisiter的内容
	 */

	@Override
	public void visit(Table tableName) {
		this.addTable(tableName);
	}

	@Override
	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}

	@Override
	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		for (Join join : subjoin.getJoinList()) {
			join.getRightItem().accept(this);
		}
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		lateralSubSelect.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(ValuesList valuesList) {
	}

	@Override
	public void visit(TableFunction tableFunction) {
	}

	@Override
	public void visit(ParenthesisFromItem aThis) {
		aThis.getFromItem().accept(this);
	}

	/**
	 * 以下来自SelectVisitor
	 */
	@Override
	public void visit(PlainSelect plainSelect) {
		if (plainSelect.getFromItem() != null) {
			plainSelect.getFromItem().accept(this);
		}

		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				join.getRightItem().accept(this);
			}
		}
	}

	@Override
	public void visit(SetOperationList list) {
		for (SelectBody plainSelect : list.getSelects()) {
			plainSelect.accept(this);
		}
	}

	@Override
	public void visit(WithItem withItem) {
	}

	@Override
	public void visit(ValuesStatement values) {
	}

	/**
	 * 以下来自StatementVisiter
	 */

	@Override
	public void visit(Comment comment) {
		setCommand(SelectDatasourceConfig.CMD_COMMENT);
		
		if (comment.getTable() != null) {
			addTable(comment.getTable());
		}

	}



	@Override
	public void visit(Commit commit) {
	}

	@Override
	public void visit(Delete delete) {
		setCommand(SelectDatasourceConfig.CMD_DELETE);
		
		addTable(delete.getTable());
	}

	@Override
	public void visit(Update update) {
		setCommand(SelectDatasourceConfig.CMD_UPDATE);
		
		for (Table table : update.getTables()) {
			 addTable(table);
	     }
	}

	@Override
	public void visit(Insert insert) {
		setCommand(SelectDatasourceConfig.CMD_INSERT);
		
		addTable(insert.getTable());
	}

	@Override
	public void visit(Replace replace) {
		setCommand(SelectDatasourceConfig.CMD_REPLACE);
		
		addTable(replace.getTable());

	}

	@Override
	public void visit(Drop drop) {
		setCommand(SelectDatasourceConfig.CMD_DROP); 
		
		addTable(drop.getName());

	}

	@Override
	public void visit(Truncate truncate) {
		setCommand(SelectDatasourceConfig.CMD_TRUNCATE);
		
		addTable(truncate.getTable());

	}

	@Override
	public void visit(CreateIndex createIndex) {
		setCommand(SelectDatasourceConfig.CMD_CREATE_INDEX);
		
		addTable(createIndex.getTable());

	}

	@Override
	public void visit(CreateTable createTable) {
		setCommand(SelectDatasourceConfig.CMD_CREATE_TABLE); 
		
		addTable(createTable.getTable());
	}

	@Override
	public void visit(CreateView createView) {
		setCommand(SelectDatasourceConfig.CMD_CREATE_VIEW); 
		
		addTable(createView.getView());
	}

	@Override
	public void visit(AlterView alterView) {
		setCommand(SelectDatasourceConfig.CMD_ALTER_VIEW);
		
		addTable(alterView.getView());
	}

	@Override
	public void visit(Alter alter) {
		setCommand(SelectDatasourceConfig.CMD_ALTER);
		
		addTable(alter.getTable());
	}

	@Override
	public void visit(Statements stmts) {
		for (Statement st:stmts.getStatements()) {
			st.accept(this);
		}
	}

	@Override
	public void visit(Execute execute) {
		throw new RuntimeException(NOT_SUPPORT_YET);
	}

	@Override
	public void visit(SetStatement set) {
		throw new RuntimeException(NOT_SUPPORT_YET); 
	}

	@Override
	public void visit(ShowColumnsStatement set) {
		throw new RuntimeException(NOT_SUPPORT_YET); 
	}

	@Override
	public void visit(Merge merge) {
		setCommand(SelectDatasourceConfig.CMD_MERGE); 
		
		addTable(merge.getTable());

	}

	@Override
	public void visit(Select select) {
		setCommand(SelectDatasourceConfig.CMD_SELECT);
		
		select.getSelectBody().accept(this);
	}

	@Override
	public void visit(Upsert upsert) {
		setCommand(SelectDatasourceConfig.CMD_UPSERT);
		
		addTable(upsert.getTable());
	}

	@Override
	public void visit(UseStatement use) {
		throw new RuntimeException(NOT_SUPPORT_YET);
	}

	@Override
	public void visit(Block block) {
		throw new RuntimeException(NOT_SUPPORT_YET);
	}

	@Override
	public void visit(DescribeStatement describe) {
		setCommand(SelectDatasourceConfig.CMD_DESC);
		
		addTable(describe.getTable());
	}

	@Override
	public void visit(ExplainStatement aThis) {
		throw new RuntimeException(NOT_SUPPORT_YET);
	}

	@Override
	public void visit(ShowStatement aThis) {
		throw new RuntimeException(NOT_SUPPORT_YET);	
	}

}
