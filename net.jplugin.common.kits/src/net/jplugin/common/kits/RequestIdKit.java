package net.jplugin.common.kits;

import java.util.concurrent.atomic.AtomicLong;

import net.jplugin.common.kits.UUIDKit;

public class RequestIdKit {
	static String startuuID = UUIDKit.getUUID();
	static AtomicLong al=new AtomicLong();
	public static String newRequestId(){
		return startuuID+"-"+al.getAndIncrement();
	}
}
