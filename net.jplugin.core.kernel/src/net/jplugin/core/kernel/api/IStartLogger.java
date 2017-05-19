package net.jplugin.core.kernel.api;

public interface IStartLogger {
	/**
	 * write to new line
	 * @param o
	 */
	public void log(Object o);
	/**
	 * write to new line
	 * @param o
	 * @param th
	 */
	public void log(Object o,Throwable th);
	/**
	 * within the same line
	 * @param s
	 */
	public void write(Object s);
}
