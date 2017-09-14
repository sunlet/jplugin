package test.net.jplugin.core.log;

import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.log.api.LogFactory;
import net.jplugin.core.log.api.Logger;

public class Plugin extends AbstractPluginForTest{
	
	static Logger log1 = LogFactory.getLogger(AbstractPluginForTest.class.getName());
	static{
		log1.info("*** in log test 1, by log1");
	}
	@Override
	public void test() throws Throwable {
		
		Logger log2 = LogFactory.getLogger(this.getClass().getName());
		log1.error("*** in log test 1, by log2");
		log2.error("*** in log test 2, by log2");
//		log2.error("abc{}abc{}abcabc","hahaha",111,new Exception());
	}

	@Override
	public int getPrivority() {
	 return	CoreServicePriority.LOG+1;
	}

}
