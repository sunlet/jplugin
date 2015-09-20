package net.jplugin.ext.filesvr.web;

import java.util.HashMap;

import net.jplugin.ext.filesvr.api.Size;

public class ImgScale {
	public final static String SMALL="S";
	public final static String MID_SML="MS";
	public final static String MIDDLE="M";
	public final static String MID_BIG="MB";
	public final static String BIG="B";
	public final static String ORIGINAL="O";
	public static String maintainImgName(String path, String scale) {
		//目前只分小图和其他图
//		if (scale.equals(SMALL)) return path+"_min";
//		else return path;
		
		if (ORIGINAL.equals(scale)) return path;
		else {
			if (scaleMap.get(scale)==null) 
				throw new RuntimeException("error scale:"+scale);
			return path+"_"+scale;
		}
	}
	
	static HashMap<String,Size> scaleMap = new HashMap();
	static{
		scaleMap.put(SMALL, new Size(80,80));
		scaleMap.put(MID_SML, new Size(300,300));
		scaleMap.put(MIDDLE, new Size(500,500));
		scaleMap.put(MID_BIG, new Size(800,800));
		scaleMap.put(BIG, new Size(1200,1200));
	}
	
	public static Size getTargetSize(String scale) {
		return scaleMap.get(scale);
	}
}
