package net.jplugin.core.das.route.impl.algms;

import java.util.Hashtable;

import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public abstract class FixedNumberTableAlgm implements ITsAlgorithm{
	/**
	 * first is 0
	 * @param ds
	 * @param tableBaseName
	 * @param key
	 * @param splits 
	 * @return
	 */
	public abstract int getTableIndex(RouterDataSource ds, String tableBaseName, ValueType vt,Object key, int splits);
	public abstract String getTableName(RouterDataSource ds, String tableBaseName,int index);
	
	private Hashtable<String,int[][]> tableMetrixMapping = new Hashtable<String, int[][]>();
	
	protected int[][] getTableMetrix(RouterDataSource dataSource,String tableBaseName) {
		String key = dataSource.hashCode()+"_"+tableBaseName;
		int[][] result = tableMetrixMapping.get(key);
		if (result==null) {
			synchronized (this) {
				result = tableMetrixMapping.get(key);
				if (result==null){
					result = computeMetrix(dataSource,tableBaseName);
					tableMetrixMapping.put(key, result);
				}
			}
		}
		return result;
	}
	
	@Override
	public Result getResult(RouterDataSource ds, String tableBaseName,ValueType vt, Object key) {
		DataSourceConfig[] dscfg = ds.getConfig().getDataSourceConfig();
		
		TableConfig tbcfg = ds.getConfig().findTableConfig(tableBaseName);
		int splits = tbcfg.getSplits();
		
		int mod = getTableIndex(ds,tableBaseName,vt,key,splits);
		
		int[][] dsBottomTop  = getTableMetrix(ds,tableBaseName);
		
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

		r.setTableName(getTableName(ds, tableBaseName, mod));
		return r;
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
