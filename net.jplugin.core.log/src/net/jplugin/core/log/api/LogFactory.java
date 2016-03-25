package net.jplugin.core.log.api;

import net.jplugin.core.log.impl.LogServiceImpl;

public class LogFactory {
	static ILogService logService = null;
	
	static boolean inited=false;
	public static  synchronized void init(){
		if (inited) {
			System.out.println("Warnning : LogFactory init a second time!");
			return ;
		}
		inited = true;
		logService = new LogServiceImpl();
	}
	
	public static Logger getLogger(String name){
		return logService.getLogger(name);
	}
	
	/**
	 * 获取特殊的专用Logger：名字是特殊的，并且additive=false，level=debug
	 * @param filename
	 * @return
	 */
	public static Logger getSpecicalLogger(String filename){
		return logService.getSpecicalLogger(filename);
	}
	
	public static ILogService getLoggerService(){
		return logService;
	}
}
