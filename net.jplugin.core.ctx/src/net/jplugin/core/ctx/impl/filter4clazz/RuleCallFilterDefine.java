package net.jplugin.core.ctx.impl.filter4clazz;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;

public class RuleCallFilterDefine {

	private StringMatcher methodMatcher;
	private StringMatcher classMatcher;

	// 下面两个属性不需要在构造函数维护，外面的管理类会赋值
	private int priority;
	private Class filterClazz;

	public String toString(){
		StringBuffer sb = new StringBuffer("{");
		sb.append("classMatcher="+classMatcher).append(" ");
		sb.append("methodMatcher="+methodMatcher).append(" ");
		sb.append("}");
		return sb.toString();
	}
	
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
		
		if (strDefine.startsWith(",") || strDefine.endsWith(",")){
			throw new RuntimeException("format error:"+strDefine);
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

}
