package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult.CommandType;
import net.jplugin.core.das.route.impl.util.FunctionEvalueKit;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;

public class InsertHandler2 extends AbstractCommandHandler2 {

	@Override
	public List<RouterKeyFilter> getKeyFilter() {
		Insert insertStmt = (Insert) this.statement;
		if (!insertStmt.isUseValues())
			throw new TablesplitException("Can't support sql not use values.");

		Table table = insertStmt.getTable();
		if (table == null)
			throw new TablesplitException("Can't find table");
		if (!StringKit.isNull(table.getSchemaName()))
			throw new TablesplitException("Schema name must be null,"+table.getSchemaName());
		String tablename = table.getName();
		//下面一句没有也没报错
		super.setTableName(tablename);

		TableConfig tableCfg = this.getTableCfg(this.connetion, tablename);
		String keyField = tableCfg.getKeyField();

		List<Column> columns = insertStmt.getColumns();

		int keyPos = findKeyPos(columns, keyField);
		// insertStmt.get
		ItemsList itemlist = insertStmt.getItemsList();
		
		
		RouterKeyFilter kf;
		if (itemlist instanceof ExpressionList){
			kf = getKeyFilterFromItemList((ExpressionList) itemlist, keyPos);
		}else if (itemlist instanceof MultiExpressionList){
			kf = getKeyFilterFromMultiExpList((MultiExpressionList) itemlist, keyPos);
		}else{
			throw new TablesplitException("Sql error:"+this.sqlString);
		}
		
		ArrayList<RouterKeyFilter> ret = new ArrayList(1);
		ret.add(kf);
		return ret;

	}

	private RouterKeyFilter getKeyFilterFromMultiExpList(MultiExpressionList itemlist, int keyPos) {
		MultiExpressionList mel = (MultiExpressionList) itemlist;
		List<ExpressionList> explistlist = mel.getExprList();
		
		//这里用Set结构，去除重复的Key，去计算
		Set<Object> paramList = new HashSet();
		for (ExpressionList explist:explistlist){
			List<net.sf.jsqlparser.expression.Expression> exps = explist.getExpressions();
			net.sf.jsqlparser.expression.Expression exp = exps.get(keyPos);
			Object param = FunctionEvalueKit.evalueNonStrickly(exp, this.parameters);
			if (param==null)
				throw new TablesplitException("The key filter value is null.");
			paramList.add(param);
		}
		
		RouterKeyFilter kf = new RouterKeyFilter(RouterKeyFilter.Operator.IN,paramList.toArray());
		return kf;
	}

	private RouterKeyFilter getKeyFilterFromItemList(ExpressionList itemlist, int keyPos) {
		List<net.sf.jsqlparser.expression.Expression> exps = itemlist.getExpressions();
		if (exps.size() <= keyPos)
			throw new TablesplitException("keyPos out of range.");
		
		net.sf.jsqlparser.expression.Expression exp = exps.get(keyPos);
		
		Object param = FunctionEvalueKit.evalueNonStrickly(exp, this.parameters);
		if (param==null)
			throw new TablesplitException("The key filter value is null.");
		
		RouterKeyFilter kf = new RouterKeyFilter(RouterKeyFilter.Operator.EQUAL,new Object[]{param});
		return kf;
	}

	private int findKeyPos(List<Column> columns, String keyField) {
		if (columns == null)
			throw new TablesplitException("insert columns must not empty");
		for (int i = 0; i < columns.size(); i++) {
			if (keyField.equalsIgnoreCase(columns.get(i).getName(false)))
				return i;
		}
		throw new TablesplitException("Can't find the key column in list");
	}

	@Override
	protected void temporyChangeTableNameTo(String nm) {
		Insert insertStmt = (Insert) this.statement;
		insertStmt.getTable().setName(nm);
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.INSERT;
	}

}
