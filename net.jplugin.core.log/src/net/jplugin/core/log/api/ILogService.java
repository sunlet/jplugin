package net.jplugin.core.log.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-8 上午12:22:40
 **/

public interface ILogService {

	/**
	 * 获取系统日志Logger
	 * @param name
	 * @return
	 */
	public abstract Logger getLogger(String name);

	/**
	 * 获取特殊的专用Logger：名字是特殊的，并且additive=false，level=debug
	 * @param filename
	 * @return
	 */
	public abstract Logger getSpecicalLogger(String filename);

}