package net.jplugin.core.rclient.proxyfac;

public class ClientProxyDefinition {
	String protocol;
	Class interf;
	String url;

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public Class getInterf() {
		return interf;
	}
	public void setInterf(Class interf) {
		this.interf = interf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
