package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.TsAlgmManager;
import net.jplugin.core.das.route.impl.conn.RouterConnection;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.util.SqlParserKit;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

public abstract class AbstractCommandHandler2 extends RefAnnotationSupport{
	public static final String __THE_TB_SPS_HDR__ = "__THE_TB_SPS_HDR__";
	protected net.sf.jsqlparser.statement.Statement statement =null;
	protected String sqlString;
	protected RouterConnection connetion;
	protected List<Object> parameters;
	private String tableName;
	private TableConfig tableConfig;
	
	
	/*
	 * <pre>
	 * 重要的规则：
	 * 目前SELECT支持  EQUAL,IN,BETWEEN三种.
	 * 其他都只支持 EQUAL一种，出现其他模式会报错。
	 * 
	 * 关于BETWEEN：
	 * 1.实际操作中，不管是左开右闭、左闭右开、左开右开都是用BETWEEN。
	 *   因为这里只限定表的范围，大一些也没关系，具体操作时会把条件都带上去的。
	 * 2.同时用BETWEEN来支持 单边界的 (>=,<=,>,<) 
	 *   如果不限制最小值，最小值为 LONG.MIN_VALUE,如果不限制最大值 最大值为 LONG.MAX_VALUE
	 * 
	 * 关于Operator的Value[]的数组长度：
	 *  EQUAL,只能有一个
	 *  IN，可以有多个
	 *  BETWEEN,必须有两个。
	 * </pre>
	 */
	
//	public static class KeyFilter {
//		public net.jplugin.core.das.route.api.KeyValueForAlgm.Operator operator;
//		public Value[] value;
//		public KeyFilter(Operator o,Value[] varr){
//			this.operator = o;
//			this.value = varr;
//		}
//		
//		public String toString(){
//			return operator+" "+ arrToString(value);
//		}
//
//		private String arrToString(Value[] v) {
//			String ret = "";
//			for (int i=0;i<v.length;i++){
//				ret = ret + "{"+ v[i]+"}  ";
//			}
//			return ret;
//		}
//	}
//	public static class Value{
//		public boolean isParamedKey;
//		public Object keyConstValue;
//		public int keyParamIndex;
//		public String toString(){
//			return "Param:"+isParamedKey +" VAL:"+ keyConstValue+  " Index:"+ keyParamIndex;
//		}
//	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public static AbstractCommandHandler2 create(RouterConnection conn, String sql, List<Object> params)  {
		CCJSqlParserManager pm = new CCJSqlParserManager();
		
    	net.sf.jsqlparser.statement.Statement stmt;
    	
		stmt = SqlParserKit.parse(sql);
    	
    	AbstractCommandHandler2 instance;
    	if (stmt instanceof Select){
    		instance = new SelectHandler2();
    	}else if (stmt instanceof Update){
    		instance = new UpdateHandler2();
    	}else if (stmt instanceof Insert){
    		instance = new InsertHandler2();
    	}else if (stmt instanceof Delete){
    		instance = new DeleteHandler2();
    	}else{
    		throw new RuntimeException("not supported sql for route:"+sql);
    	}
    	
    	//验证schema合法性，并去除schema前缀
    	SchemaCheckUtil.checkAndRemoveSchema(conn,stmt,sql);
    
    	instance.statement = stmt;
    	instance.connetion = conn;
    	instance.parameters = params;
    	instance.sqlString = sql;
    	
//    	//如果需要注释才能span，才获取span标记
//    	if (conn.getDataSource().getConfig().isCommentRequiredForSpan()){
//        	instance.spanTable = SpanCheckKit.isSpanTable(sql);    		
//    	}

//    	//insert 不支持spantable
//    	if (instance.spanTable && (stmt instanceof Insert)){
//    		throw new TablesplitException("Insert sql not support spantable.");
//    	}
    	return instance;
	}
	
	/**
	 * 在getKeyFilter过程中要调用setTableName方法设置tableName
	 * @return
	 */
	protected abstract List<RouterKeyFilter> getKeyFilter();
