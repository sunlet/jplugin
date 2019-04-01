package net.jplugin.core.das.route.impl.algms;

import java.sql.Date;
import java.time.temporal.ChronoUnit;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.TablesplitException;

public class MonthAlgm  implements ITsAlgorithm{

	int trackMonths = 6;
	protected void setTrackMonths(int m){
		this.trackMonths = m;
	}
	
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
	public DataSourceInfo[] getMultiResults(RouterDataSource dataSource, String tableName,ValueType valueType,RouterKeyFilter kva) {
//		return TimeBasedSpanUtil.get(this, dataSource, tableName, this.trackMonths,(ld,units)->ld.minusMonths(units));
//		LocalDateMaintain timeMaintainer = (ld,units)->ld.minusMonths(units);
		return TimeBasedSpanUtil.getResults(this,dataSource,tableName,valueType,kva,ChronoUnit.MONTHS,this.trackMonths);

	}

}
