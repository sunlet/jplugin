package net.jplugin.core.log.api;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import net.jplugin.core.log.impl.BridgedLoggerService;

public class LogFactory {
	static BridgedLoggerService logService = null;
	
	static boolean inited=false;
	public static  synchronized void init(){
		if (inited) {
			return ;
		}
		inited = true;
		logService = new BridgedLoggerService();
		//为了避免第三方软件中直接调用LOG4J接口的情况打印找不到配置红色信息，先配置一个基础的
		//待系统启动过程中重新加载log配置
		org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.ERROR);
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
	}
	
	public static synchronized void initCommonLoggerService(){
		logService.initCommonLoggerService();
	}
	
	public static Logger getLogger(Class c){
		init();
		return logService.getLogger(c.getName());
	}
	
	public static Logger getLogger(String name){
		init();
		return logService.getLogger(name);
	}
	
	/**
	 * 获取特殊的专用Logger：名字是特殊的，并且additive=false，level=debug
	 * @param filename
	 * @return
	 */
	public static Logger getSpecicalLogger(String filename){
		init();
		return logService.getSpecicalLogger(filename);
	}
	
	public static void main(String[] args) {
		getLogger(LogFactory.class).info("abcdefg");
		getLogger(LogFactory.class).info("abcdefg");
		getLogger(LogFactory.class).info("abcdefg");
		getLogger(LogFactory.class).info("abcdefg");
		getLogger(LogFactory.class).info("abcdefg");
	}
}
