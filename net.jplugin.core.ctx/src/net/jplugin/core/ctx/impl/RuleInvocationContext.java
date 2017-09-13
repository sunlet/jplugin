package net.jplugin.core.ctx.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-12 下午04:11:52
 **/

public class RuleInvocationContext {

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

//	static boolean isLogRule;
	public static void init(){
//		isLogRule = "true".equalsIgnoreCase(ConfigFactory.getStringConfig("platform.log-rule-exec"));
//		PluginEnvirement.INSTANCE.getStartLogger().log("platform.log-rule-exec value is:"+isLogRule);
	}
	/**
	 * @param athrowable
	 */
	public void end(Throwable athrowable) {
		this.throwable = athrowable;
		this.end = new Date();
		if (meta.log()){
			doLog();
		}
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
		sb.append("-RuleInvoked:").append(method.getName()).append(meta.actionDesc());
		sb.append(" begin=").append(local.get().format(begin));
		sb.append(" end=").append(local.get().format(end));
		sb.append(" dural=").append(end.getTime()-begin.getTime());
		sb.append(" result=").append(throwable==null? "OK":"EXCEPTION");
		
		if (meta.logIndexes().length>0 && args!=null){
			sb.append(" param={");
			int[] idxs = meta.logIndexes();
			for (int i=0;i<idxs.length;i++){
				int idx = idxs[i];
				if (idx<args.length)
					sb.append(args[idx].toString());
				
				if (i!=idxs.length-1){
					sb.append(",");
				}
			}
			sb.append("}");
		}
		
		RuleLoggerHelper.dolog(sb.toString(),throwable);
	}



}
