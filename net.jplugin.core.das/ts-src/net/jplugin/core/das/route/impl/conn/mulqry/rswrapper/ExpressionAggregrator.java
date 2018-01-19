package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import net.jplugin.core.das.route.api.AggFunctionEvalueContext;
import net.jplugin.core.das.route.api.IAggregationFunctionHandler;
import net.jplugin.core.das.route.function.FunctionHandlerManager;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ExpressionAggregrator {

	private SelectExpressionItem selectItem;
	private AggFunctionEvalueContext ctx=new AggFunctionEvalueContext();
	private IAggregationFunctionHandler aggFunction;
	/**
	 * <PRE>
	 * 对SelectItem的要求:
	 * 1.根节点不是聚集函数，此时下层也都不能包含聚集函数。比如  f1 是合法的，但是 sum(f1)+1 是不合法的。 
	 * 2.根节点是聚集函数，里面参数等都不是聚集函数。比如： sum(f1)合法 ，但是 sum(sum(f1)+5)是不合法的。
	 * 
	 * select g,sum(f1) from t group by g 
	 * 字段g满足条件1，sum(f1)满足条件2
	 * 
	 * select g,sum(f1),x from t group by g
	 * 这里字段x也满足条件1，这种sql在mysql中合法的，x随机一条
	 * 
	 * 算法：
	 * 对于情况1：
	 * </PRE>
	 * @param si
	 */
	public ExpressionAggregrator(SelectItem si) {
		if (si instanceof SelectExpressionItem){
			this.selectItem = (SelectExpressionItem) si;
			
			Expression exp = selectItem.getExpression();
			if (exp instanceof Function){
				//看看是不是aggfunction
				String functionName = ((Function)exp).getName().toUpperCase();
				IAggregationFunctionHandler fun = FunctionHandlerManager.INSTANCE.getAggFunctionHandler(functionName);
				if (fun!=null){
					this.aggFunction = fun;
				}
			}
		}else{
			throw new RuntimeException("select item must be a Expression. "+si.toString());
		}
	}

	public void aggrateItem(Object v,int rowCntForCurentValue) {
		if (this.aggFunction!=null){
			//计算聚集函数
			this.aggFunction.aggrate(ctx, v, rowCntForCurentValue);
		}else{
			//只计算一次
			if (!ctx.containAttribute("FIRST_ROW")){
				ctx.setAttribute("FIRST_ROW",v);
			}
		}
	}

	public Object getResult() {
		if (this.aggFunction!=null){
			//计算聚集函数
			return this.aggFunction.getResult(ctx);
		}else{
			//只计算一次
			if (!ctx.containAttribute("FIRST_ROW")){
				throw new RuntimeException("Can't find the first row attribute.");
			}else{
				return ctx.getAttribute("FIRST_ROW");
			}
		}
	}
	
	public void resetState() {
		ctx.clear();
	}
}
