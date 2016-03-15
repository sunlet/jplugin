package net.jplugin.ext.webasic.impl.rmethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.SerializKit;
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

public class RmethodController implements IController{
	private static final String PARATYPES = "TYPES";
	private static final String PARAVALUES = "PARA";
	private static final String REMOTE_EXCEPTION_PREFIX = "$RE#";
	ObjectCallHelper helper;
	
	/**
	 * @param value
	 */
	public RmethodController(ObjectDefine d) {
		this.helper = new ObjectCallHelper(d);
	}
	
	public ObjectCallHelper getObjectCallHelper(){
		return helper;
	}

	public void dohttp(HttpServletRequest req, HttpServletResponse res,String innerPath) throws Throwable{
		
		Class[] paraType = getParaTypes(req);
		Object[] paraValue = getParaValues(req);
		ObjectAndMethod oam = helper.get(innerPath, paraType);
		
		try{
//			Object result = oam.method.invoke(oam.object, paraValue);
			
			Object result = helper.invokeWithRuleSupport(oam,paraValue);
			res.getWriter().print(toResultString(result));
		}catch(InvocationTargetException e){
			Object result = REMOTE_EXCEPTION_PREFIX + "  cls="+e.getTargetException().getClass().getName()+" msg="+e.getTargetException().getMessage();
			res.getWriter().print(toResultString(result));
			log(e.getTargetException());
		}
	}


	/**
	 * @param targetException
	 */
	private void log(Throwable targetException) {
		ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName()).error(targetException);
	}


	/**
	 * @param result
	 * @return
	 */
	private String toResultString(Object result) {
		return SerializKit.encodeToString(result);
	}


	/**
	 * @param req
	 * @return
	 */
	private Object[] getParaValues(HttpServletRequest req) {
		String s = req.getParameter(PARAVALUES);
		if (StringKit.isNull(s)){
			throw new RuntimeException("para value is null.");
		}
		return (Object[]) SerializKit.deserialFromString(s);
	}


	/**
	 * @param req
	 * @return
	 */
	private Class[] getParaTypes(HttpServletRequest req) {
		String s = req.getParameter(PARATYPES);
		if (StringKit.isNull(s)){
			throw new RuntimeException("para type is null.");
		}
		return (Class[]) SerializKit.deserialFromString(s);
	}


}
