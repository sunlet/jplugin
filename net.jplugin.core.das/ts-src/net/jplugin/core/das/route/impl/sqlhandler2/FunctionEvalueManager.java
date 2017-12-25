package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.List;

import net.jplugin.core.das.route.api.IFunctionHandler;
import net.jplugin.core.das.route.function.FunctionHandlerManager;
import net.jplugin.core.das.route.impl.util.ConstValueExpressionKit;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

public class FunctionEvalueManager {

	public static Object evalueNonStrickly(Expression expression, List<Object> parameters) {
		//求参数值
		if (expression instanceof JdbcParameter){
			return parameters.get((((JdbcParameter)expression).getIndex()-1));
		}
		
		//求常量值
		Object constV = ConstValueExpressionKit.tryGetConstValue(expression);
		if (constV!=null){
			return constV;
		}
		
		//求函数值
		Function f;
		if (expression instanceof Function){
			f = (Function) expression;
		}else{
			return null;
		}
		
		String name = f.getName();
		if (name==null) return null;
		
		name = name.toUpperCase();
		IFunctionHandler handler = FunctionHandlerManager.INSTANCE.getFunctionHandler(name);
		if (handler==null){
			return null;
		}
		
		Object[] vals=null;
		ExpressionList funcParams = f.getParameters();
		if (funcParams!=null){
			List<Expression> exps = funcParams.getExpressions();
			if (exps!=null){
				vals = new Object[exps.size()];
				for (int i=0;i<exps.size();i++){
					Object v = evalueNonStrickly(exps.get(i),parameters);
					if (v==null) 
						return null;//碰到null参数，早点儿退出
					vals[i] = v;
				}
			}
		}
		
		if (vals==null){
			vals = new Object[0];
		}
		
		return handler.getResult(vals);
	}

}
