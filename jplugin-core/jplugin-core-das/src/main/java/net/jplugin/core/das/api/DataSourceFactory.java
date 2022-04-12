package net.jplugin.core.das.api;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.Plugin;
import net.jplugin.core.das.api.impl.ConfigedDataSource;
import net.jplugin.core.das.api.impl.DataSourceAutoFindUtil;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.das.api.impl.DataSourceWrapper;
import net.jplugin.core.das.api.impl.DynamicDataSourceManager;
import net.jplugin.core.das.api.impl.TxManagedDataSource;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.ServiceFactory;

public class DataSourceFactory {
	public static final String DATABASE_DSKEY="database";
	public static final String IS_TX_MANAGED = "is-tx-managed";
	private static Map<String ,DataSource> map = new Hashtable<String, DataSource>();
	
	private static boolean inited=false;
	public synchronized static void init(){
		if (inited) return;
		else inited = true;

		Map<String, DataSourceDefinition> dss=new HashMap();
		dss.putAll(PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_DATASOURCE,DataSourceDefinition.class));

		//如果包含database的配置组，而且没有创建名字为database的DataSource，则自动创建一个。为了兼容
		//<<<<<
//		if (ConfigFactory.getGroups().contains(DATABASE_DSKEY) && !dss.containsKey(DATABASE_DSKEY)){
//			DataSourceDefinition dsd = new DataSourceDefinition();
//			dsd.setConfigGroupName(DATABASE_DSKEY);
//			dsd.setManaged(true);
//			dss.put(DATABASE_DSKEY, dsd);
//		}
		//>>>>>>
		//自动发现所有没有按照配置名称注册的数据源，并全部加入进去。
		List<String> allConfigedNames = DataSourceAutoFindUtil.getAllDataSourceNames();
		for (String configedName:allConfigedNames){
			if (dss.containsKey(configedName))
				continue;
			DataSourceDefinition dsd = new DataSourceDefinition();
			dsd.setConfigGroupName(configedName);
			dsd.setManaged(true);
			if ("false".equalsIgnoreCase(ConfigFactory.getStringConfig(configedName+"."+IS_TX_MANAGED))){
				dsd.setManaged(false);//为字符串false才到这里
			}else{
				dsd.setManaged(true);//默认是true
			}
			dss.put(configedName, dsd);
		}
		
		for (Entry<String, DataSourceDefinition> ds:dss.entrySet()){
			DataSource dataSource = ConfigedDataSource.getDataSource(ds.getValue().getConfigGroupName());
			if (ds.getValue().getManaged()){
				TxManagedDataSource managedDataSource = new TxManagedDataSource(ds.getKey(),dataSource);
				map.put(ds.getKey(), new DataSourceWrapper(ds.getKey(),managedDataSource));	
				ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);
			}else{
				map.put(ds.getKey(), new DataSourceWrapper(ds.getKey(),dataSource));
			}
		}
		//加入动态DataSource,动态数据源不会加DataSourceWrapper
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfig("platform.use-dynamic-datasource-old-version"))) {
			addDynamicDataSourceToMap();
		}
	}
	
	private static void addDynamicDataSourceToMap() {
		Map<String, DataSource> dmap = DynamicDataSourceManager.INSTANCE.getDataSoruceMap();
		for (Entry<String, DataSource> en:dmap.entrySet()){
			if (map.containsKey(en.getKey())){
				throw new RuntimeException("duplicate dataSource Name for dynamic:"+en.getKey());
			}
			map.put(en.getKey(), en.getValue());
		}
	}

	public static Set<String> getDataSourceNames(){
		return map.keySet();
	}

	public static DataSource getDataSource(String dataSourceName){
		DataSource ds = map.get(dataSourceName);
		
		if (ds==null) {//如果匹配不到，从配置名匹配 2016-9-12
			throw new RuntimeException("Can't find datasource config for:"+dataSourceName);
		}
		return ds;
	}
}
