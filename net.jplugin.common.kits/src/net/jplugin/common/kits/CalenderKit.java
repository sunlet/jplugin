package net.jplugin.common.kits;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderKit {

	private static final String date_pattern_short = "yyMMdd";

	private static final String month_pattern_short = "yyMM";

	public static String split_pattern = "yyyy-MM-dd HH:mm:ss";

	public static String date_pattern = "yyyyMMdd";

	public static String time_pattern = "yyyyMMddHHmmss";

	//=================以下几个方法用来实现和数据格式转换
	public static String getTimeString(long timeLong) {
		return getFormatedTimeString(timeLong, time_pattern);
	}
	public static Date getTimeFromString(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(time_pattern);
		try {
			return sdf.parse(timeString);
		} catch (ParseException e) {
			throw new RuntimeException("parse error:"+timeString);
		}
	}
	public static String getDateString(long timeLong) {
		return getFormatedTimeString(timeLong, date_pattern);
	}
	
	public static String getShortDateString(long timeLong) {
		return getFormatedTimeString(timeLong, date_pattern_short);
	}
	public static String getShortMonthString(long timeLong) {
		return getFormatedTimeString(timeLong, month_pattern_short);
	}

	public static Date getDateFromString(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(date_pattern);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException("parse error:"+dateString);
		}
	}
	//=====================以上实现数据转换=============================
	
	
	
	/**
	 * 格式化时间为指定格式<br>
	 * 
	 * @param timeLong
	 * @param pattern
	 * @return
	 */
	public static String getFormatedTimeString(long timeLong, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(pattern);
		return sdf.format(new Date(timeLong));
	}

	public static String getCurrentTimeString() {
		return getTimeString(System.currentTimeMillis());
	}

	public static String getCurrentDateString() {
		Date dt = new Date();
		return getFormatedTimeString(dt.getTime(), date_pattern);
	}


	/**
	 * 格式化时间为指定格式<br>
	 * <em>Pattern: yyyy-MM-dd HH:mm:ss </em>
	 * 
	 * @param timeLong
	 * @return
	 */
	public static String getSpitTimeString(long timeLong) {
		return getFormatedTimeString(timeLong, split_pattern);
	}

	public static long getCurrentTime() {
		return (new Date()).getTime();
	}
	public static long getTime(String timeString) throws RuntimeException {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMddHHmmss");
		Date dt;
		try {
			dt = sdf.parse(timeString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return dt.getTime();
	}

	public static String getTimeDescription(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy年MM月dd日HH:mm:ss");
		return sdf.format(new Date(time));
	}

	// //////////////////////////////////////////////

	public static Date addMillisecond(Date dt, int millisecond) {
		return addSecond(dt, (long) millisecond);
	}

	public static Date addMillisecond(Date dt, long millisecond) {
		dt.setTime(dt.getTime() + millisecond);
		return dt;
	}

	// add second
	public static Date addSecond(Date dt, int second) {
		return addSecond(dt, (long) second);
	}

	public static Date addSecond(Date dt, float second) {
		return addSecond(dt, (double) second);
	}

	public static Date addSecond(Date dt, long second) {
		return addMillisecond(dt, 1000L * second);
	}

	public static Date addSecond(Date dt, double second) {
		Double millisecond = new Double(1000.0 * second);
		return addMillisecond(dt, millisecond.longValue());
	}

	// add minute
	public static Date addMinute(Date dt, int minute) {
		return addMinute(dt, (long) minute);
	}

	public static Date addMinute(Date dt, float minute) {
		return addMinute(dt, (double) minute);
	}

	public static Date addMinute(Date dt, long minute) {
		return addMillisecond(dt, 1000L * 60L * minute);
	}

	public static Date addMinute(Date dt, double minute) {
		Double millisecond = new Double(1000.0 * 60.0 * minute);
		return addMillisecond(dt, millisecond.longValue());
	}

	// add hour
	public static Date addHour(Date dt, int hour) {
		return addHour(dt, (long) hour);
	}

	public static Date addHour(Date dt, float hour) {
		return addHour(dt, (double) hour);
	}

	public static Date addHour(Date dt, long hour) {
		return addMillisecond(dt, 1000L * 60L * 60L * hour);
	}

	public static Date addHour(Date dt, double hour) {
		Double millisecond = new Double(1000.0 * 60.0 * 60.0 * hour);
		return addMillisecond(dt, millisecond.longValue());
	}

	// add day
	public static Date addDay(Date dt, int day) {
		return addDay(dt, (long) day);
	}

	public static Date addDay(Date dt, float day) {
		return addDay(dt, (double) day);
	}

	public static Date addDay(Date dt, long day) {
		return addMillisecond(dt, 1000L * 60L * 60L * 24L * day);
	}

	public static Date addDay(Date dt, double day) {
		Double millisecond = new Double(1000.0 * 60.0 * 60.0 * 24.0 * day);
		return addMillisecond(dt, millisecond.longValue());
	}

	// add month
	public static Date addMonth(Date dt, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + month);
		return cal.getTime();
	}

	public static Date addMonth(Date dt, float month) {
		return addMonth(dt, (double) month);
	}

	public static Date addMonth(Date dt, long month) {
		return addMonth(dt, (new Long(month)).intValue());
	}

	public static Date addMonth(Date dt, double month) {
		double floorMonth = Math.floor(month);
		double decimalMonth = month - floorMonth;
		dt = addMonth(dt, (new Double(floorMonth)).intValue());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		Date nextdt = cal.getTime();
		long monthMillisecond = nextdt.getTime() - dt.getTime();
		double millisecond = (double) monthMillisecond * decimalMonth;
		return addMillisecond(dt, (long) millisecond);
	}

	// add year
	public static Date addYear(Date dt, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + year);
		return cal.getTime();
	}

	public static Date addYear(Date dt, float year) {
		return addYear(dt, (double) year);
	}

	public static Date addYear(Date dt, long year) {
		return addYear(dt, (new Long(year)).intValue());
	}

	public static Date addYear(Date dt, double year) {
		double floorYear = Math.floor(year);
		double decimalYear = year - floorYear;
		dt = addYear(dt, (new Double(floorYear)).intValue());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		Date nextdt = cal.getTime();
		long yearMillisecond = nextdt.getTime() - dt.getTime();
		double millisecond = (double) yearMillisecond * decimalYear;
		return addSecond(dt, (long) millisecond);
	}

	public static long getPeriodLong(int days, int hour, int minutes) {
		return days * 24 * 60 * 60 * 1000L + hour * 60 * 60 * 1000L + minutes
				* 60 * 1000L;
	}

	public static long getDays(long times) {
		return times / (24 * 60 * 60 * 1000L);
	}

	public static long getHours(long times) {
		return times / (60 * 60 * 1000L);
	}

	public static long getMinutes(long times) {
		return times / (60 * 1000L);
	}

	public static String getDHM(long times) {
		long hours = times % (24 * 60 * 60 * 1000L);
		long minutes = hours % ((60 * 60 * 1000L));
		return String.valueOf(getDays(times)) + "天 "
				+ String.valueOf(getHours(hours)) + "小时: "
				+ String.valueOf(getMinutes(minutes)) + "分钟";
	}

	public static int getHour24OfTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMiniteOfTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		return c.get(Calendar.MINUTE);
	}

	public static int getSecondOfTime(long time) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		return c.get(Calendar.SECOND);
	}

	public static String getDuralTillNowString(long l) {
		long now = System.currentTimeMillis();
		long dural = now - l;

		if (dural < 10 * 60 * 1000) {
			return "刚刚";
		}

		if (dural < 3600 * 1000) {
			return (dural / (60 * 1000 * 10)) + "0 "+"分钟前";
		}

		if (dural < 5 * 3600 * 1000) {
			return dural / (3600 * 1000) + "小时前";
		}

		if (l / (3600 * 1000 * 24) == now / (3600 * 1000 * 24)) {
			return "今天";
		}

		Date theTime = new Date(l);
		Date nowTime = new Date(now);

		SimpleDateFormat sdf = null;
		String pattern;
		if (theTime.getYear() != nowTime.getYear()) {
			pattern = "yyyy-MM-dd";
		} else {
			pattern = "MM-dd";
		}

		sdf = new SimpleDateFormat(pattern);
		String ret =  sdf.format(l);
		if (ret.startsWith("0"))
			ret = ret.substring(1);
		return ret;
	}

	


	/**
	 * @param id
	 * @param ih
	 * @param im
	 * @return
	 */
	public static long getTimeLongFromDHM(int id, int ih, int im) {
		return ((long) id) * 24 * 60 * 60 * 1000 + ((long) ih) * 60 * 60 * 1000
				+ ((long) im) * 60 * 1000;
	}

	/**
	 * @param timeString
	 * @param pattern
	 * @return
	 */
	public static long parseTimeString(String timeString, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		ParsePosition pos = new ParsePosition(0);
		Date date = formatter.parse(timeString, pos);
		return date.getTime();
	}
}