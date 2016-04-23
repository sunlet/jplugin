package net.jplugin.core.das.hib.impl;

import javax.sql.DataSource;

import net.jplugin.core.das.api.impl.ConfigedDataSource;

public class DataSourceHolder {
	private static DataSourceHolder instance;
//	private static ApplicationContext applicationContext;

	public static DataSourceHolder getInstance() {
		if (instance != null)
			return instance;
		else {
			synchronized (DataSourceHolder.class) {
				if (instance != null) {
					return instance;
				} else {
					instance = new DataSourceHolder();
//					instance.initApplicationContext();
					return instance;
				}
			}
		}
	}

	DataSource dataSource = null;

	public DataSource getDataSource() {
		if (dataSource == null) {
			synchronized (this) {
				if (dataSource == null)
					dataSource = ConfigedDataSource.getDataSource("database");
			}
		}
		return dataSource;
		// return (DataSource) applicationContext.getBean("dataSource");
	}

//	/**
//	 * 
//	 */
//	private void initApplicationContext() {
//		if (applicationContext == null) {
//			synchronized (this.getClass()) {
//				if (applicationContext == null) {
//					String xml = PluginEnvirement.getInstance().getConfigDir() + "/spring-das.xml";
//					// applicationContext = new FileSystemXmlApplicationContext(
//					// "file:" + xml);
//					applicationContext = new ClassPathXmlApplicationContext("net/jplugin/core/das/api/spring-das.xml");
//				}
//			}
//		}
//	}

}
