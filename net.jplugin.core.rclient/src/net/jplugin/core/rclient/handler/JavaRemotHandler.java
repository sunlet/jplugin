package net.jplugin.core.rclient.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.jplugin.common.kits.SerializKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientInfo;
import net.jplugin.core.rclient.api.IClientHandler;
import net.jplugin.core.rclient.api.RemoteExecuteException;

public class JavaRemotHandler implements IClientHandler{
	
	static final String PARATYPES = "TYPES";
	static final String PARAVALUES = "PARA";
	static final String REMOTE_EXCEPTION_PREFIX = "$RE#";
	static final String OPERATION_KEY = "_o";
	/**
	 * @return
	 */
//	public <T> T createProxyObject(final Client<T> c) {
//		//final ClientInfo clientInfo = c.getClientInfo();
//		
//		return (T)Proxy.newProxyInstance(c.getInterfaceClazz().getClassLoader(), new Class[]{c.getInterfaceClazz()}, new InvocationHandler() {
			
			public Object invoke(Client c,Object proxy, Method method, Object[] args)
					throws Throwable {
				if (c.getServiceBaseUrl()==null){
					throw new RuntimeException("Server url is null");
				}
				
				ClientInfo clientInfo = c.getClientInfo();
				HashMap< String, Object> map = new HashMap<String, Object>();
				map.put(PARATYPES, SerializKit.encodeToString(method.getParameterTypes()));
				map.put(PARAVALUES, SerializKit.encodeToString(args));
				map.put(OPERATION_KEY, method.getName());
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
//				if (PluginEnvirement.isUnitTesting()){
//					String requrl;
//					if (serverUrl.startsWith("http://localhost") || serverUrl.startsWith("https://localhost")){
//						requrl = serverUrl.substring(10); 
//						int splitpos = requrl.indexOf('/');
//						requrl = requrl.substring(splitpos+1);
//						HttpMock mock = HttpMock.create().servletPath(requrl+"/"+method.getName()+".do");
//						mock.request.getParameterMap().putAll(map);
//						ret = mock.invoke();
//					}else{
//						throw new RuntimeException("can't work in unit test mode");
//					}
//					
//				}else{
				String realUrl = ServiceUrlResolverManager.instance.resolveUrl(c.getProtocal(), c.getServiceBaseUrl());
				ret = HttpKit.post(realUrl+"/"+method.getName()+".do", map);
//				}
				if (StringKit.isNull(ret)){
					throw new RuntimeException("Server return null,perhaps can't find the controller or method not found");
				}
				Object retObj =  SerializKit.deserialFromString(ret);
				if (retObj!=null && retObj instanceof String ){
					if ( ((String)retObj).startsWith(REMOTE_EXCEPTION_PREFIX)){
						String stemp = (String)retObj;
						stemp = stemp.substring(REMOTE_EXCEPTION_PREFIX.length());
						throw new RemoteExecuteException("Remote exception:"+stemp);
					}
				}
				return retObj;
			}
//		});
//	}
}
