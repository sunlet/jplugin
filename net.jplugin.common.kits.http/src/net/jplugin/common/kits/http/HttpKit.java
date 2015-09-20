package net.jplugin.common.kits.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.jplugin.common.kits.http.mock.HttpMock;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
/**
 * Http操作工具类
 * @author liyy
 * @date 2014-05-20
 */
public class HttpKit{
	
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024; //8KB
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private static int maxConnections = 200; //http请求最大并发连接数
    private static int maxConnectionsPerRoute = 20; //http请求最大并发连接数
    private static int socketTimeout = 120; //超时时间，默认20秒
    private static int maxRetries = 5;//错误尝试次数，错误异常表请在RetryHandler添加
    private static int httpThreadCount = 3;//http线程池数量
    
    private static boolean unitTesting=false;
	public static boolean isUnitTesting(){
		return unitTesting;
	}
	public static void setUnitTesting(boolean b) {
		unitTesting = b;
	}
	private static HttpClient createHttpClient() {
		try {
			//HttpClients.createDefault();
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
			
			//ConnManagerParams.setTimeout(params, socketTimeout*1000);
	        //ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(maxConnectionsPerRoute));
	        //ConnManagerParams.setMaxTotalConnections(params, maxConnections);

	        HttpConnectionParams.setTcpNoDelay(params, true);
	        HttpConnectionParams.setSocketBufferSize(params, DEFAULT_SOCKET_BUFFER_SIZE);
	        
	        HttpConnectionParams.setConnectionTimeout(params, socketTimeout*1000);  
	        HttpConnectionParams.setSoTimeout(params, socketTimeout*1000);  

			HttpProtocolParams.setUseExpectContinue(params, true);
			/*
			 * if (requestTimeout > 0)
			 * HttpConnectionParams.setConnectionTimeout(params, requestTimeout
			 * * 1000); if (soTimeout > 0)
			 * HttpConnectionParams.setSoTimeout(params, soTimeout * 1000);
			 */

			SchemeRegistry registry = new SchemeRegistry();
			//此处不能直接使用新的Scheme的构造方法，否则会有https的验证问题
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(registry);
			cm.setMaxTotal(maxConnections);  
			cm.setDefaultMaxPerRoute(maxConnectionsPerRoute); 

			return new DefaultHttpClient(cm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
	
	public static String post(String url, Map<String,Object> datas) throws  IOException, HttpStatusException{
		if (isUnitTesting()){
			if (url.startsWith("http://localhost") || url.startsWith("https://localhost")){
				return executeDummy(url,datas);
			}
		}
		
		HttpClient httpClient = createHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		Set<Entry<String,Object>> temps = datas.entrySet();
		for(Entry<String,Object> temp : temps){
			String key = temp.getKey();
			Object value = temp.getValue();
			if(value==null)	value = "";
			if(key!=null)
				params.add(new BasicNameValuePair(key, value.toString()));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		return handleResponse(httpClient, httpPost);
	}

	private static String executeDummy(String url, Map<String, Object> datas) {
		if (url.indexOf('?')>=0) 
			throw new RuntimeException("not impl get mode");
		
		HttpMock mock = HttpMock.createFromUrl(url);
		if (datas!=null){
			mock.request.putAllParameter(datas);
		}
		return mock.invoke();
	}
	public static String get(String url) throws ClientProtocolException, IOException, HttpStatusException {
		if (isUnitTesting()){
			if (url.startsWith("http://localhost") || url.startsWith("https://localhost")){
				return executeDummy(url,null);
			}
		}
		
		HttpClient httpClient = createHttpClient();
		HttpGet httpGet = new HttpGet(url);
		return handleResponse(httpClient, httpGet);
	}

	public static String handleResponse(HttpClient client, HttpRequestBase request) throws ClientProtocolException, IOException, HttpStatusException {
		String responseText = "";
		request.setHeader("Connection", "close");
		HttpResponse response = client.execute(request);
		if (response != null) {
			int code = response.getStatusLine().getStatusCode();
			if (code == 200){
				responseText = EntityUtils.toString(response.getEntity());
				EntityUtils.consume(response.getEntity());
			}else
				throw new HttpStatusException("Status Error:"+code);
		}
		return responseText;
	}
	
	static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port,
					autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
	
//	public static void writeResponse(HttpServletResponse response, String content){
//		try {
//			response.setCharacterEncoding("utf-8");
//			response.setContentType("text/plain");
//			response.getWriter().write(content);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public static String encodeParam(String param){
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
