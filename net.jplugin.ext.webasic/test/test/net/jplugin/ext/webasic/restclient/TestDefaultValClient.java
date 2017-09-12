package test.net.jplugin.ext.webasic.restclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.ctx.api.HttpServiceResult;
import net.jplugin.core.ctx.api.JsonResult;

public class TestDefaultValClient {

	public void test() throws IOException, HttpStatusException{
		final HashMap<String,Object> para = new HashMap();
		//正常调用
		para.put("arg0","a");
//		para.put("b", "b");
		String r = HttpKit.post("http://localhost:8080/demo/testrestclient/addStringNoRequired.do",para );
		HttpServiceResult hsr = HttpServiceResult.create(r);
		
//		
//		Object result = getResult(r);
		AssertKit.assertEqual("anull", hsr.getResult());
	}
//
//	private Object getResult(String r) {
//		JsonResult jr = JsonKit.json2Object(r, JsonResult.class);
//		Map<String,Object> content = (Map<String, Object>) jr.getContent();
//		Object result = content.get("result");
//		return result;
//	}
}
