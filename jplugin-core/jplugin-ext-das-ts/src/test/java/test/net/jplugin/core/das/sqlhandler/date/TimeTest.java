package test.net.jplugin.core.das.sqlhandler.date;

import java.time.Clock;
import java.util.Date;
import java.util.TimeZone;

import net.jplugin.common.kits.CalenderKit;

public class TimeTest {

	public static void main(String[] args) {
		
		TimeZone timezone = TimeZone.getDefault();
		System.out.println(timezone);
		System.out.println(timezone.getRawOffset());
		System.out.println(8*60*60*1000);
		
		
		System.out.println(Clock.systemDefaultZone().getZone());
		
		show("20191009235959");
		
		show("20191010000000");
		
		show("20191010020101");
		
		
		show("20191010030101");
		
		
		show("20191010040101");
		
		
		show("20191010070101");
		
		
		show("20191010075959");

		
		show("20191010080000");
		
		
		show("20191010090101");
		
		
		show("20191010100101");
		
		
		show("20191010150101");
		
		
		show("20191010150101");
		
		
		show("20191010160158");
		
		
		show("20191010235959");
		
		
		show("20191011000000");
		
		show("19700101000000");
		show("19690101000000");
		show("19691231000000");
		
		System.out.println(-365/5);
	}

	private static void show(String s){
		Date t1 = CalenderKit.getTimeFromString(s);
		System.out.println(s+" -> "+getIndex( t1)+"  old = "+getIndexOld(t1));
	}
	
	//修改后0点的数据源位置和旧版本不一样，8点以后的才一致，升级以后可以调整数据源顺序来解决，循环调整一位即可
	static int  offset = TimeZone.getDefault().getRawOffset();
	private static long getIndex(Date t1) {
		long time = t1.getTime();
		return (time + offset)/(24*60*60*1000);
	}
	private static long getIndexOld(Date t1) {
		long time = t1.getTime();
//		time = time + (8*60*60*1000);
		return time/(24*60*60*1000);
	}
}
