package net.jplugin.core.das.api;

/**
 * 每次获取Connection都会调用，所以实现类要做好缓存。
 * @author LiuHang
 *
 */
public interface IDynamicDataSourceProvider {
	String computeDataSourceName();
}