//	protected void maintainSqlMeta(Meta meta) {
//	}
	protected abstract void temporyChangeTableNameTo(String nm);
	
	
	public final  SqlHandleResult handle(){
		//获取表名称和KeyFilter
		List<RouterKeyFilter> kf = getKeyFilter();
//		//设置表名称
//		this.tableName = kf.first;
		//返回结果
		return getHandleResult(kf);
	}
	
	@RefLogger
	Logger logger;
	protected final SqlHandleResult getHandleResult(List<RouterKeyFilter> kfList){
//		//这里只支持EQ
//		if (kf==null) throw new RouterException("found null KeyFilter."+this.sqlString);
//		if (kf.operator!=Operator.EQUAL)
//			throw new RouterException("Only EQUAL be supported in basic impl."+this.sqlString);
		
		if (kfList == null || kfList.isEmpty())
			throw new RouterException("Found null KeyFilter."+this.sqlString);
		
		//获取配置
		TableConfig tableCfg = getTableCfg();
		if (tableCfg==null) 
			throw new TablesplitException("Table not configed in the router databse."+tableName);
		
		//转换为常亮值
		RouterKeyFilter[] keyValueArr = kfList.toArray(new RouterKeyFilter[kfList.size()]);
		//根据算法计算
		DataSourceInfo[] algmResults;
		
		//查询
		algmResults = getDataSourceInfosFromKeyValueArr(keyValueArr);
		if (logger.isInfoEnabled()){
			if (algmResults!=null)
				for (DataSourceInfo a:algmResults){
					logger.info("\tDataSourceInfo:"+a);
				}
			else logger.info("\tGet Null DataSource");
		}
		
		
		SqlHandleResult result = new SqlHandleResult();


		if (algmResults==null || algmResults.length==0){
			throw new RuntimeException("Can't find the dest table.");
		}else if (algmResults.length==1 && algmResults[0].getDestTbs().length==1){
			//设置sql
			String finalSql = getFinalSql(algmResults[0].getDestTbs()[0]);
			result.setResultSql(finalSql);
		}else{
			//如果是insert，直接错误
			if (this.statement instanceof Insert)
				throw new TablesplitException("Insert cant' support span table now. "+this.sqlString );
			
			//检查span注释是否有
			if (this.connetion.getDataSource().getConfig().isCommentRequiredForSpan()){
				if (!SpanCheckKit.isSpanTable(this.sqlString))
					throw new TablesplitException("table not use spantable,can't span. "+this.sqlString);
			}
			
			//设置sql
			String targetSql = getFinalSql(__THE_TB_SPS_HDR__);
			result.setResultSql(targetSql);
		}
		
		result.setDataSourceInfos(algmResults);
		result.setSourceTable(tableName);
		return result;
//		if (algmResults==null || algmResults.length==0){
//			throw new RuntimeException("Can't find the dest table.");
//		}else if (algmResults.length==1 && algmResults[0].getDestTbs().length==1){
//			result.setTargetDataSourceName(algmResults[0].getDsName());
//			String finalSql = getFinalSql(algmResults[0].getDestTbs()[0]);
//			result.setResultSql(finalSql);
//			result.setTargetTable(tableName);
//			return result;
//		}else{
//			
//			//如果是insert，直接错误
//			if (this.statement instanceof Insert)
//				throw new TablesplitException("Insert cant' support span table now. "+this.sqlString );
//			
//			//检查span注释是否有
//			if (this.connetion.getDataSource().getConfig().isCommentRequiredForSpan()){
//				if (!SpanCheckKit.isSpanTable(this.sqlString))
//					throw new TablesplitException("table not use spantable,can't span. "+this.sqlString);
//			}
//			
//			
//			CombinedSqlParser.Meta meta = new CombinedSqlParser.Meta();
//			meta.setSourceTb(tableName);
//			meta.setDataSourceInfos(algmResults);
//			
////			RouterConnectionCallContext.setStatement(this.statement);
////			maintainSqlMeta(meta);//维护OrderBy，count 
////			RouterConnectionCallContext.setMeta(meta);
//			String targetSql = getFinalSql(__THE_TB_SPS_HDR__);
//			String newSql = CombinedSqlParser.combine(targetSql, meta);
//			result.setResultSql(newSql);
//			result.setTargetDataSourceName(CombinedSqlParser.SPAN_DATASOURCE);
//			result.setTargetTable(tableName);
//			return result;
//		}
	}
	
	private DataSourceInfo[] getDataSourceInfosFromKeyValueArr(RouterKeyFilter[] keyValueArr) {
		if (keyValueArr.length==1){
			RouterKeyFilter keyValue = keyValueArr[0];
			return getDataSourceInfosFromOneKeyFilter(keyValue);
		}
		
		List<DataSourceInfo[]> metrix= new ArrayList<>(keyValueArr.length);
		for (int i=0;i<keyValueArr.length;i++){
			metrix.add( getDataSourceInfosFromOneKeyFilter(keyValueArr[i]));
		}
		
		//计算交集
		return InSectUtil.computeInsect(metrix);
	}
	
	private DataSourceInfo[] getDataSourceInfosFromOneKeyFilter(RouterKeyFilter keyValue) {
		//根据情况调用不同接口
		DataSourceInfo[] algmResults;
		if (keyValue.getOperator()==Operator.EQUAL){
			Result result = TsAlgmManager.getResult(connetion.getDataSource(), tableName, keyValue.getConstValue()[0]);
			algmResults = convertToOneDataSourceInfo(result);
		}else{
			algmResults = TsAlgmManager.getMultiResults(connetion.getDataSource(), tableName, keyValue);
		}
		return algmResults;
	}


	private DataSourceInfo[] convertToOneDataSourceInfo(Result result) {
		DataSourceInfo[] dsi = new DataSourceInfo[1];
		dsi[0] = new DataSourceInfo();
		dsi[0].setDsName(result.getDataSource());
		dsi[0].setDestTbs(new String[]{result.getTableName()});
		return dsi;
	}

	/**
	 * 深度遍历，碰到AND继续，否则终止
	 * @param where
	 * @return
	 */
	protected final List<RouterKeyFilter> getKeyFilterFromWhere(Expression where) {
		String colName =getTableCfg().getKeyField();
		
		List<RouterKeyFilter> list = VisitorExpressionManager.getKeyFilterList(where, colName,this.parameters);
		if (list==null || list.isEmpty())
			return null;
		else
			return list;
		
//		VisitorForAndExpression whereVisitor = new VisitorForAndExpression(colName);
//		where.accept(whereVisitor);
//		return whereVisitor.getKnownFilter();
	}

	protected final String getFinalSql(String finalTableName) {
		try{
			temporyChangeTableNameTo(finalTableName);
			return this.statement.toString();
		}finally{
			temporyChangeTableNameTo(tableName);
		}
	}

