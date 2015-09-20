package net.jplugin.ext.webasic.impl.rests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.core.ctx.api.RuleParameter;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.WebDriver;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper.ObjectAndMethod;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 下午05:51:42
 **/

public class ServiceController implements IController{
	ObjectCallHelper helper;
	private static Class[] para=new Class[]{RuleParameter.class};
	
	/**
	 * @param value
	 */
	public ServiceController(ObjectDefine d) {
		this.helper = new ObjectCallHelper(d);
	}
	

	public void dohttp(HttpServletRequest req, HttpServletResponse res,String innerPath) throws Throwable{
		
		ObjectAndMethod oam = helper.get(innerPath, para);
			
		if (!oam.method.getReturnType().equals(JsonResult.class)){
			throw new RuntimeException("Rule must return:"+JsonResult.class.getName());
		}
		
		RuleParameter para = makeParameter(req);
		try{
			JsonResult result = (JsonResult) oam.method.invoke(oam.object, para);
			res.getWriter().print(result.toJson());
		}catch(InvocationTargetException e){
			throw ((InvocationTargetException)e).getTargetException();
		}
	}


	/**
	 * @param req
	 * @return
	 */
	private RuleParameter makeParameter(HttpServletRequest req) {
		RuleParameter para = new RuleParameter();
		Enumeration nms = req.getParameterNames();
		while(nms.hasMoreElements()){
			String name = (String) nms.nextElement();
			para.getParameterMap().put(name, req.getParameter(name));
		}
		return para;
	}

}
