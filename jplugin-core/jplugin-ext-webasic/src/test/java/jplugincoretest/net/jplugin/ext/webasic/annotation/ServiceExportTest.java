package jplugincoretest.net.jplugin.ext.webasic.annotation;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;

public class ServiceExportTest {
	@RefLogger
	private Logger logger;
	@RefLogger
	private static Logger logger2=LogFactory.getLogger(String.class);
	public String test() {
		AssertKit.assertTrue(logger!=null);
		AssertKit.assertTrue(logger2!=null);
		return "1";
	}
}
