package net.jplugin.ext.webasic.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 下午05:51:09
 **/

public interface IController {
	
	public void dohttp(String servicePath,HttpServletRequest req,HttpServletResponse res,String innerPath) throws Throwable;
	
}
