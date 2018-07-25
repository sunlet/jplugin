//这个类本来准备用，后来没用。
//package net.jplugin.core.mtenant;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import net.jplugin.common.kits.StringKit;
//import net.jplugin.core.config.api.ConfigFactory;
//import net.jplugin.core.das.api.DataSourceFactory;
//import net.jplugin.core.kernel.api.RefAnnotationSupport;
//
//public class MTenantConfigHelper extends RefAnnotationSupport{
//	public static MTenantConfigHelper instance = new MTenantConfigHelper();
//	private boolean init=false;
//	private String[] dataSources;
//	
//	/**
//	 * 获取多租户数据源
//	 * @return
//	 */
//	public String[] getMultiTenantDataSources(){
//		init();
//		return this.dataSources;
//	}
//	
//	private void init(){
//		if (!init){
//			init = true;
//			String datasource = ConfigFactory.getStringConfig("mtenant.datasource", "ALL");
//			
//			Set<String>  temp;
//			if ("ALL".equals(datasource)){
//				temp = DataSourceFactory.getDataSourceNames();
//			}else{
//				temp = toSet(StringKit.splitStr(datasource, ","));
//			}
//			this.dataSources = filterForPhicalDataSources(temp);
//		}
//	}
//	
//	private String[] filterForPhicalDataSources(Set<String> names) {
//		List<String> retults= new ArrayList();
//		for (String name:names){
//			String driver = ConfigFactory.getStringConfig("driverClassName");
//			String url = ConfigFactory.getStringConfig("url");
//			if (StringKit.isNotNull(driver) && StringKit.isNotNull(url)){
//				retults.add(name);
//			}
//		}
//		return retults.toArray(new String[retults.size()]);
//	}
//
//	private Set<String> toSet(String[] names) {
//		HashSet set = new HashSet<>();
//		for (String name:names){
//			set.add(name);
//		}
//		return set;
//	}
//
//	
//}
