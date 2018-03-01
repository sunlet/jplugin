package net.jplugin.core.das.api.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.IDynamicDataSourceProvider;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class DynamicDataSourceProviderManager {
	public static DynamicDataSourceProviderManager INSTANCE = new DynamicDataSourceProviderManager();
	private Map<String, IDynamicDataSourceProvider> nameMap;
	private Map<String, DataSource> dataSourceMap;
	
	public void init(){
		Map<String, IDynamicDataSourceProvider> m = PluginEnvirement.getInstance().getExtensionMap(net.jplugin.core.das.Plugin.EP_DYNAMIC_DS_PROVIDER,IDynamicDataSourceProvider.class);
		this.nameMap = m;
		dataSourceMap=new HashMap<String,DataSource>();
		for (Entry<String, IDynamicDataSourceProvider> en:nameMap.entrySet()){
			dataSourceMap.put(en.getKey(), new DynamicDataSource(en.getValue()));
		}
	}

	public Map<String,DataSource> getDataSoruceMap(){
		return this.dataSourceMap;
	}
	
	class DynamicDataSource implements DataSource{
		private IDynamicDataSourceProvider prod;
		private PrintWriter loggerWriter;
		public DynamicDataSource(IDynamicDataSourceProvider p) {
			this.prod  = p;
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
			return DataSourceFactory.getDataSource(prod.computeDataSourceName()).getConnection();
		}
		@Override
		public Connection getConnection(String username, String password) throws SQLException {
			return DataSourceFactory.getDataSource(prod.computeDataSourceName()).getConnection(username,password);
		}
	}
}
