package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta;
import net.jplugin.core.das.route.impl.util.SelectSqlKit;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class SelectHandler2 extends AbstractCommandHandler2 {

	private PlainSelect innerSelect;

	@Override
	public List<RouterKeyFilter> getKeyFilter() {
		Select select = (Select) this.statement;
		SelectBody body = select.getSelectBody();
		PlainSelect ps = SelectSqlKit.getMostInnerSelect(body,this.sqlString);
		this.innerSelect = ps;
		Table table = ((Table) ps.getFromItem());
		if (StringKit.isNotNull(table.getSchemaName()))
			throw new RouterException("schema must not specified." + this.sqlString);

		super.setTableName(table.getName());

		List<RouterKeyFilter> kf = null;
		Expression where = ps.getWhere();
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
//		((Table)this.innerSelect.getFromItem()).setName(nm);
		TableNameReplacerKit.handleSelect(this.innerSelect, nm);
	}
	
//	@Override
//	protected void maintainSqlMeta(Meta meta) {
////		if (isCountStar()){
////			meta.setCountStar(Meta.COUNG_STAR_YES);
////		}else{
////			meta.setCountStar(Meta.COUNG_STAR_NO);
////		}
//		
////		List<String> orderparams = getOrderParams();
////		meta.setOrderParam(orderparams);
//	}
//	
//	/**
//	 * <PRE>
//	 * sql语句必须是 select xxx,xxx from table 
//	 * 或者 
//	 * select xxx from (select xxx) 样式的，此时from后面只能有一个，并且是子查询
//	 * </PRE>
//	 * 
//	 * @param sb
//	 * @return
//	 */
//	private PlainSelect getMostInnerSelect(SelectBody sb) {
//		// 检查类型
//		if (!(sb instanceof PlainSelect))
//			throw new RouterException("only support plain select ." + sqlString);
//
//		// 检查join
//		List<Join> joins = ((PlainSelect) sb).getJoins();
//		if (joins != null && !joins.isEmpty()) {
//			throw new RouterException("join is not supported." + this.sqlString);
//		}
//
//		// 递归判断或者返回结果
//		FromItem fromItem = ((PlainSelect) sb).getFromItem();
//		if (fromItem instanceof Table) {
//			return (PlainSelect) sb;
//		} else if (fromItem instanceof SubSelect) {
//			//判断不能有order
//			List<OrderByElement> order = ((PlainSelect) sb).getOrderByElements();
//			if (order!=null && order.size()>0)
//				throw new RouterException("outer sql must not with orderby."+this.sqlString);
//			SubSelect ss = (SubSelect) fromItem;
//			return getMostInnerSelect(ss.getSelectBody());
//		} else {
//			throw new RouterException("from item must be Table or SubSelect." + this.sqlString);
//		}
//	}

//	/**
//	 * 这里判断最外层是不是count(*),count(0),count(n)
//	 * @return
//	 */
//	private boolean isCountStar() {
//		//获取Function
//		SelectBody body = ((Select)this.statement).getSelectBody();
//		List<SelectItem> items = ((PlainSelect)body).getSelectItems();
//		if(items.size()!=1) return false;
//		SelectItem onlyItem = items.get(0);
//		if (!(onlyItem instanceof SelectExpressionItem))
//			return false;
//		
//		Expression expression = ((SelectExpressionItem)onlyItem).getExpression();
//		if (!(expression instanceof Function))
//			return false;
//		
//		Function function = (Function) expression;
//		
//		//下面判断function是否满足
//		if (!"count".equalsIgnoreCase(function.getName())) {
//			return false;
//		}
//		ExpressionList functionParams = function.getParameters();
//		if (functionParams == null && function.isAllColumns())
//			return true;
//		List<Expression> expressions = functionParams.getExpressions();
//		if (expressions == null)
//			return false;
//		if (expressions.size() == 0 && function.isAllColumns())
//			return true;
//		if (expressions.size() > 1)
//			return false;
//		Expression functionParam = expressions.get(0);
//		if (!(functionParam instanceof LongValue))
//			return false;
//		else
//			return true;
//	}

//	private List<String> getOrderParams() {
//		List<OrderByElement> orderby = this.innerSelect.getOrderByElements();
//		if (orderby==null || orderby.size()==0) 
//			return null;
//		ArrayList<String> ret = new ArrayList<>();
//		for (int i=0;i<orderby.size();i++){
//			if (i!=0) ret.add(",");
//			OrderByElement obe = orderby.get(i);
//			String[] arrs = obe.toString().split(" ");
//			for (String e:arrs){
//				ret.add(e);
//			}
//		}
//		return ret;
//	}
	

//	@Override
//	public SqlHandleResult getHandleResult(KeyFilter kf) {
//		SqlHandleResult result = new SqlHandleResult();
//		if (kf!=null){
//			return getResultForSelectFromKeyFilter(kf);
//		}else{
//			if (isSpanAll()){
//				return getSpanAllResult();
//			}else{
//				throw new RouterException("Key filter not found  and  spanall not indicted."+this.sqlString);
//			}
//		}
//	}
	
//
//	private SqlHandleResult getSpanAllResult() {
//		 CombinedSqlParser.Meta meta = new
//		 net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta();
//		 meta.setSourceTb(tableName);
//		 meta.setDataSourceInfos(TsAlgmManager.getDataSourceInfos(this.connetion.getDataSource(),tableName));
//		 meta.setOrderParam(getOrderParams());
//		 meta.setCountStar(isCountStar()? 1:0);
//		 String newSql = CombinedSqlParser.combine(this.sqlString, meta);
//		 SqlHandleResult result = new SqlHandleResult();
//		 result.setResultSql(newSql);
//		 result.setTargetDataSourceName(CombinedSqlParser.SPANALL_DATASOURCE);
//		 return result;
//	}

	
	
//	
//	private boolean isSpanAll() {
//		OracleHint hint = this.innerSelect.getOracleHint();
//		if (hint == null)
//			return false;
//		return "spanall".equalsIgnoreCase(hint.getValue());
//	}

//	private SqlHandleResult getResultForSelectFromKeyFilter(KeyFilter kf) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
	//
	// @Override
	// public final SqlHandleResult handle(){
	// KeyFilter keyFilter = getKeyResult();
	// if (keyFilter!=null){
	// return makeFromKeyFilter(keyFilter);
	// }else{
	// if (supportSpanAll()&& isSpanAll()){
	// return makeSpanAllResult();
	// }else{
	// //语法错误
	// throw new RuntimeException("Can't find key info for router
	// check."+statement.toString());
	// }
	// }
	// }
	// /**
	// * 解析获得Key参数信息
	// * @return
	// */
	// public abstract KeyFilter getKeyResult();
	//
	// /**
	// * 是否支持跨所有表的操作。目前只有select支持。
	// * @return
	// */
	// public abstract boolean supportSpanAll();
	//
	// /**
	// * 当前是否可以跨所有表操作
	// * @return
	// */
	// public abstract boolean isSpanAll();
	//
	//
	//
	// private SqlHandleResult makeSpanAllResult() {
	// String tableName = getTableName();
	// CombinedSqlParser.Meta meta = new
	// net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta();
	// meta.setSourceTb(tableName);
	// meta.setDataSourceInfos(TsAlgmManager.getDataSourceInfos(this.connetion.getDataSource(),tableName));
	// meta.setOrderParam(getOrderParam(walker));
	// meta.setCountStar(getCountStar(walker)? 1:0);
	// String newSql = CombinedSqlParser.combine(sql, meta);
	// SqlHandleResult result = new SqlHandleResult();
	// result.setResultSql(newSql);
	// result.setTargetDataSourceName(CombinedSqlParser.SPANALL_DATASOURCE);
	// return result;
	// }

}
