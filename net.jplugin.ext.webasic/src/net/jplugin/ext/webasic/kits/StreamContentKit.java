package net.jplugin.ext.webasic.kits;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class StreamContentKit {

	public static String getContent(HttpServletRequest req) {
		String json = ThreadLocalContextManager.getRequestInfo().getContent().getJsonContent();
		
		if (json == null && ContentKit.jsonCheckCompirable){
			//从流中读取
			try{
				json = readReqStream(req);
			}catch(Exception e){
				//忽略异常
			}
		}
		return json;
	}

	public static String readReqStream(HttpServletRequest req) {
		InputStream is = null;
		String json;
		try {
			req.getInputStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int len;
			is = req.getInputStream();
			if (is == null) {
				json = "";
			} else {
				while ((len = is.read(buffer)) > 0) {
					os.write(buffer, 0, len);
				}
				json = os.toString("utf-8");
			}
		} catch (IOException e) {
			throw new RuntimeException("get json error:" + e.getMessage(), e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Exception e) {
				}
		}
		return json;
	}
	
}
