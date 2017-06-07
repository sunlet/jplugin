package net.jplugin.ext.webasic.impl.reqid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.common.kits.http.filter.HttpClientFilterChain;
import net.jplugin.common.kits.http.filter.HttpFilterContext;
import net.jplugin.common.kits.http.filter.IHttpClientFilter;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.webasic.impl.InitRequestInfoFilterNew;

public class HttpRequestIdChain implements IHttpClientFilter {

	public String filter(HttpClientFilterChain fc, HttpFilterContext ctx) throws IOException, HttpStatusException {
		Map<String, String> map = ctx.getHeaders();
		if (map==null) {
			map = new HashMap();
			ctx.setHeaders(map);
		}
		String reqId = ThreadLocalContextManager.getRequestInfo().getRequestId();
		if (StringKit.isNotNull(reqId)){
			map.put(InitRequestInfoFilterNew._GREQID, ThreadLocalContextManager.getRequestInfo().getRequestId());
		}
		
		return fc.next(ctx);
	}

}
