package net.jplugin.core.rclient.api;

public interface IClientHandler {
	<T> T createProxyObject(Client<T> client);

}
