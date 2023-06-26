package jplugincoretest.net.jplugin.ext.webasic.annotation;

import java.io.IOException;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.config.api.RefConfig;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;
import net.jplugin.ext.webasic.api.AbstractExController;

public class WebExControllerTest extends AbstractExController{
	public WebExControllerTest(){
//		PluginEnvirement.INSTANCE.resolveRefAnnotation(this);
	}
	@RefConfig(path="test1.c1")
	String cfg1;
	@RefConfig(path="test1.c2")
	Integer cfg2;
	
	@RefLogger
	private Logger logger;
	@RefLogger
	private static Logger logger2=LogFactory.getLogger(String.class);
	public void test() throws IOException {
		AssertKit.assertTrue(logger!=null);
		AssertKit.assertTrue(logger2!=null);
		getRes().getWriter().write("1");
		
		AssertKit.assertTrue(cfg1.equals("1"));
		AssertKit.assertTrue(cfg2 == 2);
	}
}
