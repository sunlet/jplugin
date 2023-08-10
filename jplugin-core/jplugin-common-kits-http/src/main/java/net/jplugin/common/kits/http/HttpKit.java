package net.jplugin.common.kits.http;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.client.ClientInvocationManager;
import net.jplugin.common.kits.client.InvocationParam;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext.Method;
import net.jplugin.common.kits.http.mock.HttpMock;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;


public final class HttpKit {
	private static boolean unitTesting=false;
	private static final int TIMEOUT = 6000;

	private static FilterManager<HttpClientFilterContext> filterManager = new FilterManager<HttpClientFilterContext>();

	static{
		//注意：在这里设置一个初始值，在Plugin环境下，Kernel中会重新设置filterManager
		filterManager.addFilter((fc, ctx) -> HttpExecution.execute(ctx));
	}

	public static boolean isUnitTesting(){
		return unitTesting;
	}
	public static void setUnitTesting(boolean b) {
		unitTesting = b;
	}
	

	@Deprecated
	public static String _post(String url, Map<String,Object> datas,Map<String,String> extHeaders) throws  IOException, HttpStatusException{
		return _handleWithEntity(Method.POST,url,datas,extHeaders);
	}
	public static String _handleWithEntity(Method method,String url, Map<String,Object> datas,Map<String,String> extHeaders) throws  IOException, HttpStatusException{
		if (isUnitTesting()){
			if (url.startsWith("http://localhost") || url.startsWith("https://localhost")){
				return executeDummy(url,datas,extHeaders);
			}
		}
		
		HttpRequest httpPost = createRequest(method,url);
		

		if (datas!=null && extHeaders!=null && isJsonFormat(extHeaders) ){
			httpPost.body(JsonKit.object2JsonEx(datas));
		}else {
			Map<String, String> params = new HashMap<>();
			if (datas != null) {
				for (Entry<String, Object> entry : datas.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					if(value==null)	value = "";
					if(key!=null) {
						params.put(key, JsonKit.object2JsonEx(value));
					}
				}
				httpPost.formStr(params);
			}
		}

		//设置headers
		httpPost.addHeaders(extHeaders);
		
		//下面设置http调用参数
		useInvokeParam(httpPost);

		//调用
		return handleResponse(httpPost);
	}

	private static boolean isJsonFormat(Map<String, String> extHeaders) {
		return ContentKit.isApplicationJson(extHeaders.get("Content-Type"));
	}

	private static void useInvokeParam(HttpRequest request) {
		InvocationParam invokeParam = ClientInvocationManager.INSTANCE.getAndClearParam();
		if (invokeParam != null) {
			//三个超时时间设置相同的值
			int soTimeout = invokeParam.getServiceTimeOut();
			if (soTimeout != 0) {
				request.timeout(soTimeout);
			}
		}
	}

	private static String executeDummy(String url, Map<String, Object> datas, Map<String, String> extHeaders) {
		if (url.indexOf('?')>=0)
			throw new RuntimeException("not impl get mode");

		HttpMock mock = HttpMock.createFromUrl(url);
		if (datas!=null){
			mock.request.putAllParameter(datas,extHeaders);
		}
		return mock.invoke();
	}

	@Deprecated
	public static String _get(String url,Map<String,String> extHeaders) throws IOException, HttpStatusException {
		return _handleWithoutEntity(Method.GET,url,extHeaders);
	}
	public static String _handleWithoutEntity(Method method,String url,Map<String,String> extHeaders) throws IOException, HttpStatusException {
		if (isUnitTesting()){
			if (url.startsWith("http://localhost") || url.startsWith("https://localhost")){
				return executeDummy(url,null,extHeaders);
			}
		}
		
		
		HttpRequest request = createRequest(method,url);


		//设置headers
		request.addHeaders(extHeaders);
		
		//单独处理cookie，目前cookie的传递也是放在header里的，hutool框架对cookie是单独处理的
		String cookie = extHeaders.get(Header.COOKIE.getValue());
		if (StringKit.isNotNullAndBlank(cookie)) {
			request.cookie(cookie);
		} else {
			//适配下cookie的key全小写
			String c = extHeaders.get(Header.COOKIE.getValue().toLowerCase());
			request.cookie(c);
		}
		
		//下面设置http调用参数
		useInvokeParam(request);

		return handleResponse(request);
	}

	private static HttpRequest createRequest(Method method, String url) {
		switch(method){
			case POST:
				return HttpRequest.of(url).setMethod(cn.hutool.http.Method.POST).timeout(TIMEOUT).cookie("");
			case GET:
				return HttpRequest.of(url).setMethod(cn.hutool.http.Method.GET).timeout(TIMEOUT).cookie("");
			case PUT:
				return HttpRequest.of(url).setMethod(cn.hutool.http.Method.PUT).timeout(TIMEOUT).cookie("");
			case DELETE:
				return HttpRequest.of(url).setMethod(cn.hutool.http.Method.DELETE).timeout(TIMEOUT).cookie("");
			default:
				throw new RuntimeException("not support method:"+method);
		}
	}

