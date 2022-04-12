package net.luis.main.remote;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-14 上午09:01:35
 **/

public class ServerObject implements IServerObject{
	public String concact(String[] arr){
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<arr.length;i++){
			sb.append(arr[i]);
		}
		return sb.toString();
	}
	
	public void printString(String s){
		System.out.println(s);
	}
	
	public void print(){
		System.out.println("print");
	}
	public HashMap<String, String> testHashMap(HashMap<String, String> para){
		System.out.println("in test hashmap");
		HashMap<String, String> ret = new HashMap<String, String>();
		for (Entry<String, String> e:para.entrySet()){
			ret.put(e.getKey(), "val "+e.getValue());
		}
		return ret;
	}
	
	public void testex(){
		throw new RuntimeException("hahaha runtimeexception");
		
	}
}
