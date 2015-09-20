package net.jplugin.core.log;

import net.jplugin.core.log.api.Logger;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午11:35:49
 **/

public class Logger4Log4j implements Logger {
	org.apache.log4j.Logger innerLogger;

	Logger4Log4j (org.apache.log4j.Logger l){
		this.innerLogger = l;
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#debug(java.lang.Object, java.lang.Throwable)
	 */
	public void debug(Object message, Throwable t) {
		innerLogger.debug(message, t);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#debug(java.lang.Object)
	 */
	public void debug(Object message) {
		innerLogger.debug(message);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#error(java.lang.Object, java.lang.Throwable)
	 */
	public void error(Object message, Throwable t) {
		innerLogger.error(message, t);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#error(java.lang.Object)
	 */
	public void error(Object message) {
		innerLogger.error(message);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#fatal(java.lang.Object, java.lang.Throwable)
	 */
	public void fatal(Object message, Throwable t) {
		innerLogger.fatal(message, t);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#fatal(java.lang.Object)
	 */
	public void fatal(Object message) {
		innerLogger.fatal(message);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#getLevel()
	 */
	public final Level getLevel() {
		return innerLogger.getLevel();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#info(java.lang.Object, java.lang.Throwable)
	 */
	public void info(Object message, Throwable t) {
		innerLogger.info(message, t);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#info(java.lang.Object)
	 */
	public void info(Object message) {
		innerLogger.info(message);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return innerLogger.isDebugEnabled();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#isEnabledFor(org.apache.log4j.Priority)
	 */
	public boolean isEnabledFor(Priority level) {
		return innerLogger.isEnabledFor(level);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return innerLogger.isInfoEnabled();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return innerLogger.isTraceEnabled();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#warn(java.lang.Object, java.lang.Throwable)
	 */
	public void warn(Object message, Throwable t) {
		innerLogger.warn(message, t);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogger#warn(java.lang.Object)
	 */
	public void warn(Object message) {
		innerLogger.warn(message);
	}
}
