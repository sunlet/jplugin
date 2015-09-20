package net.jplugin.ext.filesvr.web;

import net.jplugin.common.kits.FileKit;
import net.jplugin.core.kernel.api.ConfigHelper;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.filesvr.Plugin;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午07:36:22
 **/

public class Configures {
	public static String uploadPath; // 上传文件的目录
	public static String tempPath ; // 临时文件目录
	public static int  maxPicSize ;
	public static int maxFileSize;
	public static int uploadBufferSize;
	public static String smallFilePath;
	
	static{
		init();
	}

	public static synchronized void init(){
		if (uploadPath == null){
			ConfigHelper config = PluginEnvirement.getInstance().getConfigHelper(Plugin.class.getName());;
			uploadPath = config.getString(Plugin.CFG_UPLOAD_PATH, PluginEnvirement.getInstance().getWorkDir()+"/upload");
			tempPath = config.getString(Plugin.CFG_TEMP_PATH, PluginEnvirement.getInstance().getWorkDir()+"/temp");
			FileKit.makeDirectory(uploadPath);
			FileKit.makeDirectory(tempPath);
			maxPicSize = config.getInt(Plugin.CFG_MAXPICSIZE, 5000000);
			maxFileSize = config.getInt(Plugin.CFG_MAXFILESIZE, 10000000);
			uploadBufferSize = config.getInt(Plugin.CFG_UPLOAD_BUF_SIZE, 4096);
			smallFilePath = config.getString(Plugin.CFG_SMALLPIC_PATH, PluginEnvirement.getInstance().getWebRootPath()+"/upload");
		}
	}
}
