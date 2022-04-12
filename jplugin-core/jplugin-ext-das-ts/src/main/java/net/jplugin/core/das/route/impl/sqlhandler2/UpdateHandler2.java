package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.dds.api.RouterException;
import net.jplugin.core.das.dds.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult.CommandType;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.update.Update;

public class UpdateHandler2 extends AbstractCommandHandler2 {

	@Override
	public List<RouterKeyFilter> getKeyFilter() {
		Update update = (Update) this.statement;
		
		List<Table> tables = update.getTables();
		if (tables.size()!=1){
			throw new TablesplitException("table num must be 1");
		}
		
		Table table = tables.get(0);
		if (StringKit.isNotNull(table.getSchemaName()))
			throw new RouterException("schema must not specified." + this.sqlString);

		super.setTableName(table.getName());

		List<RouterKeyFilter> kf = null;
		Expression where = update.getWhere();
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
		TableNameReplacerKit.handleUpdate((Update)this.statement, nm);
	}

	@Override
	protected CommandType getCommandType() {
		return CommandType.UPDATE;
	}
}
