package net.jplugin.core.log.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.impl.helper.FormattingTuple;
import net.jplugin.core.log.impl.helper.MessageFormatter;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午11:35:49
 **/

public class Logger4Log4j implements Logger {
	private static final String FQCN = Logger4Log4j.class.getName();
	org.apache.log4j.Logger logger;
	String logName;

	Logger4Log4j(org.apache.log4j.Logger l) {
		this.logger = l;
	}
	
	Logger4Log4j(String n) {
		this.logName = n;
		LoggerListCacher.add(this);
	}

	public void debug(Object msg, Throwable t) {
		log(Level.DEBUG,msg,t);
	}

	public void debug(Object msg) {
		log(Level.DEBUG,msg);
	}

	public void debug(String format, Object... arguments) {
		log(Level.DEBUG,format,arguments);
	}

	public void error(Object message, Throwable t) {
		log(Level.ERROR, message, t);
	}

	public void error(Object message) {
		log(Level.ERROR,message);
	}
	
	public void error(String format, Object... arguments) {
		 log(Level.ERROR,format,arguments);
	}

	public void fatal(Object message, Throwable t) {
		log(Level.FATAL,message,t);
	}
	public void fatal(String format, Object... arguments) {
		log(Level.FATAL,arguments);
	}
	public void fatal(Object message) {
		log(Level.FATAL, message);
	}
	
	public final Level getLevel() {
		return getLogLevel();
	}

	@Override
	public int getLoggerLevel() {
		return getLogLevel().toInt();
	}

	public void info(Object message, Throwable t) {
		log(Level.INFO,message,t);
	}

	public void info(Object message) {
		log(Level.INFO,message);
	}
	
	public void info(String format, Object... arguments) {
		log(Level.INFO, format,arguments);
	}

	public boolean isDebugEnabled() {
		return isLogEnabled(Level.DEBUG);
	}

	public boolean isEnabledFor(Priority level) {
		return isLogEnabled(level);
	}

	@Override
	public boolean isEnabledFor(int level) {
		switch (level){
			case Level.ALL_INT:
				return isLogEnabled(Level.ALL);
			case Level.DEBUG_INT:
				return isLogEnabled(Level.DEBUG);
			case Level.INFO_INT:
				return isLogEnabled(Level.INFO);
			case Level.WARN_INT:
				return isLogEnabled(Level.WARN);
			case Level.ERROR_INT:
				return isLogEnabled(Level.ERROR);
			case Level.FATAL_INT:
				return isLogEnabled(Level.FATAL);
			default:
				throw new RuntimeException("error log level:"+level);
		}
	}

	public boolean isInfoEnabled() {
		return isLogEnabled(Level.INFO);
	}

	public boolean isTraceEnabled() {
		return isLogEnabled(Level.TRACE);
	}

	@Override
	public boolean isErrorEnabled() {
		return isLogEnabled(Level.ERROR);
	}

	@Override
	public boolean isWarnEnabled() {
		return isLogEnabled(Level.WARN);
	}

	public void warn(Object message, Throwable t) {
		log(Level.WARN, message, t);
	}

	public void warn(Object message) {
		log(Level.WARN, message);
	}
	
	public void warn(String format, Object... arguments) {
		log(Level.WARN,format,arguments);
	}
	
	
	//基础方法
	private Level getLogLevel(){
		if (logger== null) 
			return Level.DEBUG;
		
		return logger.getLevel();
	}

	private boolean isLogEnabled(Priority level){
		if (logger == null) 
			return true;

		return logger.isEnabledFor(level);
	}
	private void log(Level level,Object message){
		if (logger == null){ 
			PluginEnvirement.INSTANCE.getStartLogger().log(message);
			return;
		}
			
		logger.log(FQCN, level, message, null);
	}
	private void log(Level level,Object message,Throwable th){
		if (logger == null){ 
			PluginEnvirement.INSTANCE.getStartLogger().log(message,th);
			return;
		}
		
		logger.log(FQCN, level, message, th);
	}
	private void log(Level level,String format,Object... arguments){
		if (logger == null){
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			PluginEnvirement.INSTANCE.getStartLogger().log(ft.getMessage(), ft.getThrowable());
			return;
		}
		
		if (logger.isEnabledFor(level)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			logger.log(FQCN, level, ft.getMessage(), ft.getThrowable());
		}
	}

	//called when log init OK
	void createLogger() {
		this.logger = org.apache.log4j.Logger.getLogger(this.logName);
	}
}
