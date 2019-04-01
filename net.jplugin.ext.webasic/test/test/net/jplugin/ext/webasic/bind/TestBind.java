package test.net.jplugin.ext.webasic.bind;

import java.io.IOException;
import java.util.HashMap;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.core.ctx.api.HttpServiceResult;

public class TestBind {
	public void test() throws IOException, HttpStatusException{
		String r = HttpKit.post("http://localhost:8080/demo/bindcontroller/test.do",new HashMap<>() );
		 r = HttpKit.post("http://localhost:8080/demo/bindexcontroller/test.do",new HashMap<>() );

		 HashMap para = new HashMap<>();
		 para.put("arg0", "a");
		 para.put("arg1", "b");
		 r = HttpKit.post("http://localhost:8080/demo/bindserviceexport/test.do", para );
		 HttpServiceResult result =  HttpServiceResult.create(r);
		 AssertKit.assertEqual(result.getResult(), "ab");

	}
}
