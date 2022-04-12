package net.jplugin.core.mtenant.api;

import java.util.List;

import net.jplugin.core.kernel.api.PluginEnvirement;

public class TenantListProvidorManager {
	public static TenantListProvidorManager instance = new TenantListProvidorManager();
	private ITenantListProvidor providor;
	private TenantListProvidorManager(){}
	
	public  void init(){
		ITenantListProvidor[] list = PluginEnvirement.getInstance().getExtensionObjects(net.jplugin.core.mtenant.Plugin.EP_TENANTLIST_PROVIDOR,ITenantListProvidor.class);
		if (list==null || list.length==0){
			this.providor = null;
		}else if (list.length>1){
			throw new RuntimeException("ITenantListProvider can only config one!");
		}else{
			this.providor = list[0];
		}
	}
	public  boolean isProviderExist(){
		return this.providor!=null;
	}
	public  List<String> getList(){
		return this.providor.getTenantList();
	}
}
