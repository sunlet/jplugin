package net.jplugin.core.rclient.handler;

import java.util.List;
import java.io.IOException;
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
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientInfo;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.core.rclient.proxyfac.TokenFactory;
import net.jplugin.ext.webasic.api.Para;

public class RestHandler implements IClientHandler{
//	static final String PARATYPES = "TYPES";
//	static final String PARAVALUES = "PARA";
//	static final String REMOTE_EXCEPTION_PREFIX = "$RE#";
//	static final String OPERATION_KEY = "_o";
	
public static final String MIX_PARA = "_MIX_PARA_";
public static final Object MIX_PARA_VALUE = "1";
	




			public Object invoke(Client c,Object proxy, Method method, Object[] args)
					throws Throwable {
				if (c.getServiceBaseUrl()==null){
					throw new RuntimeException("Server url is null");
				}
				
				Class interfaceClazz = c.getInterfaceClazz();
				ClientInfo clientInfo = c.getClientInfo();
				
				HashMap< String, Object> map = new HashMap<String, Object>();
				map.put(MIX_PARA,MIX_PARA_VALUE );
				map.put(JsonResult4Client.JSON_FORMAT_INDICATOR, "1");
				
				Annotation[][] paraAnootation = method.getParameterAnnotations();
				if (args!=null){
					for (int i=0; i<args.length;i++){
						Object paraVal = args[i];
						String paraJsonVal = JsonKit.object2JsonEx(paraVal);
						String paraName = getParameterName(paraAnootation[i],i);
						map.put(paraName, paraJsonVal);
					}
				}
				
				//2020/12/15 开始放到Header里面
				Map<String,String > headerMap = null;
				if (clientInfo!=null){
					headerMap = new HashMap<String, String>();
					
					if (StringKit.isNotNull(clientInfo.getAppId()))
						headerMap.put(Client.CLIENT_HAID, clientInfo.getAppId());
					String token = TokenFactory.getAppToken();
					if (StringKit.isNotNull(token))
						headerMap.put(Client.CLIENT_HATK, token);
//					map.put(Client.CLIENT_APPID, clientInfo.getAppId());
//					map.put(Client.CLIENT_TOKEN, clientInfo.getAppToken());
					Map<String, String> extPara = clientInfo.getExtParas();
					if (extPara!=null){
						for (Entry<String, String> en:extPara.entrySet()){
							headerMap.put(en.getKey(), en.getValue());
						}
					}
				}
				String ret ;
				String realUrl = ServiceUrlResolverManager.instance.resolveUrl(c.getProtocal(), c.getServiceBaseUrl());
				ret = httpKitPost(realUrl+"/"+method.getName()+".do", map,headerMap);

				if (StringKit.isNull(ret)){
					throw new RuntimeException("Server return null,perhaps can't find the controller or method not found");
				}
				
				JsonResult4Client result = (JsonResult4Client) JsonKit.json2Object(ret,JsonResult4Client.class);
				if (result == null) throw new RuntimeException("parse server response json error:"+ret);
				if (!result.success){
					throw new RemoteExecuteException(result.getCode(),result.getMsg());
				}else{
					Object content = result.getContent();
					content = ((Map)content).get("result");
					Class<?> rettype = method.getReturnType();
					Type generictype = method.getGenericReturnType();
					return json2ObjectWithGenericType(JsonKit.object2Json(content),rettype,generictype);
				}
			}

			private String httpKitPost(String url, HashMap<String, Object> map, Map<String, String> headerMap) throws IOException, HttpStatusException {
				try{
					return HttpKit.postWithHeader(url, map,headerMap);
				}catch(Exception e){
//					ClientFailHandlerManager.connectFailed(Client.PROTOCOL_REST, url);
					//改用hutu访问http服务，这个版本暂时不处理链接失败的情况
					PluginEnvirement.getInstance().getStartLogger().log("改用hutu访问http服务，这个版本暂时不处理链接失败的情况!");
					throw e;
				}

//				}catch(org.apache.http.conn.HttpHostConnectException e){
//					ClientFailHandlerManager.connectFailed(Client.PROTOCOL_REST, url);
//					throw e;
//				}catch(org.apache.http.conn.ConnectTimeoutException e){
//					ClientFailHandlerManager.connectFailed(Client.PROTOCOL_REST, url);
//					throw e;
//				}
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
				
				//FULL MATCH的情况，直接用argX来传递；服务端会通过MIXED模式去处理，如果不是MIXED模式，就随他吧。
				if (StringKit.isNull(paramName) || paramName.equals(Para.FULL_MATCH)){
					paramName = "arg"+index;
				}
				return paramName;
			}
//		});
//	}

}
