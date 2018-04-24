package net.jplugin.core.mtenant.handler2;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.route.impl.parser.SqlStrLexerToolNew;
import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
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
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.WithinGroupExpression;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
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
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Commit;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;

public class SqlHandlerVisitorForMixed
		implements StatementVisitor, SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor {

	static String tenantColumnName;
	public static void init(){
		tenantColumnName = ConfigFactory.getStringConfigWithTrim("mtenant.field");
		if (StringKit.isNull(tenantColumnName)){
			tenantColumnName = "mtenant_id";
		}
		PluginEnvirement.getInstance().getStartLogger().log("**mtenant.field="+tenantColumnName);
	}
	
	SqlRefactor sqlRefactor;
	String schemaName;
	String tenantId;//是否有值决定是否处理字段

	public SqlHandlerVisitorForMixed(String aSchemaName){
		this(aSchemaName,null);
	}
	public SqlHandlerVisitorForMixed(String aSchemaName,String tid){
		this.schemaName = aSchemaName;
		this.tenantId = tid;
		this.sqlRefactor = new SqlRefactor();
	}
	public String handle(String sql) throws JSQLParserException{
		//引号里面的分号目前会报错！！
		if (sql.indexOf(';') <0){
			return handleOne(sql);
		}else{
			String[] arr = StringKit.splitStr(sql, ";");
			StringBuffer sb = new StringBuffer();
			for (int i=0;i<arr.length;i++){
				if (i!=0) {
					sb.append(";");
				}
				sb.append(handleOne(arr[i]));
			}
			return sb.toString();
		}
	}
	
	private String handleOne(String sql) throws JSQLParserException{
		CCJSqlParserManager pm = new CCJSqlParserManager();
    	net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
    	if (statement instanceof Select)
    		visit((Select)statement);
    	else if (statement instanceof Insert)
    		visit((Insert)statement);
    	else if (statement instanceof Update)
    		visit((Update)statement);
    	else if (statement instanceof Delete)
    		visit((Delete)statement);
    	else if (statement instanceof Replace)
    		visit((Replace)statement);
    	return statement.toString();
	}
	
	
	@Override
	public void visit(Select select) {
		//Select比较特殊，在下级方法当中处理
		select.getSelectBody().accept(this);
	}

	@Override
	public void visit(Delete delete) {
		//先处理本语句
		sqlRefactor.handleDelete(delete);
		
		if (delete.getWhere()!=null)
			delete.getWhere().accept(this);
	}

	@Override
	public void visit(Update update) {
		//先处理本语句
		sqlRefactor.handleUpdate(update);

		List<Expression> exp = update.getExpressions();
		if (exp != null)
			for (Expression e : exp) {
				e.accept(this);
			}
		if (update.getWhere()!=null)
			update.getWhere().accept(this);
	}

	@Override
	public void visit(Insert insert) {
		//先处理本语句
		sqlRefactor.handleInsert(this,insert);
		if (insert.getSelect()!=null)
			insert.getSelect().accept(this);
		
		//好像新版本是不再需要了，因为子查询有属性Select，加上也没关系。Replace类似的地方需要
		ItemsList itemlist = insert.getItemsList();
		if (itemlist instanceof SubSelect){
			((SubSelect)itemlist).getSelectBody().accept(this);
		}
	}

	@Override
	public void visit(Replace replace) {
		//先处理本语句
		sqlRefactor.handleReplace(this,replace);
		
		//处理子查询等
		ItemsList itemlist = replace.getItemsList();
		if (itemlist instanceof SubSelect){
			((SubSelect)itemlist).getSelectBody().accept(this);
		}
	}

	@Override
	public void visit(Drop drop) {
		//先处理本语句
		sqlRefactor.handleDrop(this,drop);
	}

	@Override
	public void visit(Truncate truncate) {
		//先处理本语句
		sqlRefactor.handleTruncate(this,truncate);
		
	}

	@Override
	public void visit(CreateTable createTable) {
		//先处理本语句
		sqlRefactor.handleCreateTable(this,createTable);

	}

	@Override
	public void visit(PlainSelect plainSelect) {
		//先处理本语句
		this.sqlRefactor.handleSelect(plainSelect);
		
		//再处理其他语句
		if (plainSelect.getFromItem()!=null)
			plainSelect.getFromItem().accept(this);
		
		if (plainSelect.getJoins() != null) {
			for (Iterator joinsIt = plainSelect.getJoins().iterator(); joinsIt.hasNext();) {
				Join join = (Join) joinsIt.next();
				join.getRightItem().accept(this);
			}
		}

		if (plainSelect.getWhere() != null) {
			plainSelect.getWhere().accept(this);
		}

	}

	@Override
	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}

	@Override
	public void visit(Addition addition) {
		visitBinaryExpression(addition);
	}

	@Override
	public void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}

	@Override
	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

	@Override
	public void visit(Division division) {
		visitBinaryExpression(division);
	}


	@Override
	public void visit(EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo);
	}

	@Override
	public void visit(Function function) {
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}

	@Override
	public void visit(InExpression inExpression) {
		inExpression.getLeftExpression().accept(this);
		inExpression.getRightItemsList().accept(this);
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}


	@Override
	public void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}

	@Override
	public void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}


	@Override
	public void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	@Override
	public void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}

	@Override
	public void visit(ExpressionList expressionList) {
		for (Iterator iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
			Expression expression = (Expression) iter.next();
			expression.accept(this);
		}
	}
	
	@Override
	public void visit(CaseExpression caseExpression) {
		caseExpression.getSwitchExpression().accept(this);
		caseExpression.getElseExpression().accept(this);
		List list = caseExpression.getWhenClauses();
		if (list!=null){
			for (Object o:list){
				((WhenClause)o).accept(this);
			}
		}
	}

	@Override
	public void visit(WhenClause whenClause) {
		whenClause.getWhenExpression().accept(this);
		whenClause.getThenExpression().accept(this);
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}

	@Override
	public void visit(Concat concat) {
		visitBinaryExpression(concat);
	}

	@Override
	public void visit(Matches matches) {
		visitBinaryExpression(matches);
	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd);
	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr);
	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor);
	}

	private void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(LateralSubSelect ss) {
		ss.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(MultiExpressionList o) {
		List<ExpressionList> list = o.getExprList();
		if (list!=null){
			for (ExpressionList el:list){
				el.accept(this);
			}
		}
	}
	
	//下面方法貌似不需实现

	@Override
	public void visit(LongValue longValue) {
	}
	@Override
	public void visit(Column tableColumn) {
	}
	@Override
	public void visit(DoubleValue doubleValue) {
	}
	@Override
	public void visit(NullValue nullValue) {
	}
	@Override
	public void visit(StringValue stringValue) {
	}
	
	@Override
	public void visit(DateValue dateValue) {
	}

	@Override
	public void visit(TimestampValue timestampValue) {
	}

	@Override
	public void visit(TimeValue timeValue) {
	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
	}
	@Override
	public void visit(SignedExpression se) {
		se.getExpression().accept(this);
	}

	@Override
	public void visit(JdbcNamedParameter arg0) {
	}

	@Override
	public void visit(HexValue arg0) {
	}

	@Override
	public void visit(CastExpression arg0) {
	}

	@Override
	public void visit(Modulo arg0) {
	}

	@Override
	public void visit(AnalyticExpression arg0) {
	}

	@Override
	public void visit(WithinGroupExpression arg0) {
	}

	@Override
	public void visit(ExtractExpression ee) {
		ee.getExpression().accept(this);
	}

	@Override
	public void visit(IntervalExpression ie) {
	}

	@Override
	public void visit(OracleHierarchicalExpression arg0) {
	}

	@Override
	public void visit(RegExpMatchOperator arg0) {
	}

	@Override
	public void visit(JsonExpression arg0) {
	}

	@Override
	public void visit(JsonOperator arg0) {
	}

	@Override
	public void visit(RegExpMySQLOperator arg0) {
	}

	@Override
	public void visit(UserVariable arg0) {
	}

	@Override
	public void visit(NumericBind arg0) {
	}

	@Override
	public void visit(KeepExpression arg0) {
	}

	@Override
	public void visit(MySQLGroupConcat arg0) {
	}

	@Override
	public void visit(RowConstructor arg0) {
	}

	@Override
	public void visit(OracleHint arg0) {
	}

	@Override
	public void visit(TimeKeyExpression arg0) {
	}

	@Override
	public void visit(DateTimeLiteralExpression arg0) {
	}

	@Override
	public void visit(NotExpression arg0) {
	}

	@Override
	public void visit(ValuesList arg0) {
	}

	@Override
	public void visit(TableFunction arg0) {
	}

	@Override
	public void visit(SetOperationList sol) {
		List<SelectBody> selects = sol.getSelects();
		if (selects!=null){
			for (SelectBody s:selects){
				s.accept(this);
			}
		}
	}

	@Override
	public void visit(WithItem arg0) {
	}

	@Override
	public void visit(Commit arg0) {
	}

	@Override
	public void visit(CreateIndex arg0) {
	}

	@Override
	public void visit(CreateView arg0) {
	}

	@Override
	public void visit(AlterView arg0) {
	}

	@Override
	public void visit(Alter arg0) {
	}

	@Override
	public void visit(Statements arg0) {
	}

	@Override
	public void visit(Execute arg0) {
	}

	@Override
	public void visit(SetStatement arg0) {
	}

	@Override
	public void visit(Merge arg0) {
	}

	@Override
	public void visit(Upsert arg0) {
	}
	
	@Override
	public void visit(Table tableName) {
	}
	
	class SqlRefactor {
		public  void handleSelect(PlainSelect plainSelect) {
			//只处理简单类型
			List<String> tableList = new ArrayList<>(3);
			FromItem fromItem = plainSelect.getFromItem();
			
			if (fromItem==null) 
				return; //应该是系统sql ，比如select now()
			
			if (fromItem instanceof Table){
				String temp = handleTableNameReturnColumnPrefix((Table) fromItem);
				tableList.add(temp);
			}
			
			
			List joins = plainSelect.getJoins();
			if (joins!=null){
				for (Object join:joins){
					String temp = handleJoin((Join) join);
					if (temp!=null) tableList.add(temp);
				}
			}

			if (handleColumn()){
				plainSelect.setWhere(refactWhere(tableList, plainSelect.getWhere()));
			}
		}

		private Expression refactWhere(List<String> tableList, Expression where) {
			Expression exp = where;
			if (exp!=null) 
				exp = new Parenthesis(exp);
			for (String prefix:tableList){
				exp = computeNewExp( exp, prefix);
			}
			return exp;
		}

		private  Expression computeNewExp(Expression exp, String name) {
			//cteate new eq
			EqualsTo eq = new EqualsTo();
			eq.setLeftExpression(new Column(new Table(null,name), tenantColumnName));
			eq.setRightExpression(getTenantIdExpression());

			if (exp==null){
				exp = eq;
			}else{
				BinaryExpression ex = new AndExpression(eq, exp);
				exp = ex;	
			}
			return exp;
		}

		private  String handleJoin(Join join) {
			if (join.isSimple()){
				FromItem rightItem = join.getRightItem();
				if (rightItem instanceof Table){
					return handleTableNameReturnColumnPrefix((Table) rightItem);
				}else{
					//复杂类型，不用处理
					return null;
				}
			}else{
				//在on里面处理
				if (join.getRightItem() instanceof Table){
					String prefix = handleTableNameReturnColumnPrefix((Table) join.getRightItem());
					if (handleColumn()){
						Expression exp = join.getOnExpression();
						exp = computeNewExp( exp, prefix);
						join.setOnExpression(exp);
					}
					return null;
				}else
					return null;
			}
		}

		private String handleTableNameReturnColumnPrefix(Table tb) {
			
			if (tb.getSchemaName()!=null)
				throw new RuntimeException("In multi tenant envirment, Tables can't has schema. but find "+tb.getSchemaName()+" from table "+tb.getName());
			tb.setSchemaName(schemaName);
			
			if (tb.getAlias()!=null) 
				return tb.getAlias().getName();
			if (tb.getSchemaName()!=null)
				return tb.getSchemaName()+"."+tb.getName();
			return tb.getName();
		}

		public  void handleCreateTable(SqlHandlerVisitorForMixed sqlHandlerVisitor, CreateTable createTable) {
			// TODO Auto-generated method stub
			
		}

		public  void handleTruncate(SqlHandlerVisitorForMixed sqlHandlerVisitor, Truncate truncate) {
			// TODO Auto-generated method stub
			
		}

		public  void handleDrop(SqlHandlerVisitorForMixed sqlHandlerVisitor, Drop drop) {
			// TODO Auto-generated method stub
			
		}

		public  void handleReplace(SqlHandlerVisitorForMixed sqlHandlerVisitor, Replace insert) {
			Table table = insert.getTable();
			if (table == null) 
				throw new RuntimeException("table can't be null");
			
			handleTableNameReturnColumnPrefix(table);
			
			if (handleColumn()){
				insert.getColumns().add(new Column(new Table(), tenantColumnName));
			
				if (insert.isUseValues()){
					ExpressionList exlist = (ExpressionList) insert.getItemsList();
					handleExpressionListForInsertReplace(exlist);
				}else{
					ItemsList items = insert.getItemsList();
					if (items instanceof SubSelect ){
						SubSelect ss = (SubSelect) items;
						SelectBody body = ss.getSelectBody();
						if (body instanceof PlainSelect){
							PlainSelect ps = ((PlainSelect)body);
							handleSelectItemForInsert((PlainSelect)ps);
						}else{
							throw new RuntimeException("Not support now");
						}
					}else
						throw new RuntimeException("not support now");
				}
			}
		}

		public  void handleInsert(SqlHandlerVisitorForMixed sqlHandlerVisitor, Insert insert) {
			Table table = insert.getTable();
			if (table == null) 
				throw new RuntimeException("table can't be null");
			
			handleTableNameReturnColumnPrefix(table);
			
			if (handleColumn()){
				insert.getColumns().add(new Column(new Table(), tenantColumnName));
			
				if (insert.isUseValues()){
					ItemsList itemList = insert.getItemsList();
					handleExpressionListForInsertReplace(itemList);
				}else{
					Select select = insert.getSelect();
					SelectBody body = select.getSelectBody();
					if (body instanceof PlainSelect){
						handleSelectItemForInsert((PlainSelect) body);
					}else if (body instanceof SetOperationList){
						SetOperationList u = (SetOperationList) body;
						for (Object s:u.getSelects()){
							handleSelectItemForInsert((PlainSelect)s);
						}
					}else
						throw new RuntimeException("error gramma");
				}
			}
		}

		private void handleExpressionListForInsertReplace(ItemsList itemList) {
			if (itemList instanceof ExpressionList){
				ExpressionList exlist = (ExpressionList) itemList;
				exlist.getExpressions().add(getTenantIdExpression());
			}else if (itemList instanceof MultiExpressionList){
				MultiExpressionList mlist = (MultiExpressionList) itemList;
				for (ExpressionList l:mlist.getExprList()){
					l.getExpressions().add(getTenantIdExpression());
				}
			}
		}



		private void handleSelectItemForInsert(PlainSelect s) {
			SelectExpressionItem exi = new SelectExpressionItem();
			exi.setExpression(getTenantIdExpression());
			s.getSelectItems().add(exi);
		}

		private boolean handleColumn() {
			return StringKit.isNotNull(tenantId);
		}
		private StringValue getTenantIdExpression() {
			return new StringValue("'"+tenantId+"'");
		}
		public  void handleUpdate(Update update) {
			List<Table> tables = update.getTables();
			
			List tableList = new ArrayList(3);
			for (Table table:tables){ 
				if (table == null) 
					throw new RuntimeException("table can't be null");
				
				String prefix = handleTableNameReturnColumnPrefix(table);
				
				tableList.add(prefix);
			}
			if (handleColumn()){
				update.setWhere(refactWhere(tableList, update.getWhere()));
			}

		}

		public  void handleDelete(Delete delete) {
			Table table = delete.getTable();
			if (table == null) 
				throw new RuntimeException("table can't be null");
			
			String prefix = handleTableNameReturnColumnPrefix(table);
			
			List tableList = new ArrayList(1);
			tableList.add(prefix);
			if (handleColumn()){
				delete.setWhere(refactWhere(tableList, delete.getWhere()));
			}
			
		}

	}

	


}
