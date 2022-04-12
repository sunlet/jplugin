package net.jplugin.core.mtenant;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.RefAnnotationSupport;

public class MTenantConfigHelper extends RefAnnotationSupport{
	
	public static String getDataSourceSchemaPrefix(String dsname){
		return ConfigFactory.getStringConfigWithTrim("mtenant.schema-prefix."+dsname);
	}
	
}
