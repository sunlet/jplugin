package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;
import java.util.List;

import net.jplugin.core.das.route.impl.CombinedSqlContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class CountStarWrapperController implements WrapperController{

	private static final String COUNT_STAR = "COUNT_STAR";

	@Override
	public boolean needWrap() {
//		CombinedSqlParser.Meta meta = (Meta) RouterConnectionCallContext.getMeta();
//		return meta.getCountStar()==Meta.COUNG_STAR_YES;

		CombinedSqlContext ctx = CombinedSqlContext.get();
		Object flag = ctx.getAttribute(COUNT_STAR);
		return (flag!=null && (boolean)flag);
	}

	@Override
	public ResultSet wrap(ResultSet rs) {
		return new CountStarWrapper(rs);
	}

	@Override
	public void handleContextInitial(CombinedSqlContext ctx) {
		ctx.setAttribute(COUNT_STAR, isCountStar(ctx.getStatement()));
	}

	/**
	 * 这里判断最外层是不是count(*),count(0),count(n)
	 * @return
	 */
	private boolean isCountStar(Statement statement) {
		//获取Function
		SelectBody body = ((Select)statement).getSelectBody();
		List<SelectItem> items = ((PlainSelect)body).getSelectItems();
		if(items.size()!=1) return false;
		SelectItem onlyItem = items.get(0);
		if (!(onlyItem instanceof SelectExpressionItem))
			return false;
		
		Expression expression = ((SelectExpressionItem)onlyItem).getExpression();
		if (!(expression instanceof Function))
			return false;
		
		Function function = (Function) expression;
		
		//下面判断function是否满足
		if (!"count".equalsIgnoreCase(function.getName())) {
			return false;
		}
		ExpressionList functionParams = function.getParameters();
		if (functionParams == null && function.isAllColumns())
			return true;
		List<Expression> expressions = functionParams.getExpressions();
		if (expressions == null)
			return false;
		if (expressions.size() == 0 && function.isAllColumns())
			return true;
		if (expressions.size() > 1)
			return false;
		Expression functionParam = expressions.get(0);
		if (!(functionParam instanceof LongValue))
			return false;
		else
			return true;
	}
}
