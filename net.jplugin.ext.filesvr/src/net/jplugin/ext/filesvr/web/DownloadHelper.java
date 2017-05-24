package net.jplugin.ext.filesvr.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午07:14:05
 **/

public class DownloadHelper {

	// 支持在线打开
	public static void downLoad(String filePath,String downloadName, HttpServletResponse response,
			boolean isOnLine){
		File f = new File(filePath);
		FileInputStream inputStream = null;
		BufferedInputStream br = null;
		OutputStream out=null;
		try {
			inputStream = new FileInputStream(f);
			br = new BufferedInputStream(inputStream);
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset(); // 非常重要
			if (isOnLine) { // 在线打开方式
				URL u = new URL("file:///" + filePath);
				response.setContentType(u.openConnection().getContentType());
				response.setHeader("Content-Disposition", "inline; filename="
						+ downloadName);
				// 文件名应该编码成UTF-8
			} else { // 纯下载方式
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + java.net.URLEncoder.encode(downloadName, "UTF-8"));
			}
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			out.flush();
		}catch(Exception e){
			throw new RuntimeException("download file error:"+filePath,e);
		}finally {
			closeQuirtely(br);
			closeQuirtely(inputStream);
			closeQuirtely(out);
		}
	}


	private static void closeQuirtely(OutputStream s) {
		if (s!=null){
			try{
				s.close();
			}catch(Exception e){
				ServiceFactory.getService(ILogService.class).getLogger(DownloadHelper.class.getName()).error("关闭流异常",e);
			}
		}
	}


	/**
	 * @param br
	 */
	private static void closeQuirtely(InputStream s) {
		if (s!=null){
			try{
				s.close();
			}catch(Exception e){
				ServiceFactory.getService(ILogService.class).getLogger(DownloadHelper.class.getName()).error("关闭流异常",e);
			}
		}
	}
}
