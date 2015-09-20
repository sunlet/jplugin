package net.jplugin.common.kits;

import java.io.UnsupportedEncodingException;

public class HeadCharKit {  
	  
	 private static final int GB_SP_DIFF = 160;  
	 private static final int[] secPosvalueList = {  
	       1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787,  
	       3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086,  
	       4390, 4558, 4684, 4925, 5249, 5600};  
	 private static final char[] firstLetter = {  
	       'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',  
	       'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',  
	       't', 'w', 'x', 'y', 'z'};  
	  
	 private static char getFirstLetter(String oriStr) throws UnsupportedEncodingException {  
	     String str = oriStr.toLowerCase();  
	     StringBuffer buffer = new StringBuffer();  
	     char ch;  
	     char[] temp;  
	     for (int i = 0; i < str.length(); i++) {   
	       ch = str.charAt(i);  
	       temp = new char[] {  
	           ch};  
	       byte[] uniCode = new String(temp).getBytes("GBK");  
	       if (uniCode[0] < 128 && uniCode[0] > 0) {  
	         buffer.append(temp);  
	       }  
	       else {  
	         buffer.append(convert(uniCode));  
	       }  
	     }  
	     return buffer.toString().charAt(0);
	 }  
	  
	 private static char convert(byte[] bytes) {  
	  
	     char result = '-';  
	     int secPosvalue = 0;  
	     int i;  
	     for (i = 0; i < bytes.length; i++) {  
	       bytes[i] -= GB_SP_DIFF;  
	     }  
	     secPosvalue = bytes[0] * 100 + bytes[1];  
	     for (i = 0; i < 23; i++) {  
	       if (secPosvalue >= secPosvalueList[i] &&  
	           secPosvalue < secPosvalueList[i + 1]) {  
	         result = firstLetter[i];  
	         break;  
	       }  
	     }  
	     return result;  
	 }  
	 //==============以上是网上公共代码====
	 
	 public static char getSerNameFirstChar(String name){
		if (name==null) return '#';
		char fc;
		try {
			fc = getFirstLetter(name.substring(0, 1));
		} catch (UnsupportedEncodingException e) {
			return '#';
		}
		
		char upfc = Character.toUpperCase(fc);
		if (upfc>='A' && upfc<='Z') return upfc;
		else return '#';
	 }
	 
	 
	 public static void main(String[] args) {
		System.out.println(getSerNameFirstChar("你"));
		System.out.println(getSerNameFirstChar("好"));
		System.out.println(getSerNameFirstChar("曾"));
		System.out.println(getSerNameFirstChar("1"));
		System.out.println(getSerNameFirstChar("hetong"));
		System.out.println(getSerNameFirstChar("-"));
	}
}