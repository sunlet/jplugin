package net.jplugin.common.kits.http;

public class ContentKit {
	private static final String APPLICATION_JSON = "application/json";
	public static Boolean jsonCheckCompirable = true;
	
	public static void init(Boolean b){
		jsonCheckCompirable = b;
		System.out.println("*************platform.json-check-compatible="+jsonCheckCompirable);
	}
	
	public static boolean isApplicationJson(String theContentType) {
		if (jsonCheckCompirable)//兼容模式,判断等于
			return theContentType!=null && theContentType.equals(APPLICATION_JSON);
		else//正常模式，判断前缀
			return theContentType!=null && theContentType.startsWith(APPLICATION_JSON);			
	}

}
