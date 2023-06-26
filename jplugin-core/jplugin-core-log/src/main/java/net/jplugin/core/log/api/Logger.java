package net.jplugin.core.log.api;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-8 上午12:24:05
 **/

public interface Logger {
	public static final int DEBUG=Level.DEBUG_INT;
	public static final int INFO=Level.INFO_INT;
	public static final int WARN=Level.WARN_INT;
	public static final int ERROR=Level.ERROR_INT;
	public static final int FATAL=Level.FATAL_INT;

	public abstract void debug(String format, Object... args);
	public abstract void info(String format, Object... args);
	public abstract void warn(String format, Object... args);
	public abstract void error(String format, Object... args);
	public abstract void fatal(String format, Object... args);
	
	public abstract void debug(Object message, Throwable t);
	
	public abstract void debug(Object message);

	public abstract void error(Object message, Throwable t);

	public abstract void error(Object message);

	public abstract void fatal(Object message, Throwable t);

	public abstract void fatal(Object message);

	@Deprecated
	public abstract Level getLevel();

	public abstract int getLoggerLevel();

	public abstract void info(Object message, Throwable t);

	public abstract void info(Object message);

	public abstract boolean isDebugEnabled();

	@Deprecated
	public abstract boolean isEnabledFor(Priority level);

	public abstract boolean isEnabledFor(int level);

	public abstract boolean isInfoEnabled();

	public abstract boolean isTraceEnabled();

	public abstract boolean isErrorEnabled();

	public abstract boolean isWarnEnabled();

	public abstract void warn(Object message, Throwable t);

	public abstract void warn(Object message);

}