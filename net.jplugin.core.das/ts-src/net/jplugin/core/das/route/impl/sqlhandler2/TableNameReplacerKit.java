package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.route.api.TablesplitException;
import net.sf.jsqlparser.expression.Alias;
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
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

public class TableNameReplacerKit {
	public static void handleDelete(Delete stmt,String toTableName) {
		//如果有别名直接返回，否则处理where
		Table tb = stmt.getTable();
		String fromTableName = tb.getName();
		tb.setName(toTableName);
		Alias alais = tb.getAlias();
		if (alais!=null && StringKit.isNotNull(alais.getName())){
			return;
		}
		ChangeWhereClauseTableNameVisitor visitor = new ChangeWhereClauseTableNameVisitor();
		visitor.handle(stmt.getWhere(),fromTableName,toTableName);
	}

	public static void handleUpdate(Update stmt,String toTableName) {
		//如果有别名直接返回，否则处理where
		Table tb = stmt.getTables().get(0);
		String fromTableName = tb.getName();
		tb.setName(toTableName);
		Alias alais = tb.getAlias();
		if (alais!=null && StringKit.isNotNull(alais.getName())){
			return;
		}
		ChangeWhereClauseTableNameVisitor visitor = new ChangeWhereClauseTableNameVisitor();
		visitor.handle(stmt.getWhere(),fromTableName,toTableName);
	}

	//这里处理的是innerSelect
	public static void handleSelect(PlainSelect ps,String toTableName) {
		//如果有别名直接返回，否则处理where
		Table tb = (Table)ps.getFromItem();
		String fromTableName = tb.getName();
		tb.setName(toTableName);
		Alias alais = tb.getAlias();
		if (alais!=null && StringKit.isNotNull(alais.getName())){
			return;
		}
		ChangeWhereClauseTableNameVisitor visitor = new ChangeWhereClauseTableNameVisitor();
		visitor.handle(ps.getWhere(),fromTableName,toTableName);
	}

	
	static class ChangeWhereClauseTableNameVisitor implements ExpressionVisitor, ItemsListVisitor{

		private String fromTableName;
		private String toTableName;

		public void handle(Expression exp, String fname, String tname) {
			this.fromTableName = fname;
			this.toTableName = tname;
			if (exp!=null)
				exp.accept(this);
		}

		@Override
		public void visit(Column tableColumn) {
			Table tb = tableColumn.getTable();
			if (tb!=null && fromTableName.equalsIgnoreCase(tb.getName())){
				tb.setName(toTableName);
			}
		}

		
		@Override
		public void visit(BitwiseRightShift aThis) {
			visitExpressions(aThis.getLeftExpression(),aThis.getRightExpression());
		}

		@Override
		public void visit(BitwiseLeftShift aThis) {
			visitExpressions(aThis.getLeftExpression(),aThis.getRightExpression());
		}

		@Override
		public void visit(NullValue nullValue) {
		}

		@Override
		public void visit(Function function) {
			ExpressionList paras = function.getParameters();
			if (paras!=null){
				List<Expression> exps = paras.getExpressions();
				if (exps!=null){
					for (Expression exp:exps){
						exp.accept(this);
					}
				}
			}
			NamedExpressionList np = function.getNamedParameters();
			if (np!=null){
				if (np.getExpressions()!=null){
					List<Expression> exps = np.getExpressions();
					if (exps!=null){
						for (Expression exp:exps){
							exp.accept(this);
						}
					}
				}
			}
		}

