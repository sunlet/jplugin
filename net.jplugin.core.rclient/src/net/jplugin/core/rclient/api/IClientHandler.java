package net.jplugin.core.rclient.api;

import java.lang.reflect.Method;

public interface IClientHandler {
//	<T> T createProxyObject(Client<T> client);

	Object invoke(Client client,Object proxy, Method method, Object[] args) throws Throwable;

}
