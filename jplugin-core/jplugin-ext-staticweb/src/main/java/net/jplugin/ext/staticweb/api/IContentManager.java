package net.jplugin.ext.staticweb.api;

import java.util.Map;

import net.jplugin.ext.staticweb.impl.ConentManagerImpl;

public interface IContentManager {
	public static IContentManager INSTANCE = new ConentManagerImpl();
	
	public boolean accept(String uri);
	
	public Response handleRequest(Request req);
	
	
	public static class Request{
		String uri;
		private Request(String aUri){
			this.uri = aUri;
		}
		public static Request create(String aUri){
			return new Request(aUri);
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		
	}
	
	public static class Response{
		Map<String,String> headers;
		byte[] contentBytes;
		public static Response create(Map<String,String> h,byte[] c){
			Response r = new Response();
			r.setContentBytes(c);
			r.setHeaders(h);
			return r;
		}
		public Map<String, String> getHeaders() {
			return headers;
		}
		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}
		public byte[] getContentBytes() {
			return contentBytes;
		}
		public void setContentBytes(byte[] contentBytes) {
			this.contentBytes = contentBytes;
		}
		
	}
}
