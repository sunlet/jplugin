package net.jplugin.common.kits;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

public  class StringMatcher{
	private enum MatcherMode {
		NONE, POSTFIX, PREFIX, EQUAL ,COMPOSITE
	}
	
	MatcherMode mode;
	String value;//mode != COMPOSITE 时有效
	StringMatcher[] childsMatcher;//mode == COMPOSITE 时有效
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[ ").append(mode).append(",");
		if (this.mode!=MatcherMode.COMPOSITE)
			sb.append(value);
		else{
			for (StringMatcher o:childsMatcher){
				sb.append(o);
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public StringMatcher (String format){
		if (StringKit.isNull(format) || "*".equals(format)){
			this.mode = MatcherMode.NONE;
			this.value = null;
		}else{
			MatcherMode mode;
			String value;
			
			if (format.indexOf('|')>=0){
				if (format.startsWith("|") || format.endsWith("|")){
					throw new RuntimeException("format error:"+format);
				}
				this.mode = MatcherMode.COMPOSITE;
				String[] itemArr = StringKit.splitStr(format, "|");
				for (String s:itemArr){
					AssertKit.assertStringNotNull(s," format error:"+format);
				}
				childsMatcher = new StringMatcher[itemArr.length];
				for (int i=0;i<itemArr.length;i++){
					childsMatcher[i] = new StringMatcher(itemArr[i]);
				}
			}else{
				if (format.startsWith("*")) {
					mode = MatcherMode.POSTFIX;
					value = format.substring(1);
				} else if (format.endsWith("*")) {
					mode = MatcherMode.PREFIX;
					value = format.substring(0, format.length() - 1);
				} else {
					mode = MatcherMode.EQUAL;
					value = format;
				}
	
				if (!checkValue(value)) {
					throw new RuntimeException("Method Filter str is bat format:" + format);
				}
				this.mode = mode;
				this.value = value;
			}
		}
	}
	
	public boolean match(String str) {
		if (this.mode==MatcherMode.COMPOSITE){
			//匹配composite类型的Matcher
			if (this.childsMatcher==null){
				throw new RuntimeException("Can't go here");
			}
			for (StringMatcher m:this.childsMatcher){
				if (m.match(str)) 
					return true;
			}
			return false;
		}
		
		switch (this.mode) {
		case NONE:
			return true;
		case EQUAL:
			return this.value.equals(str);
		case POSTFIX:
			return str.endsWith(this.value);
		case PREFIX:
			return str.startsWith(this.value);
		default:
			throw new RuntimeException("error value:" + this.mode);
		}
	}
	
	private boolean checkValue(String value) {
		if (StringKit.isNull(value))
			return false;
		if (value.indexOf('*') >= 0)
			return false;
		return true;
	}
}