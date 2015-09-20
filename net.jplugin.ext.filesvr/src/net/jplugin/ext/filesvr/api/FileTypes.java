package net.jplugin.ext.filesvr.api;

import java.util.HashSet;

import net.jplugin.common.kits.StringKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 下午03:42:20
 **/

public class FileTypes {
	public static final String FT_IMAGE="image";
	public static final String FT_OFFICE="office";
	public static final String FT_FILE="file";

	public static boolean validType(String reqFileType) {
		return reqFileType.equals(FT_IMAGE) || reqFileType.equals(FT_OFFICE) || reqFileType.equals(FT_FILE);
	}
	static HashSet<String> imageExts = new HashSet<String>();
	static HashSet<String> officeExts = new HashSet<String>();
	static {
		imageExts.add("jpg");
		imageExts.add("jpeg");
		imageExts.add("png");
		imageExts.add("gif");
		imageExts.add("bmp");
		imageExts.add("png");
		
		officeExts.add("doc");
		officeExts.add("docx");
		officeExts.add("ppt");
		officeExts.add("pptx");
		officeExts.add("xls");
		officeExts.add("xlsx");
		officeExts.add("txt");
	}
	
	
	public static String getFileType(String ext){
		if (StringKit.isNull(ext)){
			return FT_FILE;
		}
		
		if (imageExts.contains(ext)){
			return FT_IMAGE;
		}
		if (officeExts.contains(ext)){
			return FT_OFFICE;
		}
		return FT_FILE;
	}


	

}
