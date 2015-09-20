package net.jplugin.ext.webasic.impl.web.webex;

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
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.AbstractExController;
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

public class WebExController implements IController{
	private ClassDefine define;
	private static Class[] para=new Class[]{};
	
	/**
	 * @param value
	 */
	public WebExController(ClassDefine classDefine) {
		this.define = classDefine;
		if (!ReflactKit.isTypeOf(classDefine.getClazz(), AbstractExController.class)){
			throw new RuntimeException("The Object must extend the AbstractExController class");
		}
	}
	

	public void dohttp(HttpServletRequest req, HttpServletResponse res,String innerPath) throws Throwable{
		
		AbstractExController cont = (AbstractExController) this.define.getClazz().newInstance();
		cont._init(req, res);
//		String mname = req.getParameter(WebDriver.OPERATION_KEY);
		String mname = innerPath;
		if (StringKit.isNull(mname))
			mname = "index";
		
		Method method = this.define.getClazz().getMethod(mname, para);
		
		if (!method.getReturnType().equals(void.class)){
			throw new RuntimeException("Rule must return void");
		}
			
		try{
			method.invoke(cont, new Object[]{});
			//res.getWriter().print(result.getJson());
		}catch(InvocationTargetException e){
			throw ((InvocationTargetException)e).getTargetException();
		}
	}


}
