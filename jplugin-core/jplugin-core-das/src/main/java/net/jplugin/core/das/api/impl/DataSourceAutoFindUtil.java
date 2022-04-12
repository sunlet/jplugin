package net.jplugin.core.das.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;

public class DataSourceAutoFindUtil {

//	driverClassName=com.mysql.jdbc.Driver
//			#the database url
//			url=jdbc:mysql://localhost:3306/weapp?useUnicode=true&amp;characterEncoding=utf8
				
	public static List<String> getAllDataSourceNames() {
		Set<String> groups = ConfigFactory.getGroups();
		List<String> ret = new ArrayList<String>();
		for (String g:groups){
			String driverClassName = ConfigFactory.getStringConfig(g+".driverClassName");
			String url = ConfigFactory.getStringConfig(g+".url");
			String route_datasource_flag = ConfigFactory.getStringConfig(g+".route-datasource-flag");
			if ((StringKit.isNotNull(url) && StringKit.isNotNull(driverClassName) && url.trim().startsWith("jdbc:"))
				|| (StringKit.isNotNull(route_datasource_flag) && "true".equalsIgnoreCase(route_datasource_flag.trim()))
				)
			{
				ret.add(g);
				continue;
			}
		}
		return ret;
	}

}
