package net.jplugin.core.log.api;

import net.jplugin.core.log.impl.LogServiceImpl;

public class LogFactory {
	static ILogService logService = null;
	
	static boolean inited=false;
	public static  synchronized void init(){
		if (inited) {
			return ;
		}
		inited = true;
		logService = new LogServiceImpl();
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
	}
}
