package net.jplugin.ext.staticweb.impl;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.staticweb.api.IContentManager;

public class ConentManagerImpl implements IContentManager {

	private static final int MAX_POST_CHECK = 15;

	/**
	 * 为了兼容ESF的老规则，可以带.do，也可以不带的情况。
	 * 如果尾部没有点，或者有 .do，返回false。
	 * 否则返回true
	 */
	@Override
	public boolean accept(String uri) {
		String post = getPost(uri);
		if (post==null || ".do".equals(post)) 
			return false; //尾部没有点，目前判断15位
		else{
			return true;
		}
	}

	private String getContentTypeForUri(String uri) {
		String post = getPost(uri);
		return ContentTypeMaps.get(post);
	}

	/**
	 * 获取最后MAX_POST_CHECK 位，如果有.XXX的话，返回.XXX
	 * @param uri
	 * @return
	 */
	private String getPost(String uri) {
		int maxlen = MAX_POST_CHECK;
		int lastIndex = uri.lastIndexOf('.');
		if (lastIndex < 0)
			return null;
		if (uri.length() - lastIndex > maxlen)
			return null;
		String post = uri.substring(lastIndex);
		return post;
	}

	public String getContentTypeForPostPrefix(String post) {
		return ContentTypeMaps.get(post);
	}

	@Override
	public Response handleRequest(Request req) {
//		Response res = Response.create(h, c)
		Map<String,String> headers = new HashMap<>();
		String uri = req.getUri();
		String contentType = getContentTypeForUri(uri);
		if (contentType==null) contentType = "text/html";
		
		if ("text/html".equals(contentType)) 
			contentType = "text/html; charset=utf-8";
		headers.put("ContentType", contentType);
		byte[] content = loadContent(req);
		
		Response response = Response.create(headers, content);
		return response;
		
	}

	private byte[] loadContent(Request req) {
		String uri = req.getUri();
		
		if (!checkPath(uri)){
			return ("Illegal path,file="+uri).getBytes();
		}
		
		String path = PluginEnvirement.INSTANCE.getWebRootPath()+uri;
//		String path = "."+uri;
		if (FileKit.existsAndIsFile(path)){
			byte[] content = FileKit.file2Bytes(path);
			return content;
		}else{
			return ("File not found,file="+uri).getBytes();
		}
	}
	
	/**
	 * 因为是后加的，在不违反安全的情况下，尽量兼容
	 * @param uri
	 * @return
	 */
	private static boolean checkPath(String uri) {
		//空，通过
		if (StringKit.isNull(uri)) {
			return true;
		}
		//开始符不对
		if (! (uri.startsWith("/") || uri.startsWith("\\"))){
			return false;
		}
		String temp = StringKit.replaceStr(uri, "\\", "/");
		//如果只是一个/，也通过
		if (temp.equals("/")) {
			return true;
		}
		
		//判断路径
		String[] arr = StringKit.splitStr(temp, "/");
		int v = 0;
		for (String a:arr){
			if (StringKit.isNull(a))
				continue;
			a = a.trim();
			if (".".equals(a))
				continue;
			if ("..".equals(a))
				v--;
			else
				v++;
			
			if (v<0){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		AssertKit.assertEqual(checkPath(null) , true );
		AssertKit.assertEqual(checkPath("") , true );
		AssertKit.assertEqual(checkPath("/") , true );
		AssertKit.assertEqual(checkPath("\\") , true );
		AssertKit.assertEqual(checkPath("/a") , true );
		AssertKit.assertEqual(checkPath("/././a") , true );
		AssertKit.assertEqual(checkPath("/././.") , true );
		AssertKit.assertEqual(checkPath("a") , false);
		AssertKit.assertEqual(checkPath("/../a") , false);
		AssertKit.assertEqual(checkPath("/../.") , false);
		AssertKit.assertEqual(checkPath("/a/..") , true);
		AssertKit.assertEqual(checkPath("/a/../..") , false);
		AssertKit.assertEqual(checkPath("/a/b/../../.") , true);
		AssertKit.assertEqual(checkPath("/a/b/../../a/.././..") , false);
		AssertKit.assertEqual(checkPath("/a/b/../../a/.././..") , false);
		AssertKit.assertEqual(checkPath("/a/b/../ ../a/.././..") , false);
	}
	
	public static void main2(String[] args) {
		
		ConentManagerImpl o = new ConentManagerImpl();
		Response res = o.handleRequest(Request.create("/src/net/jplugin/ext/staticweb/Plugin.java"));
		
		System.out.println(new String(res.getContentBytes()));
		System.out.println("headers = "+res.getHeaders());
		res = o.handleRequest(Request.create("/src/net/jplugin/ext/staticweb/Plugin22.aa"));
		System.out.println(new String(res.getContentBytes()));
		System.out.println("headers = "+res.getHeaders());
	}

}
