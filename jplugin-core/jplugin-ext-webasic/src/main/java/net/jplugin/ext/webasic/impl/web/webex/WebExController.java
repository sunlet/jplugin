package net.jplugin.ext.webasic.impl.web.webex;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.RuleProxyHelper;
import net.jplugin.ext.webasic.api.AbstractExController;
import net.jplugin.ext.webasic.api.IController;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.impl.filter.IMethodCallback;
import net.jplugin.ext.webasic.impl.filter.MethodIllegleAccessException;
import net.jplugin.ext.webasic.impl.filter.webctrl.WebCtrlFilterManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 下午05:51:35
 **/

public class WebExController implements IController{
//	private ClassDefine define;
	private static Class[] para=new Class[]{};
	private AbstractExController object;
	
	public Object getObject() {
		return this.object;
	}
//	/**
//	 * @param value
//	 */
//	public WebExController(ClassDefine classDefine) {
//		this.define = classDefine;
//		if (!ReflactKit.isTypeOf(classDefine.getClazz(), AbstractExController.class)){
//			throw new RuntimeException("The Object must extend the AbstractExController class");
//		}
//		try {
//			this.object = (AbstractExController) this.define.getClazz().newInstance();
//			PluginEnvirement.INSTANCE.resolveRefAnnotation(object);
//		} catch (InstantiationException e) {
//			throw new RuntimeException(e);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
//	}
	public WebExController(Object obj) {
		if (!ReflactKit.isTypeOf(obj.getClass(), AbstractExController.class)){
			throw new RuntimeException("The Object must extend the AbstractExController class");
		}
		this.object = (AbstractExController) obj;
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath) throws Throwable{
		
		final AbstractExController cont = object;
		cont._init(req, res);
//		String mname = req.getParameter(WebDriver.OPERATION_KEY);
		String mname = innerPath;
		if (StringKit.isNull(mname))
			mname = "index";
		
//		final Method method = this.define.getClazz().getMethod(mname, para);
		final Method method = this.object.getClass().getMethod(mname, para);
		
		if (!method.getReturnType().equals(void.class)){
			throw new RuntimeException("Rule must return void");
		}
			
		try{
			final Object[] args = new Object[]{};
			
			InvocationContext mfc = new InvocationContext(path, cont, method, args);
			WebCtrlFilterManager.INSTANCE.executeWithFilter(mfc, new IMethodCallback() {
				public Object run() throws Throwable {
					return RuleProxyHelper.invokeWithRule(cont, method, args);
				}
			});
			
			//res.getWriter().print(result.getJson());
		}catch(MethodIllegleAccessException e1){
			//无权限，返回空内容
		}catch(InvocationTargetException e){
			throw ((InvocationTargetException)e).getTargetException();
		}
	}


}
