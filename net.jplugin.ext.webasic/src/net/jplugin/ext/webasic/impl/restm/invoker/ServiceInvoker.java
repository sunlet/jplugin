package net.jplugin.ext.webasic.impl.restm.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.api.Para;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper;
import net.jplugin.ext.webasic.impl.helper.ObjectCallHelper.ObjectAndMethod;
import net.jplugin.ext.webasic.impl.restm.RestMethodState;
import net.jplugin.ext.webasic.impl.restm.RestMethodState.State;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 下午05:51:42
 **/

public class ServiceInvoker implements IServiceInvoker{
	ObjectCallHelper helper;
	
	/**
	 * @param value
	 */
	public ServiceInvoker(ObjectDefine d) {
		this.helper = new ObjectCallHelper(d);
	}

	/**
	 * @param req
	 * @param paraAnootation
	 * @param parameterTypes
	 * @return
	 */
	private Object[] getParaValueFromRequest(CallParam req,
			Annotation[][] paraAnootation, Class<?>[] parameterTypes) {
		if (paraAnootation.length != parameterTypes.length){
			throw new RuntimeException("Method mismatch!");
		}
		Object[] ret = new Object[parameterTypes.length];
		Map paraMap = req.getParamMap();
		for (int i=0;i<parameterTypes.length;i++){
			String paramName = getParameterName(paraAnootation[i],i);
			ret[i] =  getFromRequest(paraMap,paramName,parameterTypes[i]);
		}
		return ret;
	}

	private Object getFromRequest(Map paraMap, String paramName,
			Class<?> clz) {
		if (!paraMap.containsKey(paramName)){
			throw new RuntimeException("Can't find http param:"+paramName);
		}
		
		String val=(String) paraMap.get(paramName);
		
		if (StringKit.isNull(val)){
			return null;
		}else{
			return JsonKit.json2ObjectEx(val, clz);
		}
	}

	private String getParameterName(Annotation[] anno,int index) {
		String paramName = null;
		for (Annotation a:anno){
			if (a.annotationType() == Para.class){
				paramName = ((Para)a).name().trim();
				break;
			}
		}
		if (StringKit.isNull(paramName)){
			paramName = "arg"+index;
		}
		return paramName;
	}

	public void call(CallParam cp) throws Throwable{
		ObjectAndMethod oam = helper.get(cp.getOperation(), null);
		
		//得到参数annotation
		Annotation[][] paraAnootation;
		if (ObjectDefine.OBJ_BIZLOGIC.equals(helper.getObjeceDefine().getObjType())){
			//从接口获取meta
			Class intf = RuleServiceFactory.getRuleInterface(helper.getObjeceDefine().getBlName());
			paraAnootation = ReflactKit.findSingeMethodExactly(intf,oam.method.getName()).getParameterAnnotations();
		}else{
			//从类获取meta
			paraAnootation = oam.method.getParameterAnnotations();
		}
		
		//获取参数的值
		Object[] paraValue = getParaValueFromRequest(cp,paraAnootation,oam.method.getParameterTypes());
		
		try{
			RestMethodState.reset();
			Object result = oam.method.invoke(oam.object, paraValue);
			State state = RestMethodState.get();

//			RuleResult rr = RuleResult.create(RuleResult.OK);
			JsonResult jr = JsonResult.create();
			jr.setCode(state.code);
			jr.setMsg(state.message);
			jr.setSuccess(state.success);
			
			HashMap<String, Object> hm = new HashMap();
			hm.put("return", result);
			jr.setContent(hm);
//			rr.setContent("return",result);
			cp.setResult(jr.toJson());
		}catch(InvocationTargetException e){
			JsonResult jr = JsonResult.create();
			jr.setSuccess(false);
			jr.setMsg(e.getMessage());
			jr.setCode("-1");
			e.getTargetException().printStackTrace();
			ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName()).error(e.getTargetException());
		}
	}
}
