package net.jplugin.core.log.api;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import net.jplugin.core.service.api.ServiceFactory;

@Deprecated
public class LazyLogger implements Logger{
	private String logName;
	private Logger log = null;
	
	public static Logger get(String name){
		return new LazyLogger(name);
	}
	private LazyLogger(String name){
		logName = name;
	}
	
	private synchronized void initlog() {
		log = LogFactory.getLogger(logName);
	}
	
	public void debug(Object message, Throwable t) {
		if (log == null) initlog();
		log.debug(message, t);
	}

	public void debug(Object message) {
		if (log == null) initlog();
		log.debug(message);
	}

	public void error(Object message, Throwable t) {
		if (log == null) initlog();
		log.error(message, t);
	}

	public void error(Object message) {
		if (log == null) initlog();
		log.error(message);
	}

	public void fatal(Object message, Throwable t) {
		if (log == null) initlog();
		log.fatal(message, t);
	}

	public void fatal(Object message) {
		if (log == null) initlog();
		log.fatal(message);
	}

	public Level getLevel() {
		if (log == null) initlog();
		return log.getLevel();
	}

	public void info(Object message, Throwable t) {
		if (log == null) initlog();
		log.info(message, t);
	}

	public void info(Object message) {
		if (log == null) initlog();
		log.info(message);
	}

	public boolean isDebugEnabled() {
		if (log == null) initlog();
		return log.isDebugEnabled();
	}

	public boolean isEnabledFor(Priority level) {
		if (log == null) initlog();
		return log.isEnabledFor(level);
	}

	public boolean isInfoEnabled() {
		if (log == null) initlog();
		return log.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		if (log == null) initlog();
		return log.isTraceEnabled();
	}

	public void warn(Object message, Throwable t) {
		if (log == null) initlog();
		log.warn(message, t);
	}

	public void warn(Object message) {
		if (log == null) initlog();
		log.warn(message);
	}
	public void debug(String format, Object... args) {
		throw new RuntimeException("not support");
		
	}
	public void info(String format, Object... args) {
		throw new RuntimeException("not support");
		
	}
	public void warn(String format, Object... args) {
		throw new RuntimeException("not support");
		
	}
	public void fatal(String format, Object... args) {
		throw new RuntimeException("not support");
		
	}
	
	
}
