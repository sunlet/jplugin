package net.jplugin.core.rclient.api;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.rclient.handler.ClientHandlerRegistry;
import net.jplugin.core.rclient.handler.JavaRemotHandler;
import net.jplugin.core.rclient.handler.ClientProxyUtil;
import net.jplugin.core.rclient.handler.RestHandler;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-13 下午01:47:46
 **/

public class Client <T> {
	
	public static final String CLIENT_USERNAME="_clientUserName";
	public static final String CLIENT_TOKEN="_clientToken";
	
	public static final String PROTOCOL_REST="rest";
	public static final String PROTOCOL_NIOREST="niorest";
	public static final String PROTOCOL_REMOJAVA="remotejava";
	
	ClientInfo clientInfo;
	String serviceBaseUrl;
	Class<T> interfaceClazz;
	T  object;
	String protocal;
	
//	private static Map<String,IClientHandler> handlerMap = new HashMap<String,IClientHandler>(); 
//	static{
//		handlerMap.put(PROTOCOL_REST, new RestHandler());
//		handlerMap.put(PROTOCOL_REMOJAVA, new JavaRemotHandler());
//	}
	
	Client(Class<T> intf,String serverurl,ClientInfo ci){
		this.interfaceClazz = intf;
		this.serviceBaseUrl = serverurl;
		this.clientInfo = ci;
	}
	
	public String getProtocal() {
		return protocal;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}
	public void setServiceBaseUrl(String serverUrl) {
		this.serviceBaseUrl = serverUrl;
	}
	public Class<T> getInterfaceClazz() {
		return interfaceClazz;
	}
	public T getObject() {
		if (object == null){
			object = createProxyObject();
		}
		return object;
	}
	
	private T createProxyObject() {
		if (StringKit.isNull(this.protocal)){
			this.protocal = PROTOCOL_REMOJAVA;
		}
		
		IClientHandler handler = ClientHandlerRegistry.instance.getClientHandler(this.protocal);
//		IClientHandler handler = handlerMap.get(this.protocal);
		
		if (handler==null) throw new RuntimeException("Error protocal:"+this.protocal);
		return ClientProxyUtil.createProxyObject(this);
//		return handler.createProxyObject(this);
	}
	
//	/**
//	 * @return
//	 */
//	private T createProxyObject1() {
//		return (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class[]{interfaceClazz}, new InvocationHandler() {
//			
//			public Object invoke(Object proxy, Method method, Object[] args)
//					throws Throwable {
//				if (serviceBaseUrl==null){
//					throw new RuntimeException("Server url is null");
//				}
//				HashMap< String, Object> map = new HashMap<String, Object>();
//				map.put(PARATYPES, SerializKit.encodeToString(method.getParameterTypes()));
//				map.put(PARAVALUES, SerializKit.encodeToString(args));
//				map.put(OPERATION_KEY, method.getName());
//				if (clientInfo!=null){
//					map.put(CLIENT_USERNAME, clientInfo.getUsername());
//					map.put(CLIENT_TOKEN, clientInfo.getToken());
//					Map<String, String> extPara = clientInfo.getExtParas();
//					if (extPara!=null){
//						for (Entry<String, String> en:extPara.entrySet()){
//							map.put(en.getKey(), en.getValue());
//						}
//					}
//				}
//				String ret;
////				if (PluginEnvirement.isUnitTesting()){
////					String requrl;
////					if (serverUrl.startsWith("http://localhost") || serverUrl.startsWith("https://localhost")){
////						requrl = serverUrl.substring(10); 
////						int splitpos = requrl.indexOf('/');
////						requrl = requrl.substring(splitpos+1);
////						HttpMock mock = HttpMock.create().servletPath(requrl+"/"+method.getName()+".do");
////						mock.request.getParameterMap().putAll(map);
////						ret = mock.invoke();
////					}else{
////						throw new RuntimeException("can't work in unit test mode");
////					}
////					
////				}else{
//				ret = HttpKit.post(serviceBaseUrl+"/"+method.getName()+".do", map);
////				}
//				if (StringKit.isNull(ret)){
//					throw new RuntimeException("Server return null,perhaps can't find the controller or method not found");
//				}
//				Object retObj =  SerializKit.deserialFromString(ret);
//				if (retObj!=null && retObj instanceof String ){
//					if ( ((String)retObj).startsWith(REMOTE_EXCEPTION_PREFIX)){
//						String stemp = (String)retObj;
//						stemp = stemp.substring(REMOTE_EXCEPTION_PREFIX.length());
//						throw new RemoteExecuteException("Remote exception:"+stemp);
//					}
//				}
//				return retObj;
//			}
//		});
//	}
}