//	private KeyValueForAlgm[] transformKeyFilterToConst(List<KeyValueForAlgm> kf) {
//		return kf.toArray(new KeyValueForAlgm[kf.size()]);
////		KeyValueForAlgm[] arr = new KeyValueForAlgm[kf.size()];
////		for (int i=0;i<arr.length;i++){
////			arr[i] = transformKeyFilterToConst(kf.get(i));
////		}
////		return arr;
//	}
//	private KeyValueForAlgm transformKeyFilterToConst(KeyFilter kf) {
//		Object[] constValues = new Object[kf.value.length];
//		KeyValueForAlgm kvfa = new KeyValueForAlgm(kf.operator, constValues);
//		for (int i=0;i<kf.value.length;i++){
//			Value v = kf.value[i];
//			if (v!=null && v.isParamedKey){
//				if (v.keyParamIndex >= this.parameters.size()) 
//					throw new RouterException("param out of index. param len="+this.parameters.size());
//				kvfa.getConstValue()[i] = this.parameters.get(v.keyParamIndex);
//			}
//		}
//		return kvfa;
//	}

	private TableConfig getTableCfg() {
		if (this.tableConfig != null)
			return this.tableConfig;
		TableConfig tableCfg = this.connetion.getDataSource().getConfig().findTableConfig(tableName);
		if (tableCfg == null)
			new TablesplitException("The table is configed as a splate table." + tableName);
		this.tableConfig = tableCfg;
		return tableCfg;
	}



