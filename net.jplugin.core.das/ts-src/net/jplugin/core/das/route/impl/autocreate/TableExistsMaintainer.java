package net.jplugin.core.das.route.impl.autocreate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple3;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.impl.TableAutoCreation;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult;
import net.jplugin.core.das.route.impl.conn.SqlHandleResult.CommandType;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;

public class TableExistsMaintainer {
	public static MaintainReturn lastMaintainResult;
	
	private static final String NO_RESULT_SELECT = "select 'a' from dual where 1=2";
//	private static final String COUNTSTAR_RESULT_SELECT = "select count(*) from dual where 1=2";

	
	
	public static String makeCountResult(String alais){
		return "select count(*) "+ alais +" from dual where 1=2";
	}
	
	public static MaintainReturn maintainAndCheckNoneResult(SqlHandleResult shr){
		boolean singleTableBefore = shr.singleTable();
		
		MaintainReturn result = maintainAndCheckNoneResultInner(shr);
		if (PluginEnvirement.INSTANCE.isUnitTesting()){
			lastMaintainResult = result;
		}
		
		if (!result.isSpecialCondition()){
			//没满足特殊情况，说明还是有表的
			if (shr.singleTable()!=singleTableBefore){
				//如果singletable状态发生变化，则需要修改sql语句
				AssertKit.assertTrue(shr.getResultSql().indexOf(AbstractCommandHandler2.__THE_TB_SPS_HDR__)>=0);
				shr.setResultSql(StringKit.replaceStr(shr.getResultSql(), AbstractCommandHandler2.__THE_TB_SPS_HDR__, shr.getDataSourceInfos()[0].getDestTbs()[0]));
			}
		}
		return result;
	}
	/**
	 * <pre>
	 * 对于Select ，没有表的情况下使用dual表,条件1=2
	 * 对于Select count的情况，对dual表使用count(*)，条件1=2
	 * 
	 * 对于update、delete，返回无结果集
	 * 
	 * 对于insert，创建不存在的表
	 * 
	 * 只要返回的不是FALSE_NULL,就不能按照正常路径走了！
	 * </pre>
	 * @param shr
	 * @return
	 */
	private static MaintainReturn maintainAndCheckNoneResultInner(SqlHandleResult shr){
		if (shr.getCommandType() == CommandType.INSERT){
			//创建不存在的表
			tryCreateTable(shr);
			return MaintainReturn.FALSE_NULL;
		}
		
		if (shr.getCommandType() == CommandType.SELECT){
			tryRemoveTable(shr);
			
			
			
			if (shr.getDataSourceInfos().length==0){
				PlainSelect selectBody = (PlainSelect) ((Select) shr.getStatement()).getSelectBody();
				
				//对于count语句,groupby 用dual表. 
				//判断是否count语句
				Tuple3<Boolean, String,Boolean> checkResult = checkCountStatement(selectBody);
				if (checkResult.first){
					if (checkResult.third){
						//有groupby，用dual表的 select
						String sql = NO_RESULT_SELECT;
						return MaintainReturn.with(false, sql);
					}else{
						//无groupby，用dual表的select count
						String sql = makeCountResult(checkResult.second);
						return MaintainReturn.with(false, sql);
					}
				}else{
					//非count语句用dual表. 注：这里并没有把列名对应select出来哈！
					String sql = NO_RESULT_SELECT;
					return MaintainReturn.with(false, sql);
				}
			}
			return MaintainReturn.FALSE_NULL;
		}
		
		if (shr.getCommandType() == CommandType.UPDATE || shr.getCommandType() == CommandType.DELETE){
			//清除不存在的表，检查是否需返回空Result
			tryRemoveTable(shr);
			if (shr.getDataSourceInfos().length==0){
				//根据后面的算法，不存在length!=0 但是 表数组为空的情况
				return MaintainReturn.TRUE_NULL;
			}else{
				return MaintainReturn.FALSE_NULL;
			}
		}else{
			throw new RuntimeException("not support yet");
		}
	}
	
	public static class MaintainReturn{
		public static final MaintainReturn TRUE_NULL = with(true,null);;
		public static final MaintainReturn FALSE_NULL = with(false,null);
		
		boolean returnZeroRowUpdateStatement;
		String targetSqlForDummy;
		
		public static MaintainReturn with(boolean b,String s){
			MaintainReturn o = new MaintainReturn();
			o.returnZeroRowUpdateStatement = b;
			o.targetSqlForDummy = s;
			return o;
		}
		
		/**
		 * 是否特殊情况
		 * @return
		 */
		public boolean isSpecialCondition(){
			//确定两个特殊只有一个
			AssertKit.assertTrue(!(this.returnZeroRowUpdateStatement && this.targetSqlForDummy!=null));
			return this.returnZeroRowUpdateStatement || this.targetSqlForDummy!=null;
		}
		
