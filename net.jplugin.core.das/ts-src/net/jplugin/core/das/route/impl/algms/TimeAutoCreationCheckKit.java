package net.jplugin.core.das.route.impl.algms;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;

public class TimeAutoCreationCheckKit {

	public static boolean check(TableConfig tbCfg, String timeString, int defaultBeforeDays, int defaultAfterDays) {
		//定义long类型，防止后面的换算后越界
		long before = tbCfg.getCreationBeforeDays() == -1 ? defaultBeforeDays : tbCfg.getCreationBeforeDays();
		long after = tbCfg.getCreationAfterDays() == -1 ? defaultAfterDays : tbCfg.getCreationAfterDays();

		//换算成毫秒
		before = before*24*3600*1000;
		after = after*24*3600*1000;
		
		long theTime = CalenderKit.getTimeFromString(timeString).getTime();
		long now = System.currentTimeMillis();

		return now - before <= theTime && now + after >= theTime;
	}

}
