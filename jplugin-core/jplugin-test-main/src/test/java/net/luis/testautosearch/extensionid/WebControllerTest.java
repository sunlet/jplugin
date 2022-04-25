package net.luis.testautosearch.extensionid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.kernel.api.SetExtensionId;
import net.jplugin.ext.webasic.api.BindController;

//@BindController(path="/WebControllerTest",id="WebControllerTest")

@SetExtensionId("WebControllerTest")
@BindController(path="/WebControllerTest")

public class WebControllerTest {

	public void aaa(HttpServletRequest req,HttpServletResponse res) {
		
	}
}
