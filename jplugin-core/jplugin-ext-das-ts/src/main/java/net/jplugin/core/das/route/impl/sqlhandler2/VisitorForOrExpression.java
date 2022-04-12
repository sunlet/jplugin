package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;

public class VisitorForOrExpression {


	private List<Expression> unionExpressions=new ArrayList();
	
	/**
	 * 找到不是OR的节点，这些节点是并集关系
	 * @return
	 */
	public void travelUnionExpressions(Expression exp) {
		if (exp instanceof OrExpression){
			Expression l = ((OrExpression)exp).getLeftExpression();
			Expression r = ((OrExpression)exp).getRightExpression();
			travelUnionExpressions(l);
			travelUnionExpressions(r);
		}else if (exp instanceof Parenthesis){
			travelUnionExpressions(((Parenthesis)exp).getExpression());
		}else{
			unionExpressions.add(exp);
		}
	}
	
	public List<Expression> get(){
		return this.unionExpressions;
	}
	
	public void clear(){
		this.unionExpressions.clear();
	}

}
