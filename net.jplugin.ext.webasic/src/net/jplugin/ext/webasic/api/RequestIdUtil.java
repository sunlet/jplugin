package net.jplugin.ext.webasic.api;

import java.util.concurrent.atomic.AtomicLong;

import net.jplugin.common.kits.UUIDKit;

public class RequestIdUtil {
	static String startuuID = UUIDKit.getUUID();
	static AtomicLong al=new AtomicLong();
	public static String newRequestId(){
		return startuuID+"-"+al.getAndIncrement();
	}
}
