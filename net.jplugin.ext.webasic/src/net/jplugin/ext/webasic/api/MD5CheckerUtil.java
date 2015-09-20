package net.jplugin.ext.webasic.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.MD5Kit;

public class MD5CheckerUtil {

	private static final String WA_CK = "wa_ck";
	private static final String WA_MD5 = "wa_md5";
	private static final String WA_CH = "wa_ch";

	public static void write(HttpServletRequest req, HttpServletResponse res, String json) {
		boolean output = tryAddMD5Header(req,res,json);
		try {
			if (output)
				res.getWriter().print(json);
			else
				res.getWriter().print("");
		} catch (IOException e) {
			throw new RuntimeException("render json error"+json,e);
		}
	}

	private static boolean tryAddMD5Header(HttpServletRequest req,
			HttpServletResponse res, String json) {
		String wa_ck = (String) req.getAttribute(WA_CK);
		if ("true".equals(wa_ck)){
			String oldMd5 = (String) req.getAttribute(WA_MD5);
			String newMd5 = MD5Kit.MD5(json);
			if (newMd5.equals(oldMd5)){
				res.addHeader(WA_CH, "false");
				return false;
			}else{
				res.addHeader(WA_CH, "true");
				res.addHeader(WA_MD5, newMd5);
				return true;
			}
		}else{
			return true;
		}
	}
	
}
