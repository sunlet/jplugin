package net.jplugin.core.das;

import net.jplugin.core.das.api.IConnectionWrapperService;
import net.jplugin.core.das.api.IDBSplitAlg;
import net.jplugin.core.das.api.impl.DataSourceDefinition;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.Extension;

public class ExtensionDasHelper {
	
	public static void addDataSourceExtension(AbstractPlugin plugin,String dataSourceName,String dasCfgName){
		plugin.addExtension(Extension.create(Plugin.EP_DATASOURCE, dataSourceName,DataSourceDefinition.class,new String[][]{{"configGroupName",dasCfgName},{"managed","true"}}));
	}

	public static void addUnManagedDataSourceExtension(AbstractPlugin plugin,String dataSourceName,String dasCfgName){
		plugin.addExtension(Extension.create(Plugin.EP_DATASOURCE, dataSourceName,DataSourceDefinition.class,new String[][]{{"configGroupName",dasCfgName},{"managed","false"}}));
	}

	public static void addDBSplitAlgExtension(AbstractPlugin plugin,String name,Class algClass){
		plugin.addExtension(Extension.create(Plugin.EP_DBSPLIT_ALG, name,algClass));
	}
	
	public static void addConnWrapperExtension(AbstractPlugin plugin,Class clz){
		plugin.addExtension(Extension.create(Plugin.EP_CONN_WRAPPER, clz));
	}
	
	public static void addSqlListenerExtension(AbstractPlugin plugin,Class clz){
		plugin.addExtension(Extension.create(Plugin.EP_SQL_LISTENER, clz));
	}

	public static void addSqlExecFilterExtension(AbstractPlugin plugin,Class clz){
		plugin.addExtension(Extension.create(Plugin.EP_SQL_EXEC_FILTER, clz));
	}
	
	public static void addSqlRefactorExtension(AbstractPlugin plugin,Class clz){
		plugin.addExtension(Extension.create(Plugin.EP_SQL_REFACTOR, clz));
	}
	
	public static void addDynamisDataSourceTypeExtension	(AbstractPlugin plugin,String typeName,Class dataSourceClass){
		plugin.addExtension(Extension.create(Plugin.EP_DYNAMIC_DATASOURCE_TYPE, typeName,ClassDefine.class,new String[][]{ {"clazz",dataSourceClass.getName()}}));
	}
	
//	public static void addDynamicDataSourceProviderExtension(AbstractPlugin plugin,String name,Class clz){
//		plugin.addExtension(Extension.create(Plugin.EP_DYNAMIC_DS_PROVIDER, name,clz));
//	}
}
