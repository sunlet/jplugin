package net.jplugin.core.das.route.impl.algms;

import net.jplugin.core.das.route.api.RouterDataSource;

public class HashAlgm extends AbstractAlgm{

	@Override
	public int getTableIndex(RouterDataSource ds, String tableBaseName, Object key,int splits) {
		int hashCode;
		
		if (key instanceof Integer || key instanceof Long){
			hashCode =  (int)key;
		}else if (key instanceof String){
			hashCode = key.toString().hashCode();
		}else{
			throw new RuntimeException("not support algm for key java type:"+key.getClass().getName()+" algm is: "+this.getClass().getName());
		}
		
		int mod = hashCode % splits;
		return mod;
	}

	@Override
	public String getTableName(RouterDataSource ds, String tableBaseName, int index) {
		return tableBaseName+"_"+(index+1);
	}
}