		@Override
		public void visit(SignedExpression signedExpression) {
			signedExpression.getExpression().accept(this);
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
		public void visit(Parenthesis parenthesis) {
		}

		@Override
		public void visit(StringValue stringValue) {
		}

		@Override
		public void visit(Addition addition) {
			visitExpressions(addition.getLeftExpression(),addition.getRightExpression());
		}
		
		void visitExpressions(Expression... exps){
			for (Expression exp:exps){
				if (exp!=null){
					exp.accept(this);
				}
			}
		}

		@Override
		public void visit(Division ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(Multiplication ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(Subtraction ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(AndExpression ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(OrExpression ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(Between ex) {
			visitExpressions(ex.getLeftExpression(),ex.getBetweenExpressionStart(),ex.getBetweenExpressionEnd());
		}

		@Override
		public void visit(EqualsTo ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(GreaterThan ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(GreaterThanEquals ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
			
		}

		@Override
		public void visit(InExpression ex) {
			visitExpressions(ex.getLeftExpression());
			ItemsList items = ex.getRightItemsList();
			if (items!=null)
				items.accept(this);
		}

		@Override
		public void visit(IsNullExpression ex) {
			visitExpressions(ex.getLeftExpression());
		}

		@Override
		public void visit(LikeExpression ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(MinorThan ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(MinorThanEquals ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(NotEqualsTo ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}


		@Override
		public void visit(SubSelect subSelect) {
			SelectBody body = subSelect.getSelectBody();
			if (body instanceof PlainSelect){
				((PlainSelect)body).getWhere().accept(this);
			}else throw new TablesplitException("not support."+body.getClass().getName());
		}

		@Override
		public void visit(CaseExpression ex) {
			visitExpressions(ex.getSwitchExpression(),ex.getElseExpression());
			List<WhenClause> wcs = ex.getWhenClauses();
			if (wcs!=null && wcs.size()>0)
			for (WhenClause wc :wcs){
				wc.accept(this);
			}
		}

		@Override
		public void visit(WhenClause wc) {
			visitExpressions(wc.getWhenExpression(),wc.getThenExpression());
		}

		@Override
		public void visit(ExistsExpression ex) {
			visitExpressions(ex.getRightExpression());
		}

		@Override
		public void visit(AllComparisonExpression ex) {
			SubSelect ss = ex.getSubSelect();
			if (ss==null) return;

			SelectBody body = ss.getSelectBody();
			if (body instanceof PlainSelect){
				((PlainSelect)body).getWhere().accept(this);
			}else throw new TablesplitException("not support."+body.getClass().getName());
		}

		@Override
		public void visit(AnyComparisonExpression anyComparisonExpression) {
			SubSelect ss = anyComparisonExpression.getSubSelect();
			if (ss==null) return;

			SelectBody body = ss.getSelectBody();
			if (body instanceof PlainSelect){
				((PlainSelect)body).getWhere().accept(this);
			}else throw new TablesplitException("not support."+body.getClass().getName());
		}

		@Override
		public void visit(Concat concat) {
			visitExpressions(concat.getLeftExpression(),concat.getRightExpression());
		}

		@Override
		public void visit(Matches ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(BitwiseAnd ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(BitwiseOr ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(BitwiseXor ex) {
			visitExpressions(ex.getLeftExpression(),ex.getRightExpression());
		}

		@Override
		public void visit(CastExpression cast) {
			cast.getLeftExpression().accept(this);
		}

		@Override
		public void visit(Modulo modulo) {
		}

		@Override
		public void visit(AnalyticExpression aexpr) {
			throw new TablesplitException("not support");
		}

		@Override
		public void visit(ExtractExpression eexpr) {
			eexpr.getExpression().accept(this);
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
			throw new TablesplitException("not support");
		}

		@Override
		public void visit(MySQLGroupConcat groupConcat) {
			throw new TablesplitException("not support");
		}

		@Override
		public void visit(ValueListExpression valueList) {
			for (Expression ex:valueList.getExpressionList().getExpressions()){
				ex.accept(this);
			}
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
			visitExpressions(aThis.getExpression());
		}

		@Override
		public void visit(NextValExpression aThis) {
		}

		@Override
		public void visit(CollateExpression aThis) {
			aThis.getLeftExpression().accept(this);
		}

		@Override
		public void visit(SimilarToExpression aThis) {
			visitExpressions(aThis.getLeftExpression(),aThis.getRightExpression());
		}

		@Override
		public void visit(ExpressionList expressionList) {
			for (Expression ex:expressionList.getExpressions()){
				ex.accept(this);
			}
		}

		@Override
		public void visit(NamedExpressionList namedExpressionList) {
			for (Expression ex:namedExpressionList.getExpressions()){
				ex.accept(this);
			}
		}

		@Override
		public void visit(MultiExpressionList multiExprList) {
			List<ExpressionList> lists = multiExprList.getExprList();
			for (ExpressionList l:lists){
				for (Expression ex:l.getExpressions()){
					ex.accept(this);
				}
			}
		}
	}
}
