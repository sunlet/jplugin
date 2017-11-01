package net.jplugin.core.mtenant.handler2;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor.Strategy;

public class TenantStoreStrategyManager {

	public static TenantStoreStrategyManager instance = new TenantStoreStrategyManager();
	private ITenantStoreStrategyProvidor providor;
	private TenantStoreStrategyManager(){}
	
	public  void init(){
		ITenantStoreStrategyProvidor[] list = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.mtenant.Plugin.EP_TENANT_STORESTG_PROVIDOR,ITenantStoreStrategyProvidor.class);
		if (list==null || list.length==0){
			this.providor = null;
		}else if (list.length>1){
			throw new RuntimeException("ITenantStoreStrategyProvidor can only config one!");
		}else{
			this.providor = list[0];
		}
	}
	public  boolean isProviderExist(){
		return this.providor!=null;
	}
	
	public Strategy getStragegy(String tid){
		return this.providor.getTenantStrategy(tid);
	}
	
}
