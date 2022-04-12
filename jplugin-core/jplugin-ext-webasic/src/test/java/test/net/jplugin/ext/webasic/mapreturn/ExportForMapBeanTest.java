package test.net.jplugin.ext.webasic.mapreturn;

import java.io.IOException;

import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;

public class ExportForMapBeanTest {

	public void test() throws IOException, HttpStatusException {
		String result = HttpKit.post("http://localhost:8080/demo/mapreturn/getResult.do",null);
		
		System.out.println(result);
		
	}

}
