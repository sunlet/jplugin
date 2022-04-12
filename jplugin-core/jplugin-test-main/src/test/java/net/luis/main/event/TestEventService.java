package net.luis.main.event;

import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.Event;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-9 下午06:00:07
 **/

public class TestEventService implements ITestEventService{

	/* (non-Javadoc)
	 * @see net.luis.main.event.ITestEventService#test()
	 */
	public void test() {
		Channel c = ServiceFactory.getService(Channel.class);
		for (int i=0;i<2;i++){
			c.sendEvent(new TestEvent(i,"hello "));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
