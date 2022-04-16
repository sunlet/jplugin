package net.jplugin.ext.webasic.impl.restm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.api.IControllerSet;
import net.jplugin.ext.webasic.api.ObjectDefine;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;
import net.jplugin.ext.webasic.impl.restm.invoker.IServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvoker;
import net.jplugin.ext.webasic.impl.restm.invoker.ServiceInvokerSet;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:02:12
 **/

public class RestMethodControllerSet4Invoker implements IControllerSet{

	Set<String> servicePathSet = new HashSet();
	public void init() {
		Map<String, Object> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD,Object.class);
//		Map<String, ObjectDefine> defs = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.ext.webasic.Plugin.EP_RESTMETHOD,ObjectDefine.class);
		servicePathSet.addAll(defs.keySet());
		ServiceInvokerSet.instance.addServices(defs);
	}
	
	public Set<String> getAcceptPaths() {
		return servicePathSet;
//		return ServiceInvokerSet.instance.getAcceptPaths();
	}

	public void dohttp(String path,HttpServletRequest req, HttpServletResponse res,String innerPath)
			throws Throwable {
		CallParam callParam;
		
		RequesterInfo requestInfo = ThreadLocalContextManager.getRequestInfo();
		if (ContentKit.isApplicationJson(requestInfo.getContent().getContentType())){
//			InputStream is = null;
//			String json;
//			try {
//				req.getInputStream();
//				ByteArrayOutputStream os = new ByteArrayOutputStream();
//				byte[] buffer = new byte[512];
//				int len;
//				is = req.getInputStream();
//				if (is==null){
//					json="";
//				}else{
//					while ((len = is.read(buffer)) > 0) {
//						os.write(buffer, 0, len);
//					}
//					json = os.toString("utf-8");
//				}
//			} catch (IOException e) {
//				throw new RuntimeException("get json error:"+e.getMessage(),e);	
//			} finally{
//				if (is!=null) 
//					is.close();
//			}
			String json = requestInfo.getContent().getJsonContent();
			Map<String,String> reqMap = new HashMap<String, String>();
			reqMap.put(CallParam.JSON_KEY,json );
			callParam = CallParam.create(CallParam.CALLTYPE_JSON,path,innerPath, reqMap);
		}else{
//			Enumeration<String> names = req.getParameterNames();
//			String name = null;
//			Map<String,String> reqMap = new HashMap<String, String>();
//			while (names.hasMoreElements()){
//				name = names.nextElement();
//				reqMap.put(name, req.getParameter(name));
//			}
			Map<String,String> reqMap = requestInfo.getContent().getParamContent();
			callParam = CallParam.create(path,innerPath, reqMap);
		}

		ServiceInvokerSet.instance.call(callParam);
		
		//2020-12 为了支持生成代码，增加判断
		Boolean flag = (Boolean) ThreadLocalContextManager.getCurrentContext().getAttribute(net.jplugin.ext.webasic.impl.restm.Constants.NOT_WRITE_RESULT);
		if (flag==null || flag) {
			String result = callParam.getResult();
			
			res.setCharacterEncoding("utf-8");
			res.setContentType("text/json");
			res.getWriter().print(result);
			//controllerMap.get(path).dohttp(req, res,innerPath);
		}

	}
}