		public boolean isReturnZeroRowUpdateStatement() {
			return returnZeroRowUpdateStatement;
		}
		public void setReturnZeroRowUpdateStatement(boolean returnZeroRowUpdateStatement) {
			this.returnZeroRowUpdateStatement = returnZeroRowUpdateStatement;
		}
		public String getTargetSqlForDummy() {
			return targetSqlForDummy;
		}
		public void setTargetSqlForDummy(String targetSqlForDummy) {
			this.targetSqlForDummy = targetSqlForDummy;
		}
	}
	/**
	 * count语句判断标准：只有一个selectItem并且为count函数，或者如果是*而且无groupby则找内层From子查询是否为count
	 * @param st
	 * @return  iscount ,count name, isgroupby
	 */
	private static Tuple3<Boolean, String,Boolean> checkCountStatement(PlainSelect ps) {
		List<SelectItem> items = ps.getSelectItems();
		if (items.size()==1){
			//只有一项
			SelectItem item = items.get(0);
			if (item instanceof AllColumns){
				//如果是 * ，判断有无groupby，找内层
				if (ps.getGroupBy()==null){
					FromItem fromitem = ps.getFromItem();
					if (fromitem instanceof SubSelect){
						return checkCountStatement((PlainSelect)((SubSelect)fromitem).getSelectBody());
					}
				}
			}
			if (item instanceof SelectExpressionItem){
				SelectExpressionItem expressionItem = ((SelectExpressionItem)item);
				Expression exp = expressionItem.getExpression();
				if (exp instanceof Function){
					if ("COUNT".equalsIgnoreCase(((Function)exp).getName())){
						//返回true
						String countName = null;
						
						Alias alais = expressionItem.getAlias();
						if (alais!=null)
							countName = expressionItem.getAlias().getName();
						if (StringKit.isNull(countName)){
							countName = "";
						}
						
						Boolean isGroupby = ps.getGroupBy()!=null;
						return Tuple3.with(true,countName,isGroupby);
					}
				}
			}
		}
		return Tuple3.with(false,null,null);
	}

	private static void tryCreateTable(SqlHandleResult shr) {
		for (DataSourceInfo ds:shr.getDataSourceInfos()){
			for (String tb:ds.getDestTbs()){
				TableAutoCreation.checkExistsAndCreate(shr.getTableConfig(), ds.getDsName(), tb,shr.getSourceTable());
			}
		}
	}
	
	private static void tryRemoveTable(SqlHandleResult shr) {
		//先扫描一遍，如果表都存在，则快速返回。同时构造不存在队列
		Set notExistsTables =null;
		for (DataSourceInfo ds:shr.getDataSourceInfos()){
			for (String tb:ds.getDestTbs()){
				if (!TableAutoCreation.checkExists(shr.getTableConfig(), ds.getDsName(), tb)){
					//不存在
					if (notExistsTables==null)
						notExistsTables = new HashSet<>();
					notExistsTables.add(makeKey(ds.getDsName(),tb));
				}
			}
		}
		
		//表不存在，构建新的数据
		if (notExistsTables!=null && !notExistsTables.isEmpty()){
			shr.setDataSourceInfos(makeNewDsInfo(shr.getDataSourceInfos(),notExistsTables));
		}
		
	}

	private static DataSourceInfo[] makeNewDsInfo(DataSourceInfo[] dataSourceInfos, Set notExistsTables) {
		Map<String,List<String>> newInfo = new HashMap();
		for (DataSourceInfo ds:dataSourceInfos){
			for (String tb:ds.getDestTbs()){
				if (!notExistsTables.contains(makeKey(ds.getDsName(), tb))){
					//表存在，先找到list; 由于延迟创建List，所以list肯定不会为空list
					List<String> targetList = newInfo.get(ds.getDsName());
					if (targetList==null){
						targetList = new ArrayList();
						newInfo.put(ds.getDsName(), targetList);
					}
					//放入tablename
					targetList.add(tb);
				}
			}
		}
		
		//转换数据返回
		DataSourceInfo[] ret = new DataSourceInfo[newInfo.size()];
		int pos = 0;
		for (Entry<String, List<String>> en:newInfo.entrySet()){
			DataSourceInfo dsi = new DataSourceInfo();
			dsi.setDsName(en.getKey());
			dsi.setDestTbs(en.getValue().toArray(new String[en.getValue().size()]));
			ret[pos ++]=dsi;
		}
		
		return ret;
	}

	private static Object makeKey(String dsName, String tb) {
		return dsName+"##"+tb;
	}


}
