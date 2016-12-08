package test.net.jplugin.ext.webasic.dynamicmethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.ctx.api.JsonResult;

public class TestDynamicMethodClient {

	public void test() throws IOException, HttpStatusException{
		HashMap<String, Object> para = new HashMap<String,Object>();
		para.put("p1", "p1value");
		String r = HttpKit.post("http://localhost:8080/demo/dynamic-method/test.do",para );
		Object result = getResult(r);
		AssertKit.assertEqual("ooo", result);
	}
	
	private Object getResult(String r) {
		JsonResult jr = JsonKit.json2Object(r, JsonResult.class);
		Map<String,Object> content = (Map<String, Object>) jr.getContent();
		Object result = content.get("result");
		return result;
	}
}
