package net.jplugin.core.das.route.impl.algms;

import java.util.Hashtable;

import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public class WightHashAlgm implements ITsAlgorithm{

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

	public String getTableName(RouterDataSource ds, String tableBaseName, int index) {
		return tableBaseName+"_"+(index+1);
	}

	
//	/**
//	 * first is 0
//	 * @param ds
//	 * @param tableBaseName
//	 * @param key
//	 * @param splits 
//	 * @return
//	 */
//	public abstract int getTableIndex(RouterDataSource ds, String tableBaseName, ValueType vt,Object key, int splits);
//	public abstract String getTableName(RouterDataSource ds, String tableBaseName,int index);
//	
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
		
		if (splits==0){
			throw new TablesplitException("Splits value error ,must >0 ,for table:"+tableBaseName);
		}
		
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
		
		checkWightValid(ds);
		
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

	private void checkWightValid(DataSourceConfig[] ds) {
		int snum=0;
		for (DataSourceConfig o:ds){
			snum+=o.getWeight();
		}
		if (snum!=100)
			throw new TablesplitException("for weightHash algm, Sum of weights for all datasources ,must be 100. tableName");
		
	}
	@Override
	public DataSourceInfo[] getDataSourceInfos(RouterDataSource dataSource, String tableName) {
		throw new RuntimeException("not impl");
	}
}
