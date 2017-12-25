package net.jplugin.core.das.route.api;

public interface IAggregationFunctionHandler{

	/**
	 * 设置状态
	 */
	public abstract void reset();
	/**
	 * 添加值
	 * @param obj
	 */
	public abstract void addValue(Object obj);
	/**
	 * 获取结果
	 * @return
	 */
	public abstract Object getResult();
	
}
