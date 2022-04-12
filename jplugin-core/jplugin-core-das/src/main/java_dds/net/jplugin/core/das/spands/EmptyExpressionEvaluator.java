package net.jplugin.core.das.spands;

import java.util.Stack;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
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
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
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

public class EmptyExpressionEvaluator implements ExpressionVisitor {
	private static final String NOT_SUPPORT = "not support";
	Stack<Object> valueStack;
	
	@Override
	public void visit(BitwiseRightShift aThis) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(BitwiseLeftShift aThis) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(NullValue nullValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(Function function) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(SignedExpression signedExpression) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(JdbcNamedParameter jdbcNamedParameter) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(DoubleValue doubleValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(LongValue longValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(HexValue hexValue) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(DateValue dateValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(TimeValue timeValue) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(TimestampValue timestampValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(StringValue stringValue) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(Addition addition) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(Division division) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Multiplication multiplication) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Subtraction subtraction) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(AndExpression andExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(OrExpression orExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Between between) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(EqualsTo equalsTo) {
		equalsTo.getLeftExpression();

	}

	@Override
	public void visit(GreaterThan greaterThan) {
		
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(InExpression inExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(LikeExpression likeExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(MinorThan minorThan) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Column tableColumn) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(SubSelect subSelect) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(CaseExpression caseExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(WhenClause whenClause) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Concat concat) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(Matches matches) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(CastExpression cast) {
		throw new RuntimeException(NOT_SUPPORT);
	}

	@Override
	public void visit(Modulo modulo) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(AnalyticExpression aexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(ExtractExpression eexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(IntervalExpression iexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(OracleHierarchicalExpression oexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(RegExpMatchOperator rexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(JsonExpression jsonExpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(JsonOperator jsonExpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(RegExpMySQLOperator regExpMySQLOperator) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(UserVariable var) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(NumericBind bind) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(KeepExpression aexpr) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(MySQLGroupConcat groupConcat) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(ValueListExpression valueList) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(RowConstructor rowConstructor) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(OracleHint hint) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(TimeKeyExpression timeKeyExpression) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(DateTimeLiteralExpression literal) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(NotExpression aThis) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(NextValExpression aThis) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(CollateExpression aThis) {
		throw new RuntimeException(NOT_SUPPORT);

	}

	@Override
	public void visit(SimilarToExpression aThis) {
		throw new RuntimeException(NOT_SUPPORT);

	}

}
