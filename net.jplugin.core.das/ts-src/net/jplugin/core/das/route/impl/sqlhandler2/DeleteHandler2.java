package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult.CommandType;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;

public class DeleteHandler2 extends AbstractCommandHandler2 {

	@Override
	public List<RouterKeyFilter> getKeyFilter() {
		Delete delete = (Delete) this.statement;
		
		List<Table> tables = delete.getTables();
		if (tables!=null && tables.size()>0){
			throw new TablesplitException("tables num must empty");
		}
		
		Table table = delete.getTable();
		
		if (StringKit.isNotNull(table.getSchemaName()))
			throw new RouterException("schema must not specified." + this.sqlString);

		super.setTableName(table.getName());

		List<RouterKeyFilter> kf = null;
		Expression where = delete.getWhere();
		if (where!=null)
			kf = super.getKeyFilterFromWhere(where);
		
		if (kf==null || kf.isEmpty()){
			kf = new ArrayList(1);
			kf.add(new RouterKeyFilter(Operator.ALL,new Object[]{}));
		}
		return kf;
	}
	
	@Override
	protected void temporyChangeTableNameTo(String nm) {
//		((Delete) this.statement).getTable().setName(nm);
		TableNameReplacerKit.handleDelete((Delete)this.statement, nm);
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.DELETE;
	}

}
