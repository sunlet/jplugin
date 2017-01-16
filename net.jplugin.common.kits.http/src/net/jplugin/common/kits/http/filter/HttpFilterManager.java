package net.jplugin.common.kits.http.filter;

import java.io.IOException;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.http.HttpStatusException;

public class HttpFilterManager {
	//初始化一个空的头节点
	static HttpClientFilterChain chain=new HttpClientFilterChain();

	
	public static void addFilter(IHttpClientFilter c){
		HttpClientFilterChain newChain = new HttpClientFilterChain();
		newChain.filter = c;

		HttpClientFilterChain prev = chain;
			while(prev.next!=null){
				prev = prev.next;
			}
			prev.next = newChain;
		}

	public static String execute(HttpFilterContext ctx) throws IOException, HttpStatusException {
		return chain.next(ctx);
	}
}
