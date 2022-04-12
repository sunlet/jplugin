package net.jplugin.common.kits;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpKit {

	public static boolean isOuterIpAddress(String ip){
		 if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			 return false;
		 }
		 String reg = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
	     Pattern p = Pattern.compile(reg);
	     Matcher matcher = p.matcher(ip);
	     boolean result = matcher.find();
	     return !result;
	}
	
	public static void main(String[] args) {
		System.out.println(isOuterIpAddress("192.168.1.1"));
		System.out.println(isOuterIpAddress("172.168.1.1"));
	}
}
