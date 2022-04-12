package net.jplugin.core.das.api.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IDynamicDataSourceProvider;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class DynamicDataSourceManager {
	private static final String DRIVER_CLASS_FOR_DYN_DS = ".driver-class-for-dyn-ds";
	
	public static DynamicDataSourceManager INSTANCE = new DynamicDataSourceManager();
	private Map<String, IDynamicDataSourceProvider> nameMap;
	private Map<String, DataSource> dataSourceMap;
	
	public void init(){
//		Map<String, IDynamicDataSourceProvider> m = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.das.Plugin.EP_DYNAMIC_DS_PROVIDER,IDynamicDataSourceProvider.class);
		Map<String, IDynamicDataSourceProvider> m = loadDynamicDataSourceMap();
		this.nameMap = m;
		dataSourceMap=new HashMap<String,DataSource>();
		for (Entry<String, IDynamicDataSourceProvider> en:nameMap.entrySet()){
			dataSourceMap.put(en.getKey(), new DynamicDataSource(en.getValue(),en.getKey()));
		}
	}

	private Map<String, IDynamicDataSourceProvider> loadDynamicDataSourceMap() {
		Map<String,IDynamicDataSourceProvider> result = new HashMap();
		Set<String> groups = ConfigFactory.getGroups();
		for (String g:groups){
			String ddsDriverClassKey= g + DRIVER_CLASS_FOR_DYN_DS;
			String driverclass = ConfigFactory.getStringConfig(ddsDriverClassKey);
			if (StringKit.isNotNull(driverclass)){
				try{
					Class<?> clazz = Class.forName(driverclass);
					Object o = clazz.newInstance();
					if (! (o instanceof IDynamicDataSourceProvider)){
						throw new RuntimeException("The driver class for dynamic datasource must implements "+IDynamicDataSourceProvider.class.getName()+". Class="+driverclass);
					}
					result.put(g, (IDynamicDataSourceProvider) o);
				}catch(ClassNotFoundException e){
					throw new RuntimeException("The driver class not found, Class="+driverclass);
				} catch (InstantiationException e) {
					throw new RuntimeException("The driver class instantiat error, Class="+driverclass);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("The driver class illegal access, Class="+driverclass);
				}
			}
		}
		return result;
	}

	public Map<String,DataSource> getDataSoruceMap(){
		return this.dataSourceMap;
	}
	
	class DynamicDataSource implements DataSource{
		private IDynamicDataSourceProvider prod;
		private PrintWriter loggerWriter;
		private String declaredDataSource;
		public DynamicDataSource(IDynamicDataSourceProvider p,String declaredDs) {
			this.prod  = p;
			this.declaredDataSource = declaredDs;
		}
		@Override
		public PrintWriter getLogWriter() throws SQLException {
			return this.loggerWriter;
		}
		@Override
		public void setLogWriter(PrintWriter out) throws SQLException {
			this.loggerWriter = out;
		}
		@Override
		public void setLoginTimeout(int seconds) throws SQLException {
			throw new RuntimeException("not support");
		}
		@Override
		public int getLoginTimeout() throws SQLException {
			throw new RuntimeException("not support");
		}
		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			return null;
		}
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}
		@Override
		public Connection getConnection() throws SQLException {
			return DataSourceFactory.getDataSource(prod.computeDataSourceName(this.declaredDataSource)).getConnection();
		}
		@Override
		public Connection getConnection(String username, String password) throws SQLException {
			return DataSourceFactory.getDataSource(prod.computeDataSourceName(this.declaredDataSource)).getConnection(username,password);
		}
	}
}
