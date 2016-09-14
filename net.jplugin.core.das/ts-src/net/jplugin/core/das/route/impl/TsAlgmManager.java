package net.jplugin.core.das.route.impl;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.das.route.Plugin;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class TsAlgmManager {
	private static Map<String,ITsAlgorithm> algmMap=new HashMap<String,ITsAlgorithm>();
	public static ITsAlgorithm.Result getResult(RouterDataSource compondDataSource,String tbBaseName,Object key){
		TableConfig tc = compondDataSource.getConfig().findTableConfig(tbBaseName);
		ITsAlgorithm algm = algmMap.get(tc.getSplitAlgm());
		if (algm==null)
			throw new TablesplitException("error algm:"+tc.getSplitAlgm()+" for table:"+tbBaseName);
		
		return algm.getResult(compondDataSource,tbBaseName,key);
	}
	
	public static void init(){
		Map<String, Object> m = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_TS_ALGM);
		algmMap.putAll((Map<? extends String, ? extends ITsAlgorithm>) m);
	}

	public static boolean exists(String algm) {
		return algmMap.containsKey(algm);
	}
}
