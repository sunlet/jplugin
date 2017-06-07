package net.jplugin.core.log.api;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-8 上午12:24:05
 **/

public interface Logger {

	public abstract void debug(Object message, Throwable t);

	public abstract void debug(Object message);

	public abstract void error(Object message, Throwable t);

	public abstract void error(Object message);

	public abstract void fatal(Object message, Throwable t);

	public abstract void fatal(Object message);

	public abstract Level getLevel();

	public abstract void info(Object message, Throwable t);

	public abstract void info(Object message);

	public abstract boolean isDebugEnabled();

	public abstract boolean isEnabledFor(Priority level);

	public abstract boolean isInfoEnabled();

	public abstract boolean isTraceEnabled();

	public abstract void warn(Object message, Throwable t);

	public abstract void warn(Object message);

}