package net.jplugin.ext.webasic.api;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static boolean  isMobileDevice(HttpServletRequest req){
		String requestHeader = req.getHeader("user-agent");
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        if(requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
                return true;
            }
        }
        return false;
	}
}
