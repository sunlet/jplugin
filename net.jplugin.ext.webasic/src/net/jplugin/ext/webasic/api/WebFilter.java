package net.jplugin.ext.webasic.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-5 ����10:24:00
 **/

public interface WebFilter {
	/**
	 * �������true����������false���ټ���
	 * @param req
	 * @param res
	 * @return
	 */
	public boolean  doFilter(HttpServletRequest req,HttpServletResponse res);

	public void doAfter(HttpServletRequest req, HttpServletResponse res, Throwable th);
}
