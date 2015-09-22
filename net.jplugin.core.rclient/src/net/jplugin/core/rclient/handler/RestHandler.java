package net.jplugin.core.rclient.handler;

import java.util.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientInfo;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.ext.webasic.api.Para;

public class RestHandler implements IClientHandler{
//	static final String PARATYPES = "TYPES";
//	static final String PARAVALUES = "PARA";
//	static final String REMOTE_EXCEPTION_PREFIX = "$RE#";
//	static final String OPERATION_KEY = "_o";
	
	public <T> T createProxyObject(final Client<T> c) {
		
		return (T)Proxy.newProxyInstance(c.getInterfaceClazz().getClassLoader(), new Class[]{c.getInterfaceClazz()}, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if (c.getServiceBaseUrl()==null){
					throw new RuntimeException("Server url is null");
				}
				
				Class interfaceClazz = c.getInterfaceClazz();
				ClientInfo clientInfo = c.getClientInfo();
				
				HashMap< String, Object> map = new HashMap<String, Object>();
				Annotation[][] paraAnootation = method.getParameterAnnotations();
				if (args!=null){
					for (int i=0; i<args.length;i++){
						Object paraVal = args[i];
						String paraJsonVal = JsonKit.object2JsonEx(paraVal);
						String paraName = getParameterName(paraAnootation[i],i);
						map.put(paraName, paraJsonVal);
					}
				}
				
				if (clientInfo!=null){
					map.put(Client.CLIENT_USERNAME, clientInfo.getUsername());
					map.put(Client.CLIENT_TOKEN, clientInfo.getToken());
					Map<String, String> extPara = clientInfo.getExtParas();
					if (extPara!=null){
						for (Entry<String, String> en:extPara.entrySet()){
							map.put(en.getKey(), en.getValue());
						}
					}
				}
				String ret;
				ret = HttpKit.post(c.getServiceBaseUrl()+"/"+method.getName()+".do", map);

				if (StringKit.isNull(ret)){
					throw new RuntimeException("Server return null,perhaps can't find the controller or method not found");
				}
				
				JsonResult4Client result = (JsonResult4Client) JsonKit.json2Object(ret,JsonResult4Client.class);
				if (result == null) throw new RuntimeException("parse server response json error:"+ret);
				if (!result.success){
					throw new RemoteExecuteException("Code="+result.getCode()+" info="+result.getMsg());
				}else{
					Object content = result.getContent();
					content = ((Map)content).get("return");
					Class<?> rettype = method.getReturnType();
					Type generictype = method.getGenericReturnType();
					return json2ObjectWithGenericType(JsonKit.object2Json(content),rettype,generictype);
				}
			}

			private Object json2ObjectWithGenericType(String content, Class<?> rettype, Type generictype) {
				if (rettype.isAssignableFrom(List.class)){
					if (generictype != null){
						if (generictype instanceof ParameterizedType){
							 ParameterizedType pt = (ParameterizedType) generictype;  
							 Class genericClazz = (Class)pt.getActualTypeArguments()[0]; 
							 return JsonKit.json2ListBean(content, genericClazz);
						}else{
							//continue
						}
					}
				}
				if (rettype.isAssignableFrom(Map.class)){
					if (generictype != null){
						if (generictype instanceof ParameterizedType){
							 ParameterizedType pt = (ParameterizedType) generictype;  
							 Class keyClazz = (Class)pt.getActualTypeArguments()[0]; 
							 Class valClazz = (Class)pt.getActualTypeArguments()[1]; 
							 return JsonKit.json2MapBean(content, keyClazz,valClazz);
						}else{
							//continue
						}
					}
				}
				return JsonKit.json2Object(content, rettype);
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
		});
	}

}
