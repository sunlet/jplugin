package net.jplugin.common.kits.filter;

public interface IFilter<T> {
	public Object filter(FilterChain fc, T ctx) throws Throwable ;
}
