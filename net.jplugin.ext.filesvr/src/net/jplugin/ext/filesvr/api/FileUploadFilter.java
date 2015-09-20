package net.jplugin.ext.filesvr.api;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:29:52
 **/

public interface FileUploadFilter {

	/**
	 * @param req
	 * @return
	 */
	boolean filter(HttpServletRequest req);

}