//
//	private DataSourceInfo[] convertToDataSourceInfos(Result[] algmResults) {
//		HashMap<String,List<String>> map = new HashMap<>();
//		for (Result r:algmResults){
//			String ds = r.getDataSource();
//			List<String> list = map.get(ds);
//			if (list==null) {
//				list = new ArrayList<>();
//				map.put(ds, list);
//			}
//			list.add(r.getTableName());
//		}
//		Set<Entry<String, List<String>>> s = map.entrySet();
//		DataSourceInfo[] arr = new DataSourceInfo[s.size()];
//		Object[] entryArr = s.toArray();
//		for (int i=0;i<entryArr.length;i++){
//			Entry<String, List<String>> en = (Entry<String, List<String>>) entryArr[i];
//			DataSourceInfo dsi = new DataSourceInfo();
//			dsi.setDsName(en.getKey());
//			String[] tbarr = new String[en.getValue().size()];
//			en.getValue().toArray(tbarr);
//			dsi.setDestTbs(tbarr);
//		}
//		return arr;
//	}
	
//		
//		boolean spanAll = tryWalkSpanAll(walker);
//		String tableName = walkTableName(walker);
//		
//		if (spanAll){
//			CombinedSqlParser.Meta meta = new Meta();
//			meta.setSourceTb(tableName);
//			meta.setDataSourceInfos(TsAlgmManager.getDataSourceInfos(conn.getDataSource(),tableName));
//			meta.setOrderParam(getOrderParam(walker));
//			meta.setCountStar(getCountStar(walker)? 1:0);
//			String newSql = CombinedSqlParser.combine(sql, meta);
//			SqlHandleResult result = new SqlHandleResult();
//			result.setResultSql(newSql);
//			result.setTargetDataSourceName(CombinedSqlParser.SPANALL_DATASOURCE);
//			return result;
//		}
//		
//		TableConfig tableCfg = getTableCfg(conn,tableName);
//		if (tableCfg==null) 
//			throw new TablesplitException("Table not configed in the router databse."+tableName);
//		String keyField = tableCfg.getKeyField();
//		String algm = tableCfg.getSplitAlgm();
//		
//		KeyResult keyResult = walkToGetKeyColumnInfo(walker,tableName,keyField);
//		Object keyValue;
//		if (keyResult.isParamedKey){
//			if (keyResult.keyParamIndex>=params.size())
//				throw new TablesplitException("Can't find the params for index "+keyResult.keyParamIndex+" param size="+params.size());
//			keyValue = params.get(keyResult.keyParamIndex);
//		}else{
//			keyValue = keyResult.keyConstValue;
//		}
//		
//		Result algmResult = TsAlgmManager.getResult(conn.getDataSource(), tableName, keyValue);
//		
//		SqlHandleResult result = new SqlHandleResult();
//		result.setTargetDataSourceName(algmResult.getDataSource());
//		String finalSql = getFinalSql(walker,tableName,algmResult.getTableName());
//		result.setResultSql(finalSql);
//		return result;
		
//	}
	
	

//
//	private boolean tryWalkSpanAll(SqlWordsWalker walker) {
//		for (int i=0;i<walker.size();i++){
//			if (walker.wordAt(i).startsWith("/*")){
//				String tmp = walker.wordAt(i);
//				if (tmp.endsWith("*/") && tmp.length()>5 && tmp.substring(2, tmp.length()-2).trim().equalsIgnoreCase("spantable"))
//					return true;
//			}
//		}
//		return false;
//	}
	
	protected TableConfig getTableCfg(RouterConnection conn,String tableName) {
		TableConfig tableCfg = conn.getDataSource().getConfig().findTableConfig(tableName);
		if (tableCfg ==null) new TablesplitException("The table is configed as a splate table."+tableName);
		return tableCfg;
	}

	public static void main(String[] args) {
		
//		AbstractCommandHandler2 o = AbstractCommandHandler2.create(null, "update   /*+asdfg sdf*/  /*+fddd*/ abc set a='1' where x = 1", null);
		AbstractCommandHandler2 o = AbstractCommandHandler2.create(null, "select count(*),count(1) from t1,t2,t3 where a=b", null);

		
		System.out.println(o);
	}


}
