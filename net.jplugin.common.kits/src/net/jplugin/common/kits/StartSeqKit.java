package net.jplugin.common.kits;

public class StartSeqKit {
	static long start = System.currentTimeMillis();
	public synchronized static long getSeq(){
		return ++start;
	}
	public static String getSeqString(){
		return getSeq()+"";
	}
}
