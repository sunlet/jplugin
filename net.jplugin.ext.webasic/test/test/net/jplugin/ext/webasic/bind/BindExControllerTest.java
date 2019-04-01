package test.net.jplugin.ext.webasic.bind;

import net.jplugin.ext.webasic.api.AbstractExController;
import net.jplugin.ext.webasic.api.BindController;

@BindController(path = "/bindexcontroller")
public class BindExControllerTest extends AbstractExController{

	public void  test(){
		System.out.println("in BindExControllerTest");
	}

}
