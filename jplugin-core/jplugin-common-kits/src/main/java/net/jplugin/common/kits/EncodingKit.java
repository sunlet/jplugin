package net.jplugin.common.kits;

import java.io.UnsupportedEncodingException;

public class EncodingKit {

	public static String encode(String url) {
		try {
			return java.net.URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
