package net.jplugin.core.das.route.impl.algms;

import java.sql.Date;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.TablesplitException;

public class MonthAlgm  implements ITsAlgorithm{

	@Override
	public Result getResult(RouterDataSource compondDataSource, String tableBaseName, ValueType vt, Object key) {
		long time;
		if (vt == ValueType.DATE){
			java.sql.Date dt = (Date) key;
			time = dt.getTime();
		}else if (vt == ValueType.TIMESTAMP){
			time = ((java.sql.Timestamp)key).getTime();
		}else throw new TablesplitException("DateAlgm don't support type:"+vt);
		
		java.util.Date javaDate = new java.util.Date(time);
		int monIndex = javaDate.getYear()*12+(javaDate.getMonth()-1);

		DataSourceConfig[] dsConfig = compondDataSource.getConfig().getDataSourceConfig();
		int dsIndex = (int) (monIndex % dsConfig.length);
		Result r = Result.create();
		r.setDataSource(dsConfig[dsIndex].getDataSrouceCfgName());
		r.setTableName(getTableName(tableBaseName,time));
		return r;		
	}

	private String getTableName(String tableBaseName, long time) {
		return  tableBaseName+"_"+CalenderKit.getShortMonthString(time);
	}
	@Override
	public DataSourceInfo[] getDataSourceInfos(RouterDataSource dataSource, String tableName) {
		throw new RuntimeException("not impl");
	}

}
