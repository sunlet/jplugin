package net.jplugin.core.rclient.proxyfac;

public class ClientProxyDefinition {
	String protocol;
	Class interf;
	String url;
	String appId;
	String appToken;

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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppToken() {
		return appToken;
	}
	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
}
