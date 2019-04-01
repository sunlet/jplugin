package net.luis.main.event;

import net.jplugin.core.event.api.Event;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-15 上午10:11:07
 **/

public class TestEvent extends Event {
	public static final String TEST_EVENT_ALIAS = "test-event-alias";
	public static final String TEST_EVENT = "test-event";
		private String string;
		int key;

		/**
		 * @param string
		 */
		public TestEvent(int k,String string) {
			super(TestEvent.TEST_EVENT);
			this.string = string;
			this.key = k;
		}
		public String toString(){
			return "event:"+string +" key="+key;
		}
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		
		
		
		
}
