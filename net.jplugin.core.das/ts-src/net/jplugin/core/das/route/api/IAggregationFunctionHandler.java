package net.jplugin.core.das.route.api;

import java.sql.SQLException;

public interface IAggregationFunctionHandler{
	/**
	 * 
	 * @param ctx 求数值的上下文
	 * @param currentRowValue  当前评估的值，可能也是聚集以后的结果
	 * @param rowCntForCurentValue  当前评估值对应的行数
	 * @param coltype 
	 * @throws SQLException 
	 */
	public void aggrate(AggFunctionEvalueContext ctx,Object currentRowValue,int rowCntForCurentValue, int coltype) throws SQLException;


	public Object getResult(AggFunctionEvalueContext ctx, int sqlType) throws SQLException;
	
}
