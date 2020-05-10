package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.impl.util.FunctionEvalueKit;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NextValExpression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

public class VisitorForAndExpression implements ExpressionVisitor {

//	private KeyFilter keyFilter;
//	private List<Expression> keyExpressions;
	private String keyColumnName;
	private List<Object> parameters;
	Object left = null;
	Object right = null;
	Object eq = null;
	List<Object> inList = null;
	
	//计算的同时得到OrRoots
	List<Expression> otherIntersectExpressions = null;


	public VisitorForAndExpression(String keyColName, List<Object> aParameters) {
		this.keyColumnName = keyColName;
		this.parameters = aParameters;
	}
	
	/**
	 * EQ优先，IN其次，BETWEEN再次。如果都没有，则返回null，而不是返回ALL。
	 * @return
	 */
	public RouterKeyFilter getKnownFilter() {
		if (eq!=null) 
			return new RouterKeyFilter(Operator.EQUAL, new Object[]{eq});
		
		if (inList!=null && !inList.isEmpty())
			return new RouterKeyFilter(Operator.IN, inList.toArray(new Object[inList.size()]));
		
		if (left!=null || right!=null){
			return new RouterKeyFilter(Operator.BETWEEN, new Object[]{left,right});
		}
		return null;
	}
	/**
	 * 获取Or节点，和现有KnownFilter是交集关系
	 * @return
	 */
	public List<Expression> getOtherIntersectExpressions(){
		return this.otherIntersectExpressions;
	}
	
	/**
	 * 清除现有内容，但是保留构造函数传入的内容
	 */
	public void clear(){
		left =null;
		right = null;
		eq = null;
		inList = null;
	}


