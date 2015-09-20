package net.jplugin.core.das.api;

import javax.sql.DataSource;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DataSourceHolder {
	private static DataSourceHolder instance;
	private static ApplicationContext applicationContext;

	public static DataSourceHolder getInstance() {
		if (instance != null)
			return instance;
		else {
			synchronized (DataSourceHolder.class) {
				if (instance != null){
					return instance;
				}else {
					instance = new DataSourceHolder();
					instance.initApplicationContext();
					return instance;
				}
			}
		}
	}
	public DataSource getDataSource(){
		return  (DataSource) applicationContext.getBean("dataSource");
	}

	/**
	 * 
	 */
	private void initApplicationContext() {
		if (applicationContext == null) {
			synchronized (this.getClass()) {
				if (applicationContext == null) {
					String xml = PluginEnvirement.getInstance().getConfigDir()
							+ "/spring-das.xml";
//					applicationContext = new FileSystemXmlApplicationContext(
//							"file:" + xml);
					applicationContext = new ClassPathXmlApplicationContext("net/jplugin/core/das/api/spring-das.xml");
				}
			}
		}
	}

}
