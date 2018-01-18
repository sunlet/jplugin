package net.jplugin.core.das.route.api;

public interface IAggregationFunctionHandler{
	/**
	 * 
	 * @param ctx 求数值的上下文
	 * @param currentRowValue  当前评估的值，可能也是聚集以后的结果
	 * @param rowCntForCurentValue  当前评估值对应的行数
	 */
	public void aggrate(AggFunctionEvalueContext ctx,Object currentRowValue,int rowCntForCurentValue);
//
//	/**
//	 * 设置状态
//	 */
//	public abstract void reset();
//	/**
//	 * 添加值
//	 * @param obj
//	 */
//	public abstract void addValue(Object obj);
//	/**
//	 * 获取结果
//	 * @return
//	 */
//	public abstract Object getResult();
	
}
