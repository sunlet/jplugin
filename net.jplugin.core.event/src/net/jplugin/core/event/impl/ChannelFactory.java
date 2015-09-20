package net.jplugin.core.event.impl;

import net.jplugin.core.event.api.Channel;
import net.jplugin.core.event.api.Channel.ChannelType;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 上午11:46:45
 **/

public class ChannelFactory {

	/**
	 * @param channelType
	 * @return
	 */
	public static Channel createChannel(ChannelType channelType) {
		if (channelType.equals(ChannelType.POST_MEMORY)){
			return new PostMemoryChannel();
		}
		
		if (channelType.equals(ChannelType.SYNC)){
			return new SyncChannel();
		}

		if (channelType.equals(ChannelType.POST_PERSISTENCE)){
			return new PostMemoryChannel();
		}
		
		throw new RuntimeException("not support channel type:"+channelType);
	}

}
