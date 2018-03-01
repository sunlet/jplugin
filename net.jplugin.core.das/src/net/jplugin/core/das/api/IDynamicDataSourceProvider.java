package net.jplugin.core.das.api;

/**
 * 每次获取Connection都会调用，所以实现类要做好缓存。
 * @author LiuHang
 *
 */
public interface IDynamicDataSourceProvider {
	/**
	 * declaredDataSource 也作为参数传入，实现类可以不用，更灵活一些
	 * @param declaredDataSource
	 * @return
	 */
	String computeDataSourceName(String declaredDataSource);
}
