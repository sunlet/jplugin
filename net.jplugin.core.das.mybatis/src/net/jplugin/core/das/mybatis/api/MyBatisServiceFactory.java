package net.jplugin.core.das.mybatis.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import net.jplugin.core.das.api.impl.ConfigedDataSource;
import net.jplugin.core.das.api.impl.TxManagedDataSource;
import net.jplugin.core.das.mybatis.Plugin;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.das.mybatis.impl.MybaticsServiceImplNew;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class MyBatisServiceFactory {
	static Map<String,IMybatisService> map = new Hashtable();
	
	private static boolean inited=false;
	public synchronized static void init(){
		if (inited) return;
		else inited = true;
		
//		MybatsiInterceptorManager.instance.init();
//		MybaticsServiceImpl svc = (MybaticsServiceImpl) ServiceFactory.getService(IMybatisService.class);
//		svc.init(PluginEnvirement.getInstance().getExtensionObjects(EP_MYBATIS_MAPPER,String.class));
		
		ExtensionDefinition4Mapping[] mappingDefs = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_MYBATIS_MAPPER,ExtensionDefinition4Mapping.class);

		ExtensionDefinition4Incept[] inceptDefs = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_MYBATIS_INCEPT,ExtensionDefinition4Incept.class);

		//获取datasources map
		Map<String,List<String>> mapperMapping=new HashMap();
		for (ExtensionDefinition4Mapping m:mappingDefs){
			String ds = m.getDataSource();
			if (!mapperMapping.containsKey(ds)) mapperMapping.put(ds, new ArrayList());
			
			List<String> list = mapperMapping.get(ds);
			list.add(m.getInterfOrResource());
		}
		
		//获取datasources map
		Map<String,List<Class>> inceptMapping=new HashMap();
		for (ExtensionDefinition4Incept m:inceptDefs){
			String ds = m.getDataSource();
			if (!inceptMapping.containsKey(ds)) inceptMapping.put(ds, new ArrayList());
			
			List<Class> list = inceptMapping.get(ds);
			list.add(m.getClazz());
		}
		
		//产生services
		for (Entry<String, List<String>> en:mapperMapping.entrySet()){
			MybaticsServiceImplNew service = new MybaticsServiceImplNew();
			service.init(en.getKey(), en.getValue(),inceptMapping.get(en.getKey()));
			map.put(en.getKey(),service );
		}
	}
	
	public static IMybatisService getService(String dataSourceName){
		return map.get(dataSourceName);
	}


}
