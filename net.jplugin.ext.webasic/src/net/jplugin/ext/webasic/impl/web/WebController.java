package net.jplugin.ext.webasic.impl.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.RuleParameter;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.WebDriver;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper.ObjectAndMethod;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 下午05:51:35
 **/

public class WebController implements IController{
	ObjectCallHelper helper;
	private static Class[] para=new Class[]{HttpServletRequest.class,HttpServletResponse.class};
	
	/**
	 * @param value
	 */
	public WebController(ObjectDefine d) {
		this.helper = new ObjectCallHelper(d);
	}
	

	public void dohttp(HttpServletRequest req, HttpServletResponse res,String innerPath) throws Throwable{
		
		ObjectAndMethod oam = helper.get(innerPath, para);
			
		if (!oam.method.getReturnType().equals(void.class)){
			throw new RuntimeException("Rule must return void");
		}
		
		try{
			oam.method.invoke(oam.object, new Object[]{req,res});
			//res.getWriter().print(result.getJson());
		}catch(InvocationTargetException e){
			throw ((InvocationTargetException)e).getTargetException();
		}
	}


}
