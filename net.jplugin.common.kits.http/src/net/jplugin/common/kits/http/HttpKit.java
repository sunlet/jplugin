package net.jplugin.common.kits.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.client.ClientInvocationManager;
import net.jplugin.common.kits.client.InvocationParam;
import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext;
import net.jplugin.common.kits.http.filter.HttpClientFilterContext.Method;
import net.jplugin.common.kits.http.mock.HttpMock;


public final class HttpKit{
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024; //8KB
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private static       int     maxConnections         = 200; //http请求最大并发连接数
    private static       int     maxConnectionsPerRoute = 20; //http请求最大并发连接数
    private static       int     socketTimeout          = 6; //超时时间，默认20秒
    private static       int     maxRetries             = 5;//错误尝试次数，错误异常表请在RetryHandler添加
    private static       int     httpThreadCount        = 3;//http线程池数量
	private static final Charset UTF_8                  = Charset.forName("UTF-8");

    private static boolean unitTesting=false;

	private static HttpClientBuilder clientBuilder;
	private static FilterManager<HttpClientFilterContext> filterManager = new FilterManager<HttpClientFilterContext>();
	
//	static class FinalFilter implements 
	
	static{
		//注意：在这里设置一个初始值，在Plugin环境下，Kernel中会重新设置filterManager
		filterManager.addFilter(new IFilter<HttpClientFilterContext>() {
			public Object filter(FilterChain fc, HttpClientFilterContext ctx) throws Throwable {
				return HttpExecution.execute(ctx);
			}
		});
	}
	
	static{
		clientBuilder = initHttpClientBuilder();
	}

	public static boolean isUnitTesting(){
		return unitTesting;
	}
	public static void setUnitTesting(boolean b) {
		unitTesting = b;
	}

