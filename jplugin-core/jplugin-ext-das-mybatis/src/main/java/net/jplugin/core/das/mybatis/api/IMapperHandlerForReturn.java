package net.jplugin.core.das.mybatis.api;

public interface IMapperHandlerForReturn<M, R> {
	public R fetchResult(M mapper);
}
