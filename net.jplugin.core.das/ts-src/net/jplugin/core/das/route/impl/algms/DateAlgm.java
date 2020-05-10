package net.jplugin.core.das.route.impl.algms;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.DataSourceConfig;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.TablesplitException;

public class DateAlgm  implements ITsAlgorithm{

	private int trackDays = 14;
	
	//修改后0点的数据源位置和旧版本不一样，8点以后的才一致，升级以后可以调整数据源顺序来解决，循环调整一位即可
	private static int  offset = TimeZone.getDefault().getRawOffset();

	protected void setTrackDays(int m){
		this.trackDays = m;
	}
	
	@Override
	public Result getResult(RouterDataSource compondDataSource, String tableBaseName, ValueType vt, Object key) {
		long time;
//		if (vt == ValueType.DATE){
//			java.sql.Date dt = (Date) key;
//			time = dt.getTime();
//		}else if (vt == ValueType.TIMESTAMP){
//			time = ((java.sql.Timestamp)key).getTime();
//		}else throw new TablesplitException("DateAlgm don't support type:"+vt);
		time = TimeConverterKit.convertToTimeLong(vt, key);
		
//		long dayIndex = time/(24*3600*1000);
		int dayIndex = computeDayIndex(time);
		DataSourceConfig[] dsConfig = compondDataSource.getConfig().getDataSourceConfig();
		int dsIndex = (int) (dayIndex % dsConfig.length);
		Result r = Result.create();
		r.setDataSource(dsConfig[dsIndex].getDataSrouceCfgName());
		r.setTableName(getTableName(tableBaseName,time));
		return r;		
	}

	/**
	 * 1970年以前目前得到负数，暂不支持
	 * @param time
	 * @return
	 */
	private int computeDayIndex(long time) {
		return (int)((time + offset)/(24*60*60*1000));
	}

	private String getTableName(String tableBaseName, long time) {
		return tableBaseName+"_"+CalenderKit.getShortDateString(time);
	}

	@Override
	public DataSourceInfo[] getMultiResults(RouterDataSource dataSource, String tableName, ValueType valueType,RouterKeyFilter kva) {
//		LocalDateMaintain timeMaintainer = (ld,units)->ld.minusDays(units);
		return TimeBasedSpanUtil.getResults(this,dataSource,tableName,valueType,kva,ChronoUnit.DAYS,this.trackDays);
	}


}
