package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;

public class RuleCallFilterDefine {
	private enum MatcherMode {
		NONE, POSTFIX, PREFIX, EQUAL
	}
	private StringMatcher methodMatcher;
	private StringMatcher classMatcher;

	// 下面两个属性不需要在构造函数维护，外面的管理类会赋值
	private int priority;
	private Class filterClazz;

	public RuleCallFilterDefine(String classFormat, String methodFormat) {
		this.classMatcher = new StringMatcher(classFormat);
		this.methodMatcher = new StringMatcher(methodFormat);
	}

	public boolean matchClazz(String clazz) {
		return this.classMatcher.match(clazz);
	}

	public boolean matchMethod(String method) {
		return this.methodMatcher.match(method);
	}

	/**
	 * 支持的格式： com.xxx.*:get*,com.yyy.Class2:*Page,com.zzz.Class3,*
	 * 
	 * @param strDefine
	 * @return
	 */
	public static List<RuleCallFilterDefine> parse(String strDefine) {
		List<RuleCallFilterDefine> ret = new ArrayList<>();
		
		if (StringKit.isNull(strDefine)){
			throw new RuntimeException("[applyTo] must not null ,if match all . should use *");
		}
		
		String[] strlist = StringKit.splitStr(strDefine, ",");

		for (String s : strlist) {
			if (s == null || s.equals("")) {
				throw new RuntimeException("parse string for define error:" + strDefine);
			}
			ret.add(createDefine(s));
		}

		return ret;
	}

	private static RuleCallFilterDefine createDefine(String s) {
		String[] arr = StringKit.splitStr(s, ":");
		if (arr.length > 2)
			throw new RuntimeException("error format for ClassMethodFilterDefine:" + s);

		String clazz, methodFilter;
		if (arr.length == 1) {
			clazz = arr[0];
			methodFilter = null;
		} else if (arr.length == 2) {
			clazz = arr[0];
			methodFilter = arr[1];
			if (StringKit.isNull(methodFilter)) {
				throw new RuntimeException("Method filter is null." + s);
			}
		} else {
			throw new RuntimeException("shoudln't come here." + s);
		}

		RuleCallFilterDefine def = new RuleCallFilterDefine(clazz, methodFilter);
		return def;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Class getFilterClazz() {
		return filterClazz;
	}

	public void setFilterClazz(Class filterClazz) {
		this.filterClazz = filterClazz;
	}
	
	

	private static class StringMatcher{
		MatcherMode mode;
		String value;
		
		public StringMatcher (String format){
			if (StringKit.isNull(format) || "*".equals(format)){
				this.mode = MatcherMode.NONE;
				this.value = null;
			}else{
				MatcherMode mode;
				String value;
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
		
		public boolean match(String str) {
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
}
