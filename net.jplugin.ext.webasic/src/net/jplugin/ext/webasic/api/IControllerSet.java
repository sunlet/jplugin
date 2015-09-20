package net.jplugin.ext.webasic.api;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:00:33
 **/

public interface IControllerSet {
	public void init();
	public void dohttp(String path,HttpServletRequest req,HttpServletResponse res,String innerPath) throws Throwable;
	public Set<String> getAcceptPaths();
}
