package net.jplugin.common.kits.http.mock;

import java.util.Map;

import net.jplugin.common.kits.JsonKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午11:51:32
 **/

public class HttpMock {
//	private static IServletService servletSvc;
	public HttpServletRequestMock request = new HttpServletRequestMock();
	public HttpServletResponseMock response = new HttpServletResponseMock(); 
//	public HttpSessionMock session = new HttpSessionMock(); 
	
	public static IHttpService servletSvc;
	
	public HttpMock(){
		request.setResponse(response);
//		request.setSession(session);
//		
//		if (request.getSession().getAttribute("_user")==null){
//			request.getSession().setAttribute("_user","dummy");
//		}
	}
//	public static void setServletService(IServletService s){
//		servletSvc = s;
//	}
	
	public static HttpMock create(){
		return new HttpMock();
	}
	public static HttpMock createFromUrl(String url) {
		HttpMock mock = new HttpMock();
		mock.request.initFromUrl(url);
		return mock;
	}
	public HttpMock para(String key,String value){
		this.request.setPara(key, value);
		return this;
	}
	public HttpMock servletPath(String path){
		this.request.setServletPath(path);
		return this;
	}
	
	public String invoke(){
		//Thread th = new Thread().
		DummyHttpThread thread = new DummyHttpThread();
		thread.start();
		while(!thread.finished){
			//servletSvc.dohttp(request,response);
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return this.response.getResult();
	}
	public Map invokeForJson(){
		return JsonKit.json2Map(invoke());
	}


	public static void initServletSvc(IHttpService s) {
		HttpMock.servletSvc = s;
	}

	public class DummyHttpThread extends Thread{
		boolean finished=false;
		@Override
		public void run() {
			servletSvc.dohttp(request,response);
			finished =true;
		}
	}

}
