package net.jplugin.core.ctx.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.jplugin.core.ctx.api.Rule;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-12 下午04:11:52
 **/

public class InvocationContext {

	private Method method;
	private Object[] args;
	private Rule meta;
	private Date begin;
	private Throwable throwable;
	private Date end;

	/**
	 * @param aMethod
	 * @param aArgs
	 * @param aMeta
	 */
	public void begin(Method aMethod, Object[] aArgs, Rule aMeta) {
		this.method = aMethod;
		this.args = aArgs;
		this.meta = aMeta;
		this.begin = new Date();
	}

	/**
	 * @param athrowable
	 */
	public void end(Throwable athrowable) {
		this.throwable = athrowable;
		this.end = new Date();
		doLog();
	}

	
	ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>(){

		@Override
		protected SimpleDateFormat initialValue() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
			return sdf;
		}
		
	};
	/**
	 * 
	 */
	private void doLog() {
		StringBuffer sb = new StringBuffer();
		sb.append("-RuleInvoked:").append(method.getName()).append(" begin=").append(local.get().format(begin));
		sb.append(" end=").append(local.get().format(end));
		sb.append(" dural=").append(end.getTime()-begin.getTime());
		sb.append(" result=").append(throwable==null? "OK":"EXCEPTION");
		RuleLoggerHelper.dolog(sb.toString(),throwable);
	}



}
