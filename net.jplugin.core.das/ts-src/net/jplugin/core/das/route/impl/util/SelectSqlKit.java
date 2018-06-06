package net.jplugin.core.das.route.impl.util;

import java.util.List;

import net.jplugin.core.das.route.api.RouterException;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;

public class SelectSqlKit {

	public static PlainSelect getMostInnerSelect(Select select,String sqlString) {
		SelectBody body = select.getSelectBody();
		PlainSelect ps = SelectSqlKit.getMostInnerSelect(body,sqlString);
		PlainSelect innerSelect = getMostInnerSelect(ps,sqlString);
		return innerSelect;
	}
	/**
	 * <PRE>
	 * sql语句必须是 select xxx,xxx from table 
	 * 或者 
	 * select xxx from (select xxx) 样式的，此时from后面只能有一个，并且是子查询
	 * </PRE>
	 * 
	 * @param sb
	 * @return
	 */
	public static PlainSelect getMostInnerSelect(SelectBody sb,String sqlString) {
		// 检查类型
		if (!(sb instanceof PlainSelect))
			throw new RouterException("only support plain select ." + sqlString);
//
//		去掉，运气好的时候可以用。
//		// 检查join
//		List<Join> joins = ((PlainSelect) sb).getJoins();
//		if (joins != null && !joins.isEmpty()) {
//			throw new RouterException("join is not supported." + sqlString);
//		}

		// 递归判断或者返回结果
		FromItem fromItem = ((PlainSelect) sb).getFromItem();
		if (fromItem instanceof Table) {
			return (PlainSelect) sb;
		} else if (fromItem instanceof SubSelect) {
			//判断不能有order
			List<OrderByElement> order = ((PlainSelect) sb).getOrderByElements();
			if (order!=null && order.size()>0)
				throw new RouterException("outer sql must not with orderby."+sqlString);
			SubSelect ss = (SubSelect) fromItem;
			return getMostInnerSelect(ss.getSelectBody(),sqlString);
		} else {
			throw new RouterException("from item must be Table or SubSelect." + sqlString);
		}
	}
}
