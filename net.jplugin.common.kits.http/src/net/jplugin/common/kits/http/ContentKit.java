package net.jplugin.common.kits.http;

public class ContentKit {
	private static final String APPLICATION_JSON = "application/json";
	
	public static boolean isApplicationJson(String theContentType) {
//		return theContentType!=null && theContentType.startsWith(APPLICATION_JSON);
		
		return theContentType!=null && theContentType.equals(APPLICATION_JSON);
	}

}
