package net.jplugin.core.mtenant.dds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.Logger;
import net.jplugin.core.log.api.RefLogger;
import net.jplugin.core.mtenant.MtenantStatus;


public class MultiTenantDDSHelper extends RefAnnotationSupport{
	
//	MultiTenantDDSHelper INSTANCE = new MultiTenantDDSHelper();
	private Map<String, String> datasourceMap;
	private String dataSourceName;
	
	public MultiTenantDDSHelper(){
	}
	public void init(String ds, Map<String, String> config) {
		this.dataSourceName= ds;
		this.datasourceMap = new ConcurrentHashMap<String, String>();
	}

	public String computeTargetDataSource() {
		if (!MtenantStatus.enabled()){
			throw new RuntimeException("The application is not in  multi-tenant status. can't use StaticTenantsDynamicDataSourceProvider");
		}
		
		String tid = ThreadLocalContextManager.getCurrentContext().getRequesterInfo().getCurrentTenantId();
		if (StringKit.isNull(tid)) {
			throw new RuntimeException("Got null tenantid when find dynamic datasoure:" + this.dataSourceName);
		}
		String key = tid;
	
		if (datasourceMap.containsKey(key)) {
			String result = datasourceMap.get(key);
			return result;
		} else {
			String result = getFromRemote(tid);
			if (result == null)
				throw new RuntimeException("Got null tenantid from remote:" + this.dataSourceName);
			if (this.dataSourceName.equals(result)){
				throw new RuntimeException("Real ds and declared ds is same!");
			}
			datasourceMap.put(key, result);
			return result;
		}
	}
	
	private String getFromRemote(String tid) {
		String path = this.dataSourceName+"."+"physical-ds";
		String physicalDss = ConfigFactory.getStringConfigWithTrim(path);
		if (StringKit.isNull(physicalDss)){
			throw new RuntimeException("physical-ds must configured for dynamic datasource :"+this.dataSourceName);
		}
		String[] phyDsArr = StringKit.splitStr(physicalDss,",");
		
		String targetDataSourceName=null;
		for ( String ds:phyDsArr){
			//在所有物理数据源查找，检查只有一个配置
			String temp = ConfigFactory.getStringConfig(ds+"-mtenant-cfg."+tid);
			if (temp!=null){
				if (targetDataSourceName!=null){
					//两个物理数据源发现
					throw new RuntimeException("Multi tenantstore policy found , tenant "+tid+" dds="+this.dataSourceName);
				}else{
					targetDataSourceName = ds;
				}
			}
		}
		return targetDataSourceName;
	}
	
	@RefLogger
	Logger logger;
	//清除缓存
	public void clearCache(){
		logger.warn("配置发生变化，刷新多租户动态数据源映射缓存");
		this.datasourceMap.clear();
	}
}
