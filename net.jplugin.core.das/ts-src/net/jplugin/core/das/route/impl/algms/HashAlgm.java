package net.jplugin.core.das.route.impl.algms;

import net.jplugin.core.das.route.api.RouterDataSource;

public class HashAlgm extends FixedNumberTableAlgm{

	@Override
	public int getTableIndex(RouterDataSource ds, String tableBaseName,ValueType vt, Object key,int splits) {
		long hashCode;
		
		if (vt==ValueType.LONG){
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