	public static String handleResponse(HttpRequest request) throws IOException, HttpStatusException {
		String responseText = "";
		
		if(request.header("Connection")==null){
			request.header("Connection", "close");
		}


		try (HttpResponse response = request.execute()){
			if (response != null) {
				int code = response.getStatus();
				if (code>=200 && code <=206){ //根据http协议，2xx都是成功返回
					responseText = response.body();
				}else{
					throw new HttpStatusException("Status Error:"+code);
				}
			}
		}catch(Exception ex){
			//继续抛出异常
			if (ex instanceof HttpStatusException)
				throw (HttpStatusException)ex;
			throw new RuntimeException(ex.getMessage(),ex);
		}

		return responseText;
	}

	public static String encodeParam(String param){
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException, HttpStatusException {
//		String s = HttpKit.get("http://localhost:8080/k-rpms-web/role/index.do");
		String s = HttpKit.get("http://192.133.212.11/32234");
		System.out.println(s);
	}

	public static String post(String url, Map<String, Object> params) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.POST, url, params);
		return (String) filterManager.filter(ctx);
	}

	public static String postWithHeader(String url, Map<String, Object> params,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.POST, url, params,headers);
		return (String) filterManager.filter(ctx);
	}

	/**
	 * 以Json格式传递参数，Json需预先转换为MapList嵌套结构。
	 * @param url
	 * @param params  Json对应的MapList结构。
	 * @param headers 额外的Header，可以传null。
	 * @return
	 * @throws IOException
	 * @throws HttpStatusException
	 */
	public static String postJsonWithHeader(String url, Map<String, Object> params,Map<String,String> headers) throws IOException, HttpStatusException {
		Map<String, String> headMap = new HashMap<>();
		if (headers!=null){
			headMap.putAll(headers);
		}

		String contentTypeValue = headMap.get("Content-Type");
		if (StringKit.isNull(contentTypeValue)){
			//没有JsonHeader，增加Json格式对应的Header
			headMap.put("Content-Type", "application/json");
		}else{
			//包含header，则必须为Json格式
			if (!ContentKit.isApplicationJson(contentTypeValue)){
				throw new RuntimeException("Content-Type is set,but not json type!");
			}
		}

		return postWithHeader(url, params, headMap);
	}

	/**
	 * 以Json格式传递参数，参数为Json字符串
	 * @param url
	 * @param json  json结构的字符串。目前必须是{}结构。
	 * @param headers
	 * @return
	 * @throws IOException
	 * @throws HttpStatusException
	 */
	public static String postJsonWithHeader(String url, String json,Map<String,String> headers) throws IOException, HttpStatusException {
		if (!json.startsWith("{")){
			throw new RuntimeException("json must start with '{'");
		}
		return postJsonWithHeader(url, JsonKit.json2Map(json), headers);
	}
	/**
	 * 用Json格式提交，参数为json字符串
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws HttpStatusException
	 */
	public static String postJson(String url, String json) throws IOException, HttpStatusException {
		return postJsonWithHeader(url, json, null);
	}

	/**
	 * 用json格式提交，json需要提前转换为MapList嵌套结构
	 * @param url
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws HttpStatusException
	 */
	public static String postJson(String url, Map<String, Object> json) throws IOException, HttpStatusException {
		return postJsonWithHeader(url, json, null);
	}


	public static String putWithHeader(String url, Map<String, Object> params,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.PUT, url, params,headers);
		return (String) filterManager.filter(ctx);
	}

	public static String get(String url) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.GET, url, null);
		return (String) filterManager.filter(ctx);
	}

	public static String getWithHeader(String url,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.GET, url, null,headers);
		return (String) filterManager.filter(ctx);
	}
	public static String deleteWithHeader(String url,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(Method.DELETE, url, null,headers);
		return (String) filterManager.filter(ctx);
	}

	public static void _setHttpFilterManager(FilterManager<HttpClientFilterContext> fm) {
		filterManager = fm;
	}

	public static class HttpExecution {
		public static Object execute(HttpClientFilterContext ctx) throws Throwable{
			//在这里验证，因为filter过程可能修改
			validate(ctx);
			//call
			Method method = ctx.getMethod();
			if (method== Method.POST || method== Method.PUT){
				return HttpKit._handleWithEntity(method,ctx.getUrl(), ctx.getParams(),ctx.getHeaders());
			}else if (method== Method.GET || method== Method.DELETE){
				return HttpKit._handleWithoutEntity(method,ctx.getUrl(),ctx.getHeaders());
			}
			throw new RuntimeException("not supported http method:"+method);
		}

		private static void validate(HttpClientFilterContext ctx) {
			Method m = ctx.getMethod();
			Map<String, Object> params = ctx.getParams();
			//validate
			AssertKit.assertNotNull(m, "http method");
			//get, params must empty
			AssertKit.assertTrue(m==Method.POST || m==Method.PUT || ( (m==Method.GET || m==Method.DELETE) && (params==null || params.isEmpty())));
		}
	}

}
