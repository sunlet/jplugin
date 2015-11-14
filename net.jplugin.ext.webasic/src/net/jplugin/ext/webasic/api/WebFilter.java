package net.jplugin.ext.webasic.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-5 上午10:24:00
 **/

public interface WebFilter {
	/**
	 * 如果返回true继续，返回false不再继续
	 * @param req
	 * @param res
	 * @return
	 */
	public boolean  doFilter(HttpServletRequest req,HttpServletResponse res);

	public void doAfter(HttpServletRequest req, HttpServletResponse res, Throwable th);
}
