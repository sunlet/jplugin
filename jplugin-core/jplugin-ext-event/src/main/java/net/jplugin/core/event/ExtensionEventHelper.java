package net.jplugin.core.event;

import net.jplugin.core.event.api.Channel.ChannelType;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午06:03:04
 **/

public class ExtensionEventHelper {

	/**
	 * @param plugin
	 * @param entitychangeevent
	 */
	public static void addEventTypeExtension(AbstractPlugin plugin,
			String eventtype) {
		plugin.addExtension(Extension.createStringExtension(net.jplugin.core.event.Plugin.EP_EVENT_TYPES, eventtype));
	}

	/**
	 * @param plugin
	 * @param class1
	 * @param entitychangeevent
	 * @param sync
	 */
	public static void addConsumerExtension(AbstractPlugin plugin,
			Class consumerCls, String targetType,
			ChannelType chanelType) {
		
		plugin.addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_CONSUMER, "",consumerCls,new String[][]{{"targetType",targetType},{"channelType",chanelType.toString()}} ));
	}
}
