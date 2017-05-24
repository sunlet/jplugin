package net.jplugin.ext.filesvr.api;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:30:56
 **/

public interface FileDownloadFilter {
	/**
	 * @param req
	 * @return
	 */
	public boolean filter(HttpServletRequest req) ;

}
