package net.jplugin.core.ctx.impl;

import java.lang.reflect.Method;

import net.jplugin.core.ctx.api.Rule;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-11 上午08:39:33
 **/

public interface RuleInvocationHandler {
	public Object invoke(Object proxyObj, Object oldService, Method method, Object[] args, Rule meta) throws Throwable;
}