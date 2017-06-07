package net.jplugin.common.kits;

import java.util.UUID;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午06:04:48
 **/

public class UUIDKit {

//	public static String getUUIDWithBottomLine(){
//		return StringKit.replaceStr(getUUID(), "-", "_");
//	}
	
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		String stemp = uuid.toString().toUpperCase();
		return StringKit.replaceStr(stemp, "-", "_");
	}
}
