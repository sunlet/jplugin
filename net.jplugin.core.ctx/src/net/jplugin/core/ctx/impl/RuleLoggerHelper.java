package net.jplugin.core.ctx.impl;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.impl.LogServiceImpl;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-12 下午05:56:20
 **/

public class RuleLoggerHelper {

	
	private static Logger ruleLogger;

	/**
	 * @return
	 */
	public static Logger getLogger() {
		if (ruleLogger == null){
			synchronized (RuleLoggerHelper.class) {
				if (ruleLogger==null){
					ruleLogger = ServiceFactory.getService(ILogService.class).getSpecicalLogger("rulelog.log");
				}
			}
		}
		return ruleLogger;
	}

	/**
	 * @param sb
	 * @param throwable
	 */
	public static void dolog(String o, Throwable throwable) {
		StringBuffer sb = new StringBuffer();
		sb.append("-user="+" ");
		sb.append("thread="+Thread.currentThread().getName());
		sb.append(" ");
		sb.append(o);
		getLogger().info(sb.toString(),throwable);
	}

	/**
	 * @param string
	 */
	public static void dolog(String string) {
		dolog(string,null);
	}
}
