package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;

public class InsertHandler2 extends AbstractCommandHandler2 {

	@Override
	public List<RouterKeyFilter> getKeyFilter() {
		Insert insertStmt = (Insert) this.statement;
		if (!insertStmt.isUseValues())
			throw new RuntimeException("Can't support sql not use values.");
		
		Table table = insertStmt.getTable();
		if (table==null)
			throw new RuntimeException("Can't find table");
		if (StringKit.isNull(table.getSchemaName()))
			throw new RuntimeException("Schema name must be null");
		String tablename = table.getName();
		
		TableConfig tableCfg = this.getTableCfg(this.connetion, tablename);
		String keyField = tableCfg.getKeyField();
		
		List<Column> columns = insertStmt.getColumns();
		
		int keyPos = findKeyPos(columns); 
		return null;
	}


	
	private int findKeyPos(List<Column> columns) {
		
		return 0;
	}

	@Override
	protected void temporyChangeTableNameTo(String nm) {
		// TODO Auto-generated method stub
		
	}

}
