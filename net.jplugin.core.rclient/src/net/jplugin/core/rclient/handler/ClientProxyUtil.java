package net.jplugin.core.rclient.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientCallContext;
import net.jplugin.core.rclient.api.IClientHandler;

public class ClientProxyUtil {

	public static <T> T createProxyObject(final Client<T> c) {
		return (T) Proxy.newProxyInstance(c.getInterfaceClazz().getClassLoader(), new Class[] { c.getInterfaceClazz() },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// ClientFilterRegistry
						ClientCallContext ctx = ClientCallContext.create(c, proxy, method, args);
						ClientFilterRegistry.instance.filterStart(ctx);
						try {
							IClientHandler handler = ClientHandlerRegistry.instance.getClientHandler(c.getProtocal());
							Object ret = handler.invoke(c, proxy, method, args);
							ctx.setReturnObject(ret);
							return ret;
						} catch (Throwable th) {
							ctx.setThrowable(th);
							if (th instanceof RuntimeException)
								throw (RuntimeException) th;
							else if (th instanceof InvocationTargetException)
								throw ((InvocationTargetException) th).getTargetException();
							else
								throw new RuntimeException(th);
						} finally {
							ClientFilterRegistry.instance.filterEnd(ctx);
						}
					}
				});

	}
}
