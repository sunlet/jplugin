package net.jplugin.core.config;

import net.jplugin.common.kits.http.ContentKit;
import net.jplugin.core.config.api.CloudEnvironment;
import net.jplugin.core.config.api.ConfigChangeManager;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.config.api.IConfigChangeHandler;
import net.jplugin.core.config.impl.AnnoForAttrHandler;
import net.jplugin.core.config.impl.ConfigChangeHandlerDef;
import net.jplugin.core.config.impl.ConfigRepository;
import net.jplugin.core.config.impl.ConfigureChangeManager;
import net.jplugin.core.config.impl.PropertyFilter;
import net.jplugin.core.config.impl.autofresh.AutoRefreshConfigChangeHandler;
import net.jplugin.core.config.impl.autofresh.RefConfigAutoRefresher;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionKernelHelper;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginEnvirement;
/**
*
* @author: LiuHang
* @version 创建时间：2015-10-12 下午01:07:22
**/
@PluginAnnotation(prepareSeq=-1)
public class Plugin extends AbstractPlugin{

	public static final String EP_CONFIG_CHANGE_HANDLER = "EP_CONFIG_CHANGE_HANDLER";

	//务必注意：
	//本插件是基本插件，加载插件的构造函数中都可以使用（除了变更通知器之外）
	//并且因为propertyFilter需要在load阶段使用
	public static void prepare(){
		String cfgdir = PluginEnvirement.getInstance().getConfigDir();
		ConfigRepository repo = new ConfigRepository();
		repo.init(cfgdir);
		ConfigFactory._setLocalConfigProvidor(repo);
		Extension.propertyFilter = new PropertyFilter();

		//初始化CloudEnvirement
		CloudEnvironment.INSTANCE.loadFromConfig();
		//从BaiscConfig初始化,两套初始化只能有一个生效


	}

	public Plugin(){
		//add point
		this.addExtensionPoint(ExtensionPoint.createList(EP_CONFIG_CHANGE_HANDLER, ConfigChangeHandlerDef.class));
		ExtensionKernelHelper.addAnnoAttrHandlerExtension(this,AnnoForAttrHandler.class );
		ExtensionConfigHelper.addConfigChangeHandlerExtension(this, "*", AutoRefreshConfigChangeHandler.class);
	}
	@Override
	public void onCreateServices() {
		//load config
//		ConfigChangeManager.instance.init();
		ConfigureChangeManager.instance.init();

		//初始化一下兼容设置
		//1.7.0 默认不再兼容旧的application/json检查。不能在代码当中直接读取流了。
		ContentKit.init(Boolean.parseBoolean(ConfigFactory.getStringConfig("platform.json-check-compatible","false")));
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.CONFIG;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
