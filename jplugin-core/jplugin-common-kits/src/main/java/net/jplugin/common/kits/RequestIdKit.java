package net.jplugin.common.kits;

import java.util.concurrent.atomic.AtomicLong;

import net.jplugin.common.kits.UUIDKit;
import net.jplugin.common.kits.tuple.Tuple2;

public class RequestIdKit {
	static IdKit traceIdKit = new IdKit();
	static IdKit spanIdKit = new IdKit();
	
	static class IdKit{
		private static final long LIMIT = Long.MAX_VALUE / 10;
		String startuuID = UUIDKit.getShortUUID();
		AtomicLong al = new AtomicLong();
		
		private String newId() {
			long idx = al.getAndIncrement();
			// 达到上限，重新申请一个UUID
			if (Long.MAX_VALUE - idx < LIMIT)
				startuuID = UUIDKit.getShortUUID();
			return startuuID + "-" + idx;
		}
	}
	

	public static String newTraceId() {
		return System.currentTimeMillis()+"-"+traceIdKit.newId();
	}

	public static String newSpanId() {
		return spanIdKit.newId();
	}

	

	public static void main(String[] args) {
//		System.out.println(newTraceId());
//		System.out.println(newTraceId());
//		System.out.println(newTraceId());
		for (int i=0;i<100000;i++)
		System.out.println(newSpanId());
		
//		System.out.println(newSpanId());
//		System.out.println(newSpanId());
//		System.out.println(newSpanId());
//		System.out.println(newSpanId());
//		System.out.println(newSpanId());
	}

	public static Tuple2<String, String> parse(String s) {
		int pos = s.indexOf('#');
		if (pos < 0)
			return Tuple2.with(s, null);
		else
			return Tuple2.with(s.substring(0, pos), s.substring(pos + 1));
	}

	public static String makeReqId(String traceId, String spanId) {
		if (spanId == null)
			return traceId;
		else
			return traceId + '#' + spanId;
	}
}
