package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.impl.CombinedSqlContext;
import net.jplugin.core.das.route.impl.util.SelectSqlKit;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class GroupByWrapperController implements WrapperController{

	private static final String OLD_ORDERBY = "OLD_ORDERBY";
	private static final String USING_GROUPBY = "USING_GROUPBY";
	private static final String GROUPBY_SQL_SELECTLIST = "GROUPBY_SQL_SELECTLIST";

	@Override
	public boolean needWrap() {
//		CombinedSqlContext combinedSqlContext = CombinedSqlContext.get();
//		Boolean b = (Boolean) combinedSqlContext.getAttribute(USING_GROUPBY);
//		if (b!=null && b){
//			return true;
//		}
		return false;
	}

	@Override
	public ResultSet wrap(ResultSet rs) {
		CombinedSqlContext combinedSqlContext = CombinedSqlContext.get();
		Boolean b = (Boolean) combinedSqlContext.getAttribute(USING_GROUPBY);
		if (b!=null && b){
			try {
				List<SelectItem> initialItems = (List<SelectItem>) combinedSqlContext.getAttribute(GROUPBY_SQL_SELECTLIST);
				rs = new GroupByWrapper(rs,initialItems);
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage()+" "+CombinedSqlContext.get().getOriginalSql(),e);
			}
			
			List<OrderByElement> oldOrderBy = (List<OrderByElement>) combinedSqlContext.getAttribute(OLD_ORDERBY);
			if (oldOrderBy!=null){
				rs = new GroupByMemoryOrderByWrapper(rs,oldOrderBy);
			}
		}
		return rs;
	}

	@Override
	public void handleContextInitial(CombinedSqlContext ctx) {
		if (1==2){
			SelectBody bd = ctx.getStatement().getSelectBody();
			PlainSelect inner = SelectSqlKit.getMostInnerSelect(bd, ctx.getOriginalSql());
			List<Expression> groupbylist = inner.getGroupByColumnReferences();
			
			if (groupbylist!=null && !groupbylist.isEmpty()){
				//产生ExpressionList, 这里用一个新的List，因为后面这个List会修改，增加一个新的项目
				List<SelectItem> initialSelectItems = new ArrayList(inner.getSelectItems().size());
				initialSelectItems.addAll(inner.getSelectItems());
				ctx.setAttribute(GROUPBY_SQL_SELECTLIST, initialSelectItems);
				
				//新增一个组合字段
				SelectExpressionItem itemToAdd = makeSelectItem(groupbylist);
				inner.getSelectItems().add(itemToAdd);
				ctx.setAttribute(USING_GROUPBY, true);
				
				//旧的orderby拿下来
				List<OrderByElement> oldorderby = inner.getOrderByElements();
				if (oldorderby!=null && !oldorderby.isEmpty()){
					ctx.setAttribute(OLD_ORDERBY, oldorderby);
				}
				
				//增加新的orderby
				inner.setOrderByElements(makeNewOrderBy(groupbylist));
			}
		}

	}
	
	private List<OrderByElement> makeNewOrderBy(List<Expression> groupbylist) {
		List<OrderByElement> result = new ArrayList<>();
		for (Expression o:groupbylist){
			OrderByElement obe = new OrderByElement();
			obe.setExpression(o);
			result.add(obe);
		}
		return result;
	}

	private static StringValue splitChar = new StringValue("#");

	private SelectExpressionItem makeSelectItem(List<Expression> groupbylist) {
		Function func = new Function();
		ExpressionList exps = new ExpressionList();
		
		List<Expression> explist = new ArrayList<>();
		for (Expression groupitem:groupbylist){
			explist.add(groupitem);
			explist.add(splitChar);
		}
		exps.setExpressions(explist);
		func.setParameters(exps);
		func.setName("concat");
		
		SelectExpressionItem item = new SelectExpressionItem();
		item.setExpression(func);
		item.setAlias(new Alias("_GROUP_BY_AUTOADD_"));
		return item;
	}


	



}
