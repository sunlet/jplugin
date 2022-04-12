package test.net.jplugin.ext.webasic.bind;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.ext.webasic.api.BindController;

@BindController(path="/bindcontroller")
public class BindControllerTest {

	public void test(HttpServletRequest req,HttpServletResponse res){
		System.out.println("in BindControllerTest");
	}
}
