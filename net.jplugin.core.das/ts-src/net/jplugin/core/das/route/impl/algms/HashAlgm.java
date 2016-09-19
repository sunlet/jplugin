package net.jplugin.core.das.route.impl.algms;

import java.sql.Date;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;

public class HashAlgm  implements ITsAlgorithm{

	@Override
	public Result getResult(RouterDataSource compondDataSource, String tableBaseName, ValueType vt, Object key) {
		long hashCode;
		if (vt==ValueType.LONG){
			hashCode =  (Long)key;
		}else if (key instanceof String){
			hashCode = key.toString().hashCode();
		}else{
			throw new RuntimeException("not support algm for key java type:"+key.getClass().getName()+" algm is: "+this.getClass().getName());
		}
		
		int splits = compondDataSource.getConfig().findTableConfig(tableBaseName).getSplits();
		if (splits==0){
			throw new TablesplitException("Splits value error ,must >0 ,for table:"+tableBaseName);
		}
		//可以假定splits为int范围内
		int mod = (int) (hashCode % splits);
		
		int dsIndex = mod / compondDataSource.getConfig().getDataSourceConfig().length;
		
		Result r = Result.create();
		r.setDataSource(compondDataSource.getConfig().getDataSourceConfig()[dsIndex].getDataSrouceCfgName());
		r.setTableName(tableBaseName+"_"+(mod+1));
		return r;		
	}

}
