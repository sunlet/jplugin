package net.jplugin.core.das.route.impl.algms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAutoCreation;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.api.TablesplitException;

public class HashAlgm  implements ITsAlgorithm,ITsAutoCreation{

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
		
		if (hashCode<0){
			hashCode = -hashCode;
		}
		
		int splits = compondDataSource.getConfig().findTableConfig(tableBaseName).getSplits();
		if (splits==0){
			throw new TablesplitException("Splits value error ,must >0 ,for table:"+tableBaseName);
		}
		//可以假定splits为int范围内
		int mod = (int) (hashCode % splits);
		
		int dsIndex = mod % compondDataSource.getConfig().getDataSourceConfig().length;
		
		Result r = Result.create();
		r.setDataSource(compondDataSource.getConfig().getDataSourceConfig()[dsIndex].getDataSrouceCfgName());
		r.setTableName(tableBaseName+"_"+(mod+1));
		return r;		
	}
	
	/**
	 * 
	 */
	@Override
	public DataSourceInfo[] getMultiResults(RouterDataSource compondDataSource, String tableBaseName,ValueType valueType ,RouterKeyFilter kva) {
		if (Operator.EQUAL.equals(kva.getOperator())){
			Result r = getResult(compondDataSource, tableBaseName, valueType, kva.getConstValue()[0]);
			DataSourceInfo[] ret = new DataSourceInfo[1];
			ret[0] =  DataSourceInfo.build(r.getDataSource(), new String[]{r.getTableName()});
			return ret;
		}
		if (Operator.IN.equals(kva.getOperator())){
			//获取去重的结果集合
			Map<String,Set<String>> resultsMap = new HashMap();
			for (int i=0;i<kva.getConstValue().length;i++){
				Result r = getResult(compondDataSource, tableBaseName, valueType, kva.getConstValue()[i]);
				Set<String> targetList = resultsMap.get(r.getDataSource());
				if (targetList==null){
					targetList = new HashSet();
					resultsMap.put(r.getDataSource(), targetList);
				}
				if (!targetList.contains(r.getTableName())){
					targetList.add(r.getTableName());
				}
			}
			//转换成结果的格式
			DataSourceInfo[] ret = new DataSourceInfo[resultsMap.size()];
			int i=0;
			for (Entry<String, Set<String>> en:resultsMap.entrySet()){
				String[] tbs = new String[en.getValue().size()];
				ret[i++] = DataSourceInfo.build(en.getKey(), en.getValue().toArray(tbs));
			}
			return ret;
		}else{
			return getAllTables(compondDataSource, tableBaseName);
		}
	}

	private DataSourceInfo[] getAllTables(RouterDataSource compondDataSource, String tableBaseName) {
		TableConfig tableCfg = compondDataSource.getConfig().findTableConfig(tableBaseName);
		int splits = tableCfg.getSplits();
		DataSourceConfig[] dscfg = compondDataSource.getConfig().getDataSourceConfig();
		
		int baseNumber = splits / dscfg.length;
		int mod = splits % dscfg.length;
		
		DataSourceInfo[] ret = new DataSourceInfo[dscfg.length];
		for (int i=0;i<ret.length;i++){
			DataSourceInfo o = new DataSourceInfo();
			o.setDsName(dscfg[i].getDataSrouceCfgName());
			if (i<mod){
				o.setDestTbs(makeDestTbs(tableBaseName,i*baseNumber,baseNumber+1));
			}else{
				o.setDestTbs(makeDestTbs(tableBaseName,i*baseNumber,baseNumber));
			}
			ret[i] = o;
		}
		return ret;
	}
	private String[] makeDestTbs(String tableBaseName, int from,int n) {
		String[] a = new String[n];
		for (int i=0;i<n;i++){
			a[i] =tableBaseName + "_"+(from+i+1);
		}
		return a;
	}

	@Override
	public boolean needCreate(TableConfig tbCfg,String dataSourceName, String tableName) {
		return true;
	}

}
