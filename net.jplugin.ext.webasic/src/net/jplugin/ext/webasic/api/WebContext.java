package net.jplugin.ext.webasic.api;

import javax.servlet.http.HttpServletRequest;

public class WebContext {
	private static String protocal;
	private static String port;
	private static String context;
	private static boolean init;
	private static String basePath;
	private static String host;

	public static String getPort() {
		return port;
	}

	public static String getContextPath() {
		return context;
	}

	public static String getProtocal() {
		return protocal;
	}
	
	public static String getBasePath(){
		return basePath;
	}
	public static String getHost() {
		return host;
	}

	public static void initFromRequest(HttpServletRequest req) {
		if (!init) {
			String url = req.getRequestURL().toString();
			int posMaoHao = url.indexOf(':');
			host = getHost(url);
			
			protocal = url.substring(0, posMaoHao);
			port = "" + req.getLocalPort();
			context = req.getContextPath();
			
			basePath = WebContext.getProtocal()+"://"+host+":"+WebContext.getPort()+WebContext.getContextPath();
			if (!localhostCall(host)){
				//如果是本机调用，继续初始化
				init = true;
			}
		}
	}

	private static boolean localhostCall(String h) {
		return h.startsWith("127.") || h.startsWith("localhost") || h.startsWith("LOCALHOST");
	}

	private static String getHost(String url) {
		int pos1 = url.indexOf("//")+2;
		String s = url.substring(pos1);
		
		int postail;
		int pos2 = s.indexOf('/');
		int pos3 = s.indexOf(':');
		if (pos2<0){
			postail = pos3;
		}else if (pos3<0){
			postail = pos2;
		}else {
			if (pos3<pos2){
				postail = pos3;
			}else {
				postail = pos2;
			}
		}
		return s.substring(0, postail);
	}

}
