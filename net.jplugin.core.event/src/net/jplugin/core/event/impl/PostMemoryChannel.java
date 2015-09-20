package net.jplugin.core.event.impl;

import java.util.LinkedList;
import java.util.Vector;

import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.ctx.api.TransactionSync;
import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.Event;
import net.jplugin.core.event.api.EventConsumer;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 上午11:48:08
 **/

public class PostMemoryChannel extends Channel{
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.Channel#getChannelType()
	 */
	@Override
	public ChannelType getChannelType() {
		return ChannelType.POST_MEMORY;
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.event.api.Channel#sendEvent(net.luis.plugin.event.api.Event)
	 */
	@Override
	public void sendEvent(Event e) {
		e.setId(nextUnique());
		
		TransactionManager txservice = ServiceFactory.getService(TransactionManager.class);
		TheTransactionSync thesync = sync.get();
		if (!txservice.containTransactionSync(thesync)){
			txservice.addTransactionSync(thesync);
			thesync.clear();
		}
		thesync.add(e);
	}
	
	static long u = System.currentTimeMillis();
	/**
	 * @return
	 */
	private static long nextUnique() {
		return ++u;
	}

	ThreadLocal<TheTransactionSync> sync = new ThreadLocal<TheTransactionSync>(){
		@Override
		protected TheTransactionSync initialValue() {
			return new TheTransactionSync();
		}
	};
	
	
	/**
	 * @author Luis
	 *
	 */
	public class TheTransactionSync implements TransactionSync{

		LinkedList<Event>  list = new LinkedList<Event>();


		public void add(Event e) {
			list.add(e);
		}

		public void clear() {
			list.clear();
		}

		
		public void afterCompletion(boolean success, Throwable th) {
			if (success){
				for (Event e:list){
					PostMemoryThreadPoolExecutor.instance.execute(new EventRunnable(e));
				}
			}
		}

		public void beforeCompletion() {
		}
	}
	
	public class EventRunnable implements Runnable{
		private Event event;
		public EventRunnable(Event e) {
			this. event = e;
		}

		public void run() {
			Vector<EventConsumer> cs = PostMemoryChannel.this.consumers;
			for (EventConsumer ec:cs){
				consumeEvent(ec,event);
			}
		}

		/**
		 * @param ec
		 * @param event2
		 */
		private void consumeEvent(EventConsumer ec, Event e) {
			Logger log = getMemoryEventLogger();
			
			long begin = System.currentTimeMillis();
			log.info("start exec event:"+ e.getId());
			ec.consume(e);
			log.info("end exec event:"+ e.getId()+" dural = "+ (System.currentTimeMillis()  - begin));
		}
	}
	

	public static Logger logger = null;
	/**
	 * @return
	 */
	private static Logger getMemoryEventLogger() {
		if (logger == null){
			synchronized (EventRunnable.class) {
				if (logger == null){
					logger = ServiceFactory.getService(ILogService.class).getSpecicalLogger("mem-event.log");
				}
			}
		}
		return logger;
	}

}
