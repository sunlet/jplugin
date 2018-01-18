package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ExpressionAggregrator {

	private SelectExpressionItem selectItem;

	public ExpressionAggregrator(SelectItem si) {
		if (si instanceof SelectExpressionItem){
			this.selectItem = (SelectExpressionItem) si;
		}else{
			throw new RuntimeException("select item must be a Expression. "+si.toString());
		}
		
	}

	public void aggrateItem(Object v) {
		Expression exp = selectItem.getExpression();
		
		
	}

	public void resetState() {
		// TODO Auto-generated method stub
		
	}

}
