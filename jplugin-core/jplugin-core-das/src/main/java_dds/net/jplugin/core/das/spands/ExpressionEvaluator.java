package net.jplugin.core.das.spands;

import java.sql.JDBCType;
import java.util.Stack;

import net.jplugin.common.kits.AssertKit;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;

public class ExpressionEvaluator extends EmptyExpressionEvaluator {

	Stack<Value> valueStack;
	public static class Value{
		JDBCType type;
		Object   val;
		
		public JDBCType getType() {
			return type;
		}

		public Object getVal() {
			return val;
		}

		public static Value with(JDBCType t,Object v) {
			Value o = new Value();
			o.type = t;
			o.val = v;
			return o;
		}
	}
	
	public static Value evaluate(EvaluatContext ctx,Expression exp) {
		ExpressionEvaluator ee= new ExpressionEvaluator();
		exp.accept(ee);
		AssertKit.assertEqual(ee.valueStack.size(),1);
		return ee.valueStack.pop();
	}
	@Override
	public void visit(NullValue nullValue) {
		valueStack.push(Value.with(JDBCType.NULL, null));
	}

	@Override
	public void visit(LongValue longValue) {
		valueStack.push(Value.with(JDBCType.NUMERIC, longValue.getValue()));
	}

	@Override
	public void visit(DateValue dateValue) {
		valueStack.push(Value.with(JDBCType.DATE, dateValue.getValue()));
	}

	@Override
	public void visit(TimeValue timeValue) {
		valueStack.push(Value.with(JDBCType.TIME, timeValue.getValue()));
	}

	@Override
	public void visit(TimestampValue timestampValue) {
		valueStack.push(Value.with(JDBCType.TIMESTAMP, timestampValue.getValue()));
	}

	@Override
	public void visit(StringValue stringValue) {
		valueStack.push(Value.with(JDBCType.CHAR, stringValue.getValue()));
	}

	@Override
	public void visit(Addition addition) {
		addition.getLeftExpression().accept(this);
		addition.getRightExpression().accept(this);
		Value vr = valueStack.pop();
		Value vl = valueStack.pop();
		valueStack.push(add(vl,vr));
	}

	private Value add(Value vl, Value vr) {
//		return SqlNumricOperationKit.add(vl,vr);
		return null;
	}
	@Override
	public void visit(Division division) {
		// TODO Auto-generated method stub
		super.visit(division);
	}

	@Override
	public void visit(Subtraction subtraction) {
		// TODO Auto-generated method stub
		super.visit(subtraction);
	}

	@Override
	public void visit(AndExpression andExpression) {
		// TODO Auto-generated method stub
		super.visit(andExpression);
	}

	@Override
	public void visit(OrExpression orExpression) {
		// TODO Auto-generated method stub
		super.visit(orExpression);
	}

	@Override
	public void visit(Between between) {
		// TODO Auto-generated method stub
		super.visit(between);
	}

	@Override
	public void visit(EqualsTo equalsTo) {
		// TODO Auto-generated method stub
		super.visit(equalsTo);
	}

	@Override
	public void visit(InExpression inExpression) {
		// TODO Auto-generated method stub
		super.visit(inExpression);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		// TODO Auto-generated method stub
		super.visit(notEqualsTo);
	}

	@Override
	public void visit(Column tableColumn) {
		// TODO Auto-generated method stub
		super.visit(tableColumn);
	}

	@Override
	public void visit(CastExpression cast) {
		// TODO Auto-generated method stub
		super.visit(cast);
	}

	@Override
	public void visit(ValueListExpression valueList) {
		// TODO Auto-generated method stub
		super.visit(valueList);
	}

	@Override
	public void visit(NotExpression aThis) {
		// TODO Auto-generated method stub
		super.visit(aThis);
	}

}
