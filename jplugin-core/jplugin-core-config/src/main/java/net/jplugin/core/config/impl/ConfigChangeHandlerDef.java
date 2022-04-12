package net.jplugin.core.config.impl;

import java.util.Set;

import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.config.api.IConfigChangeHandler;

public class ConfigChangeHandlerDef {
	String pattern;
	Class<? extends IConfigChangeHandler>  handlerClass;
	
	StringMatcher matcher;//初始化時賦值！
	IConfigChangeHandler handlerInstance;//初始化時賦值！
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public Class<? extends IConfigChangeHandler> getHandlerClass() {
		return handlerClass;
	}
	public void setHandlerClass(Class<? extends IConfigChangeHandler> handlerClass) {
		this.handlerClass = handlerClass;
	}
	
	public StringMatcher getMatcher() {
		return matcher;
	}
	public void setMatcher(StringMatcher matcher) {
		this.matcher = matcher;
	}
	public IConfigChangeHandler getHandlerInstance() {
		return handlerInstance;
	}
	public void setHandlerInstance(IConfigChangeHandler handlerInstance) {
		this.handlerInstance = handlerInstance;
	}
	
	
}
