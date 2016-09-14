package net.jplugin.core.das.route.impl.algms;

import java.math.BigDecimal;
import java.util.Hashtable;

import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public class HashAlgm implements ITsAlgorithm{

	@Override
	public Result getResult(RouterDataSource ds, String tableBaseName, Object key) {
		DataSourceConfig[] dscfg = ds.getConfig().getDataSourceConfig();
		
		TableConfig tbcfg = ds.getConfig().findTableConfig(tableBaseName);
		int splits = tbcfg.getSplits();
		if (!(key instanceof Integer || key instanceof Long || key instanceof String)){
			throw new RuntimeException("not support algm for key:"+key.getClass().getName()+" algm is: "+this.getClass().getName());
		}
		int hashCode = key.toString().hashCode();
		int mod = hashCode % splits;
		
		int[][] dsBottomTop  = getMetrix(ds,tableBaseName);
		
		//find the datasource
		int targetDsIndex=-1;
		for (int i=0;i<dsBottomTop.length;i++){
			int[] o = dsBottomTop[i];
			if (o[0]<=mod && o[1]>=mod) {
				targetDsIndex = i;
			}
		}
		
		Result r = Result.create();
		r.setDataSource(dscfg[targetDsIndex].getDataSrouceCfgName());
		r.setTableName(tableBaseName+"_"+(mod+1));
		return r;
	}

	Hashtable<String,int[][]> ht = new Hashtable<String, int[][]>();
	private int[][] getMetrix(RouterDataSource dataSource,String tableBaseName) {
		String key = dataSource.hashCode()+"_"+tableBaseName;
		int[][] result = ht.get(key);
		if (result==null) {
			synchronized (this) {
				result = ht.get(key);
				if (result==null){
					result = computeMetrix(dataSource,tableBaseName);
					ht.put(key, result);
				}
			}
		}
		return result;
		
	}
	private int[][] computeMetrix(RouterDataSource dataSource, String tableBaseName) {
		DataSourceConfig[] ds = dataSource.getConfig().getDataSourceConfig();
		TableConfig tbcfg = dataSource.getConfig().findTableConfig(tableBaseName);
		int splits = tbcfg.getSplits();
		
		//compute the matrix
		int[][] dsBottomTop = new int[ds.length][2];
		int pos = 0;
		for (int i=0;i<ds.length-1;i++){
			DataSourceConfig dsc = ds[i];
			int size = (int) Math.round(dsc.getWeight()*splits*1.0/100);
			dsBottomTop[i][0]=pos;
			dsBottomTop[i][1]=pos + size -1;
			pos +=size;
		}
		dsBottomTop[ds.length-1][0] = pos;
		dsBottomTop[ds.length-1][1] = splits - 1;
		return dsBottomTop;
	}


}
