package net.jplugin.ext.token.impl;

import net.jplugin.core.das.hib.api.Entity;

@Entity(indexes={"identifier,realm"},textFields="tokenInfo",idgen="uuid.hex")
public class DBToken {
	String id;
	String random;
	String identifier;
	String realm;
	String tokenInfo;
	String createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getTokenInfo() {
		return tokenInfo;
	}
	public void setTokenInfo(String tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
