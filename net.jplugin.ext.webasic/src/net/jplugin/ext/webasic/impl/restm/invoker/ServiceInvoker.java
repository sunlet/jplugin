package net.jplugin.ext.webasic.impl.restm.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.SerializKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.api.Para;
import net.jplugin.ext.webasic.api.MethodFilterContext;
import net.jplugin.ext.webasic.impl.RemoteExceptionKits;
import net.jplugin.ext.webasic.impl.RemoteExceptionKits.RemoteExceptionInfo;
import net.jplugin.ext.webasic.impl.filter.IMethodCallback;
import net.jplugin.ext.webasic.impl.filter.service.ServiceFilterManager;
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
			ParaInfo paraInfo = getParaInfo(paraAnootation[i],i);
//			ParaInfo pi = getParaInfo(paraAnootation[i],i);
			ret[i] =  getFromRequest(paraMap,paraInfo,parameterTypes[i]);
		}
		return ret;
	}

	static class ParaInfo{
		String name;
		boolean required;
	}

	private Object getFromRequest(Map paraMap, ParaInfo paraInfo,
			Class<?> clz) {
		if (!paraMap.containsKey(paraInfo.name) && paraInfo.required){
			throw new RuntimeException("Can't find http param:"+paraInfo.name);
		}
		
		String val=(String) paraMap.get(paraInfo.name);
		
		if (StringKit.isNull(val)){
			return null;
		}else{
			return JsonKit.json2ObjectEx(val, clz);
		}
	}

	private ParaInfo getParaInfo(Annotation[] anno,int index) {
		ParaInfo pi = new ParaInfo();
		String paramName = null;
		for (Annotation a:anno){
			if (a.annotationType() == Para.class){
				paramName = ((Para)a).name().trim();
				pi.name = paramName;
				pi.required= ((Para)a).required();
				break;
			}
		}
		if (StringKit.isNull(paramName)){
			paramName = "arg"+index;
			pi.name = paramName;
			pi.required= false;
		}
		return pi;
	}

	public void call(CallParam cp) throws Throwable{
		int callType = cp.getCallType();
		if (callType==CallParam.CALLTYPE_STRING_PARAM)
			callStringParam(cp);
		else if (callType==CallParam.CALLTYPE_REMOTE_CALL)
			callRemteCall(cp);
		else if (callType==CallParam.CALLTYPE_JSON)
			callJson(cp);
		else throw new RuntimeException("known call type");
	}

	private void callJson(CallParam cp) throws Throwable {
		//目前转换为StringParam调用
		JsonCallHelper.convertToHttp(cp);
		callStringParam(cp);
	}

	private void callStringParam(CallParam cp) throws Throwable{
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
			
//			Object result = oam.method.invoke(oam.object, paraValue);
			Object result = null;
			
//			result = helper.invokeWithRuleSupport(oam,paraValue);
			result = invokeWithServiceFilter(cp.getPath(),oam,paraValue);

			State state = RestMethodState.get();

//			RuleResult rr = RuleResult.create(RuleResult.OK);
			JsonResult jr = JsonResult.create();
			jr.setCode(state.code);
			jr.setMsg(state.message);
			jr.setSuccess(state.success);
			
			HashMap<String, Object> hm = new HashMap();
			hm.put("result", result);
			jr.setContent(hm);
//			rr.setContent("return",result);
			cp.setResult(jr.toJson());
		}catch(InvocationTargetException e){
			Throwable targetEx = e.getTargetException();
			RemoteExceptionInfo exInfo = RemoteExceptionKits.getExceptionInfo(e.getTargetException());

			JsonResult jr = JsonResult.create();
			jr.setSuccess(false);
			jr.setMsg(exInfo.getMsg());//get message
			jr.setCode(exInfo.getCode());//get code
			cp.setResult(jr.toJson());

			ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName()).error(e.getTargetException());
		}
	}

	private Object invokeWithServiceFilter(String servicePath,final ObjectAndMethod oam, final Object[] paraValue) throws Throwable {
		MethodFilterContext ctx = new MethodFilterContext(servicePath,oam.object,oam.method,paraValue);

		return ServiceFilterManager.INSTANCE.executeWithFilter(ctx, new IMethodCallback() {
			public Object run() throws Throwable {
				return helper.invokeWithRuleSupport(oam,paraValue);
			}
		});
	}

	public ObjectCallHelper getObjectCallHelper() {
		return this.helper;
	}
	
	//<<<<<<<<<<<<<<<<<<<<<<以下调用RemoteCall
	private void callRemteCall(CallParam cp) throws Throwable {
		Class[] paraType = getParaTypes(cp);
		Object[] paraValue = getParaValues(cp);
		ObjectAndMethod oam = helper.get(cp.operation, paraType);
		
		try{
//			Object result = oam.method.invoke(oam.object, paraValue);
//			Object result = helper.invokeWithRuleSupport(oam,paraValue);
			Object result = invokeWithServiceFilter(cp.getPath(),oam,paraValue);
			cp.setResult(toResultString(result));
		}catch(InvocationTargetException e){
			Throwable targetEx = e.getTargetException();
			RemoteExceptionInfo exInfo = RemoteExceptionKits.getExceptionInfo(e.getTargetException());

			Object result = CallParam.REMOTE_EXCEPTION_PREFIX + JsonKit.object2Json(exInfo);
			cp.setResult(toResultString(result));
			log(e.getTargetException());
		}
	}

	/**
	 * @param cp
	 * @return
	 */
	private Object[] getParaValues(CallParam cp) {
		String s = cp.getParamMap().get(CallParam.REMOTE_PARAVALUES_KEY);
		if (StringKit.isNull(s)){
			throw new RuntimeException("para value is null.");
		}
		return (Object[]) SerializKit.deserialFromString(s);
	}
	/**
	 * @param cp
	 * @return
	 */
	private Class[] getParaTypes(CallParam cp) {
		String s = cp.getParamMap().get(CallParam.REMOTE_PARATYPES_KEY);
		if (StringKit.isNull(s)){
			throw new RuntimeException("para type is null.");
		}
		return (Class[]) SerializKit.deserialFromString(s);
	}
	/**
	 * @param result
	 * @return
	 */
	private String toResultString(Object result) {
		return SerializKit.encodeToString(result);
	}
	/**
	 * @param targetException
	 */
	private void log(Throwable targetException) {
		ServiceFactory.getService(ILogService.class).getLogger(this.getClass().getName()).error(targetException);
	}
	
	//以上调用RemoteCall>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
