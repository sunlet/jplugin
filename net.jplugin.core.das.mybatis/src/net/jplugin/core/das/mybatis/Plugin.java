package net.jplugin.core.das.mybatis;

import org.apache.ibatis.plugin.Interceptor;

import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.das.mybatis.impl.MybaticsServiceImpl;
import net.jplugin.core.das.mybatis.impl.MybatsiInterceptorManager;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.ExtensionServiceHelper;
import net.jplugin.core.service.api.ServiceFactory;

public class Plugin extends AbstractPlugin{
	
	public static final String EP_MYBATIS_MAPPER = "EP_MYBATIS_MAPPER";
	public static final String EP_MYBATIS_INCEPT = "EP_MYBATIS_INCEPT";

	public Plugin(){
		if (noMybatis()){
			System.out.println("Mybatis env not found,skipped!");
			return;
		}
		this.addExtensionPoint(ExtensionPoint.create(EP_MYBATIS_MAPPER, String.class));
		this.addExtensionPoint(ExtensionPoint.create(EP_MYBATIS_INCEPT,Interceptor.class));
		ExtensionServiceHelper.addServiceExtension(this, IMybatisService.class.getName(), MybaticsServiceImpl.class);
	}
	
	private boolean noMybatis() {
		try {
			Class.forName("org.apache.ibatis.session.SqlSession");
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	@Override
	public void init() {
		if (noMybatis()){
			System.out.println("Mybatis env not found,not init!");
			return;
		}else{
			System.out.println("now to init mybatis");
		}
		
		MybatsiInterceptorManager.instance.init();
		MybaticsServiceImpl svc = (MybaticsServiceImpl) ServiceFactory.getService(IMybatisService.class);
		svc.init(PluginEnvirement.getInstance().getExtensionObjects(EP_MYBATIS_MAPPER,String.class));
	}

	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_IBATIS;
	}

}