	@Override
	public void visit(AndExpression andExpression) {
		andExpression.getLeftExpression().accept(this);
		andExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {
		// ignore
		if (this.otherIntersectExpressions==null)
			this.otherIntersectExpressions = new ArrayList<>();
		this.otherIntersectExpressions.add(orExpression);
	}


	@Override
	public void visit(Parenthesis parenthesis) {
//		if (parenthesis.isNot()){
//			//ignore
//		}
		parenthesis.getExpression().accept(this);
	}
	
	@Override
	public void visit(Between e) {
		checkAndAddLeft(e.getLeftExpression(), e.getBetweenExpressionStart());
		checkAndAddRight(e.getLeftExpression(), e.getBetweenExpressionEnd());
	}

	@Override
	public void visit(EqualsTo e) {
		checkAndAddEQ(e.getLeftExpression(),e.getRightExpression());
	}

	@Override
	public void visit(GreaterThan e) {
		checkAndAddLeft(e.getLeftExpression(),e.getRightExpression());
	}

	@Override
	public void visit(GreaterThanEquals e) {
		checkAndAddLeft(e.getLeftExpression(),e.getRightExpression());
	}

	@Override
	public void visit(InExpression e) {
		checkAndAddIn(e.getLeftExpression(),e.getRightItemsList());
	}
	@Override
	public void visit(MinorThan e) {
		checkAndAddRight(e.getLeftExpression(),e.getRightExpression());
	}
	@Override
	public void visit(MinorThanEquals e) {
		checkAndAddRight(e.getLeftExpression(),e.getRightExpression());
	}
	@Override
	public void visit(Function function) {
	}


	private void checkAndAddIn(Expression leftExpression, ItemsList rightItemsList) {
		//因为都是and条件，如果已经有一个了，新的可以不判断了
		if (inList!=null) 
			return;
		
		if (checkLeftColunn(leftExpression)){
			if (rightItemsList instanceof ExpressionList){
				List<Expression> expressions = ((ExpressionList)rightItemsList).getExpressions();
				
				//只要有一个数据不满足，则忽略该条件；所以要先全部取出来
				List<Object> values = new ArrayList();
				for (Expression item:expressions){
					Object v = tryComputeValue(item);
					if (v!=null) 
						values.add(v);
					else 
						return;
				}
				inList = values;
			}
		}
	}
	private Object tryComputeValue(Expression item) {
		return FunctionEvalueKit.evalueNonStrickly(item, parameters);
	}
//	private Object tryComputeValue(Expression item) {
//		Value v = new Value();
//		Object constv = ConstValueExpressionKit.tryGetConstValue(item);
//		
//		if (constv!=null){
//			v.isParamedKey = false;
//			v.keyConstValue = constv;
//			return v;
//		}
//		
//		if (item instanceof JdbcParameter){
//			v.isParamedKey = true;
//			v.keyParamIndex = ((JdbcParameter)item).getIndex()-1;
//			return v;
//		}
//		
//		if (item instanceof Function){
//			Object o = FunctionEvalueManager.evalueNonStrickly(item,this.parameters);
//			if (o!=null){
//				v.isParamedKey = false;
//				v.keyConstValue = o;
//				return v;
//			}else{
//				return null;
//			}
//		}
//		
//		return null;
//	}

	private boolean checkLeftColunn(Expression leftExpression) {
		if (! (leftExpression instanceof Column)) 
			return false;
		
		String name = ((Column)leftExpression).getColumnName();
		return this.keyColumnName.equals(name);
	}

	private void checkAndAddRight(Expression leftExpression, Expression rightExpression) {
		if (right !=null ) 
			return;
		
		if (checkLeftColunn(leftExpression)){
			Object v = tryComputeValue(rightExpression);
			if (v!=null){
				right = v;
			}
		}
	}
	private void checkAndAddEQ(Expression leftExpression, Expression rightExpression) {
		if (eq !=null ) 
			return;
		
		if (checkLeftColunn(leftExpression)){
			Object v = tryComputeValue(rightExpression);
			if (v!=null){
				eq = v;
			}
		}
	}

	private void checkAndAddLeft(Expression leftExpression, Expression rightExpression) {
		if (left !=null ) 
			return;
		
		if (checkLeftColunn(leftExpression)){
			Object v = tryComputeValue(rightExpression);
			if (v!=null){
				left = v;
			}
		}
	}

	@Override
	public void visit(NullValue nullValue) {
	}


	@Override
	public void visit(SignedExpression signedExpression) {
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
	}

	@Override
	public void visit(JdbcNamedParameter jdbcNamedParameter) {
	}

	@Override
	public void visit(DoubleValue doubleValue) {
	}

	@Override
	public void visit(LongValue longValue) {
	}

	@Override
	public void visit(HexValue hexValue) {
	}

	@Override
	public void visit(DateValue dateValue) {
	}

	@Override
	public void visit(TimeValue timeValue) {
	}

	@Override
	public void visit(TimestampValue timestampValue) {
	}


	@Override
	public void visit(StringValue stringValue) {
	}

	@Override
	public void visit(Addition addition) {
	}

	@Override
	public void visit(Division division) {
	}

	@Override
	public void visit(Multiplication multiplication) {
	}

	@Override
	public void visit(Subtraction subtraction) {
	}


	@Override
	public void visit(IsNullExpression isNullExpression) {
	}

	@Override
	public void visit(LikeExpression likeExpression) {
	}



	@Override
	public void visit(NotEqualsTo notEqualsTo) {
	}

	@Override
	public void visit(Column tableColumn) {
	}

	@Override
	public void visit(SubSelect subSelect) {
	}

	@Override
	public void visit(CaseExpression caseExpression) {
	}

	@Override
	public void visit(WhenClause whenClause) {
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
	}

	@Override
	public void visit(Concat concat) {
	}

	@Override
	public void visit(Matches matches) {
	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
	}

	@Override
	public void visit(CastExpression cast) {
	}

	@Override
	public void visit(Modulo modulo) {
	}

	@Override
	public void visit(AnalyticExpression aexpr) {
	}

//	@Override
//	public void visit(WithinGroupExpression wgexpr) {
//	}

	@Override
	public void visit(ExtractExpression eexpr) {
	}

	@Override
	public void visit(IntervalExpression iexpr) {
	}

	@Override
	public void visit(OracleHierarchicalExpression oexpr) {
	}

	@Override
	public void visit(RegExpMatchOperator rexpr) {
	}

	@Override
	public void visit(JsonExpression jsonExpr) {
	}

	@Override
	public void visit(JsonOperator jsonExpr) {
	}

	@Override
	public void visit(RegExpMySQLOperator regExpMySQLOperator) {
	}

	@Override
	public void visit(UserVariable var) {
	}

	@Override
	public void visit(NumericBind bind) {
	}

	@Override
	public void visit(KeepExpression aexpr) {
	}

	@Override
	public void visit(MySQLGroupConcat groupConcat) {
	}

	@Override
	public void visit(RowConstructor rowConstructor) {
	}

	@Override
	public void visit(OracleHint hint) {
	}

	@Override
	public void visit(TimeKeyExpression timeKeyExpression) {
	}

	@Override
	public void visit(DateTimeLiteralExpression literal) {
	}

	@Override
	public void visit(NotExpression aThis) {
		aThis.getExpression().accept(this);
	}

	@Override
	public void visit(BitwiseRightShift aThis) {
	}

	@Override
	public void visit(BitwiseLeftShift aThis) {
	}

	@Override
	public void visit(ValueListExpression valueList) {
	}

	@Override
	public void visit(NextValExpression aThis) {
	}

	@Override
	public void visit(CollateExpression aThis) {
	}

	@Override
	public void visit(SimilarToExpression aThis) {
	}


}
