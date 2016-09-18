package net.jplugin.core.das.route.impl.algms;

import net.jplugin.core.das.route.api.RouterDataSource;

public class HashAlgm extends AbstractAlgm{

	@Override
	public int getTableIndex(RouterDataSource ds, String tableBaseName, Object key,int splits) {
		long hashCode;
		
		if (key instanceof Integer){
			hashCode = Long.valueOf((Integer)key);
		}else if (key instanceof Long){
			hashCode =  (Long)key;
		}else if (key instanceof String){
			hashCode = key.toString().hashCode();
		}else{
			throw new RuntimeException("not support algm for key java type:"+key.getClass().getName()+" algm is: "+this.getClass().getName());
		}
		
		//可以假定splits为int范围内
		int mod = (int) (hashCode % splits);
		return mod;
	}

	@Override
	public String getTableName(RouterDataSource ds, String tableBaseName, int index) {
		return tableBaseName+"_"+(index+1);
	}
}
