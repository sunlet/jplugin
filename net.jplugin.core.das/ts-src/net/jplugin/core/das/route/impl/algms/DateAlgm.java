package net.jplugin.core.das.route.impl.algms;

import java.sql.Date;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.algms.TimeBasedSpanUtil.LocalDateMaintain;

public class DateAlgm  implements ITsAlgorithm{

	private int trackDays = 14;

	protected void setTrackDays(int m){
		this.trackDays = m;
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
		
		long dayIndex = time/(24*3600*1000);
		DataSourceConfig[] dsConfig = compondDataSource.getConfig().getDataSourceConfig();
		int dsIndex = (int) (dayIndex % dsConfig.length);
		Result r = Result.create();
		r.setDataSource(dsConfig[dsIndex].getDataSrouceCfgName());
		r.setTableName(getTableName(tableBaseName,time));
		return r;		
	}

	private String getTableName(String tableBaseName, long time) {
		return tableBaseName+"_"+CalenderKit.getShortDateString(time);
	}

	@Override
	public DataSourceInfo[] getMultiResults(RouterDataSource dataSource, String tableName, ValueType valueType,RouterKeyFilter kva) {
		LocalDateMaintain timeMaintainer = (ld,units)->ld.minusDays(units);
		return TimeBasedSpanUtil.getResults(this,dataSource,tableName,valueType,kva,timeMaintainer,this.trackDays);
	}

}
