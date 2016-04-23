package net.jplugin.core.das.api;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.Plugin;
import net.jplugin.core.das.api.impl.ConfigedDataSource;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.das.api.impl.TxManagedDataSource;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.service.api.ServiceFactory;

public class DataSourceFactory {
	public static final String DATABASE_DSKEY="database";
	private static Map<String ,DataSource> map = new Hashtable<String, DataSource>();
	
	private static boolean inited=false;
	public synchronized static void init(){
		if (inited) return;
		else inited = true;

		Map<String, DataSourceDefinition> dss=new HashMap();
		dss.putAll(PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_DATASOURCE,DataSourceDefinition.class));

		//如果包含database的配置组，而且没有创建名字为database的DataSource，则自动创建一个。为了兼容
		//<<<<<
		if (ConfigFactory.getGroups().contains(DATABASE_DSKEY) && !dss.containsKey(DATABASE_DSKEY)){
			DataSourceDefinition dsd = new DataSourceDefinition();
			dsd.setConfigGroupName(DATABASE_DSKEY);
			dsd.setManaged(true);
			dss.put(DATABASE_DSKEY, dsd);
		}
		//>>>>>>
		
		for (Entry<String, DataSourceDefinition> ds:dss.entrySet()){
			DataSource dataSource = ConfigedDataSource.getDataSource(ds.getValue().getConfigGroupName());
			if (ds.getValue().getManaged()){
				TxManagedDataSource managedDataSource = new TxManagedDataSource(ds.getKey(),dataSource);
				map.put(ds.getKey(), managedDataSource);	
				ServiceFactory.getService(TransactionManager.class).addTransactionHandler(managedDataSource);
			}else{
				map.put(ds.getKey(), dataSource);
			}
		}
		
		

	}
	
	public static Set<String> getDataSourceNames(){
		return map.keySet();
	}
	
	public static DataSource getDataSource(String dataSourceName){
		DataSource ds = map.get(dataSourceName);
		if (ds==null) throw new RuntimeException("Can't find datasource config for:"+dataSourceName);
		return ds;
	}
}
