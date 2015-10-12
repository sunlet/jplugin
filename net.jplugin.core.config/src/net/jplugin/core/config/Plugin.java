package net.jplugin.core.config;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.impl.ConfigRepository;
import net.jplugin.core.config.impl.PropertyFilter;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;
/**
*
* @author: LiuHang
* @version 创建时间：2015-10-12 下午01:07:22
**/
public class Plugin extends AbstractPlugin{

	public Plugin(){
		//因为propertyFilter需要在load阶段使用，所以只能在构造函数中初始化
		String cfgdir = PluginEnvirement.getInstance().getConfigDir();
		ConfigRepository repo = new ConfigRepository();
		repo.init(cfgdir);
		ConfigFactory.setRepository(repo);
		Extension.propertyFilter = new PropertyFilter();
	}
	@Override
	public void init() {
		//load config

	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CONFIG;
	}
	
	
}
