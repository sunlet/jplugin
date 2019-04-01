package net.jplugin.core.log.impl;

import java.util.LinkedList;
import java.util.List;

public class LoggerListCacher {
	private static final int MAX_LOGGERS_BEF_COMMON_LOG_START = 100000;
	
	static List<Logger4Log4j> list = new LinkedList<Logger4Log4j>();
	public synchronized static void add(Logger4Log4j logger4Log4j) {
		if (list.size()>=MAX_LOGGERS_BEF_COMMON_LOG_START){
			throw new RuntimeException("Too many loggers created before Common Logger Start");
		}else{
			list.add(logger4Log4j);
		}
		
	}
	public synchronized static void createLog4jLoggers() {
		for (Logger4Log4j l:list){
			l.createLogger();
		}
		list.clear();
		list = null;
	}
}
