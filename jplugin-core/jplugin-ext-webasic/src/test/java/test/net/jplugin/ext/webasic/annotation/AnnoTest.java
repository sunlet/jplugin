package test.net.jplugin.ext.webasic.annotation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.common.kits.http.mock.HttpMock;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.ext.webasic.ExtensionWebHelper;

public class AnnoTest {
/**
 * 		ExtensionWebHelper.addWebControllerExtension(this, "/webanno/web",WebControllerTest.class);
		ExtensionWebHelper.addWebExControllerExtension(this, "/webanno/webex", WebExControllerTest.class);
		ExtensionWebHelper.addServiceExportExtension(this, "/webanno/service", ServiceExportTest.class);
 * @throws HttpStatusException 
 * @throws IOException 

 */
	public void test() throws IOException, HttpStatusException {
		
		HashMap<String, Object> para = new HashMap<String,Object>();
		String r = HttpKit.post("http://localhost:8080/demo/webanno/web/test.do",para );
		 AssertKit.assertEqual("1", r);
		 
		 r = HttpKit.post("http://localhost:8080/demo/webanno/webex/test.do",para );
		 AssertKit.assertEqual("1", r);
		 
		 //在调用一次，因为每次创建新的
		 r = HttpKit.post("http://localhost:8080/demo/webanno/webex/test.do",para );
		 AssertKit.assertEqual("1", r);
		
		 r = HttpKit.post("http://localhost:8080/demo/webanno/service/test.do",para );
		 Object result = getResult(r);
		 AssertKit.assertEqual("1", result);
	}

	private Object getResult(String r) {
		JsonResult jr = JsonKit.json2Object(r, JsonResult.class);
		Map<String,Object> content = (Map<String, Object>) jr.getContent();
		Object result = content.get("result");
		return result;
	}
}