	private static HttpClientBuilder initHttpClientBuilder() {
		final HttpClientBuilder builder = HttpClientBuilder.create();
		try {
			//请求配置
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(socketTimeout * 1000)
					.setConnectionRequestTimeout(socketTimeout * 1000)
					.setSocketTimeout(socketTimeout * 1000).build();
			builder.setDefaultRequestConfig(config);

			//socket配置
			SocketConfig socketConfig = SocketConfig.custom()
					.setTcpNoDelay(true)
					.build();

			//连接属性配置
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setCharset(UTF_8)
					.setBufferSize(DEFAULT_SOCKET_BUFFER_SIZE)
					.build();

			// 请求重试处理
			HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
				public boolean retryRequest(IOException exception,
											int executionCount, HttpContext context) {
					if (executionCount >= maxRetries) {// 如果已经重试指定次数了，就放弃
						return false;
					}
					if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
						return true;
					}
					if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
						return false;
					}
					if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
						return false;
					}
					if (exception instanceof InterruptedIOException) {// 超时
						return false;
					}
					if (exception instanceof UnknownHostException) {// 目标服务器不可达
						return false;
					}
					if (exception instanceof SSLException) {// SSL握手异常
						return false;
					}
					HttpClientContext clientContext = HttpClientContext
							.adapt(context);
					HttpRequest request = clientContext.getRequest();
					// 如果请求是幂等的，就再次尝试
					if (!(request instanceof HttpEntityEnclosingRequest)) {
						return true;
					}
					return false;
				}
			};


			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			}).build();

			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)).build();

			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(maxConnections);  
			cm.setDefaultMaxPerRoute(maxConnectionsPerRoute);
			cm.setDefaultSocketConfig(socketConfig);
			cm.setDefaultConnectionConfig(connectionConfig);
			builder.setConnectionManager(cm);
			builder.setRetryHandler(httpRequestRetryHandler);
			return builder;
		} catch (Exception e) {
			e.printStackTrace();
			return builder;
		}
	}

	private static CloseableHttpClient createHttpClient() {
		return clientBuilder.build();
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
		
		CloseableHttpClient httpClient = createHttpClient();
		HttpEntityEnclosingRequestBase httpPost = (HttpEntityEnclosingRequestBase) createRequest(method,url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		Set<Entry<String,Object>> temps = datas.entrySet();
		for(Entry<String,Object> temp : temps){
			String key = temp.getKey();
			Object value = temp.getValue();
			if(value==null)	value = "";
			if(key!=null)
				params.add(new BasicNameValuePair(key, JsonKit.object2JsonEx(value)));
//				params.add(new BasicNameValuePair(key, value.toString()));
		}
		
		if (datas!=null && extHeaders!=null && isJsonFormat(extHeaders) ){
//			ByteArrayInputStream bais = new ByteArrayInputStream(JsonKit.object2JsonEx(datas).getBytes(UTF_8));
//			httpPost.setEntity(new InputStreamEntity(bais));
			httpPost.setEntity(new StringEntity(JsonKit.object2JsonEx(datas),UTF_8));
		}else
			httpPost.setEntity(new UrlEncodedFormEntity(params, UTF_8));
		
		//设置headers
		if (extHeaders!=null){
			for (Entry<String, String> en:extHeaders.entrySet()){
				httpPost.setHeader(en.getKey(),en.getValue());
			}
		}
		
		//下面设置http调用参数
		useInvokeParam(httpPost);
		
		//调用
		return handleResponse(httpClient, httpPost);
	}
	
	private static boolean isJsonFormat(Map<String, String> extHeaders) {
		return ContentKit.isApplicationJson(extHeaders.get("Content-Type"));
	}
	
	private static void useInvokeParam(HttpRequestBase httpReqBase) {
		InvocationParam invokeParam = ClientInvocationManager.INSTANCE.getAndClearParam();
		if (invokeParam != null) {
			Builder configBuilder = RequestConfig.custom();
			if (invokeParam.getServiceTimeOut() != 0) {
				configBuilder.setSocketTimeout(invokeParam.getServiceTimeOut());
			}
			httpReqBase.setConfig(configBuilder.build());
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
		
		CloseableHttpClient httpClient = createHttpClient();
		HttpRequestBase httpGet = createRequest(method,url);
		
		
		//设置headers
		if (extHeaders!=null){
			for (Entry<String, String> en:extHeaders.entrySet()){
				httpGet.setHeader(en.getKey(),en.getValue());
			}
		}
		
		//下面设置http调用参数
		useInvokeParam(httpGet);
		
		return handleResponse(httpClient, httpGet);
	}

	private static HttpRequestBase createRequest(Method method, String url) {
		switch(method){
		case POST:
			return new HttpPost(url);
		case GET:
			return new HttpGet(url);
		case PUT:
			return new HttpPut(url);
		case DELETE:
			return new HttpGet(url);
		default:
			throw new RuntimeException("not support method:"+method);	
		}
	}
	
	public static String handleResponse(CloseableHttpClient client, HttpRequestBase request) throws IOException, HttpStatusException {
		String responseText = "";
		request.setHeader("Connection", "close");
		
		try{
			HttpResponse response = client.execute(request);
			if (response != null) {
				int code = response.getStatusLine().getStatusCode();
	//			if (code == 200){
				if (code>=200 && code <=206){ //根据http协议，2xx都是成功返回
					responseText = EntityUtils.toString(response.getEntity());
					EntityUtils.consume(response.getEntity());
					//重新设置状态
					request.reset();
				}else{
					request.abort();//异常结束调用about
					throw new HttpStatusException("Status Error:"+code);
				}
			}
		}catch(Exception ex){
			//异常，则about
			if (!request.isAborted()){
				request.abort();
			}
			//继续抛出异常
			if (ex instanceof IOException)
				throw (IOException)ex;
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
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.POST, url, params);
		return (String) filterManager.filter(ctx);
	}
	
	public static String postWithHeader(String url, Map<String, Object> params,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.POST, url, params,headers);
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
		HashMap headMap = new HashMap();
		if (headers!=null){
			headMap.putAll(headers);
		}
		
		String contentTypeValue = (String) headMap.get("Content-Type");
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
	public static String postJson(String url, Map json) throws IOException, HttpStatusException {
		return postJsonWithHeader(url, json, null);
	}
	
	
	public static String putWithHeader(String url, Map<String, Object> params,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.PUT, url, params,headers);
		return (String) filterManager.filter(ctx);
	}
	
	public static String get(String url) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.GET, url, null);
		return (String) filterManager.filter(ctx);
	}
	
	public static String getWithHeader(String url,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.GET, url, null,headers);
		return (String) filterManager.filter(ctx);
	}
	public static String deleteWithHeader(String url,Map<String,String> headers) throws IOException, HttpStatusException {
		HttpClientFilterContext ctx = new HttpClientFilterContext(HttpClientFilterContext.Method.DELETE, url, null,headers);
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
			if (method==HttpClientFilterContext.Method.POST || method==HttpClientFilterContext.Method.PUT){
				return HttpKit._handleWithEntity(method,ctx.getUrl(), ctx.getParams(),ctx.getHeaders());
			}else if (method==HttpClientFilterContext.Method.GET || method==HttpClientFilterContext.Method.DELETE){
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

//	//HttpInvoke Param 
//	private static ClientInvocationManager manager = new ClientInvocationManager();
//	
//	public static void setInvokeParam(InvocationParam p){
//		manager.setParam(p);
//	}
//	public static void clearInvokeParam(){
//		manager.clearParam();
//	}
}
