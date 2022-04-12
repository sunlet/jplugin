package net.jplugin.core.kernel.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午05:44:04
 **/

public class CoreServicePriority {
	//-5000到-1 留给Jplugin extension
	public static final int OFFSET_FOR_EXTENSION=-5000;
	//-10000到-5001 留给core
	private static final int OFFSET = -10000;
	public static final int KERNEL = 0 + OFFSET;
	public static final int SERVICE = 10 + OFFSET;
	public static final int LOG = 20 + OFFSET;
	public static final int CTX = 30 + OFFSET;
	public static final int DAS = 40 + OFFSET;
	public static final int DAS_DDS = 42 + OFFSET;
	public static final int DAS_TS = 45 + OFFSET;
	public static final int DAS_HIB = 50 + OFFSET;
	public static final int DAS_IBATIS = 60 + OFFSET;
	public static final int REMOTECLIENT = 70 + OFFSET;
	public static final int EVENT = 80 + OFFSET;
	public static final int TOKEN = 90 + OFFSET;
	public static final int WEBSERVICE = 100 + OFFSET;
	public static final int DICT = 110 + OFFSET;
	public static final int SCHEDULER = 120 + OFFSET;
	public static final int FILE_UPLOAD = 130 + OFFSET;
	public static final int JOB = 140 + OFFSET;
	public static final int LOCK = 150 + OFFSET;
	public static final int CONFIG = 5+OFFSET;
	public static final int MULTI_TANANT = 105 + OFFSET;
	public static final int STATIC_WEB = 5+OFFSET;
	public static final int GTRACE = 102+OFFSET;//修改到多租户之前，因为希望SchedulerExecutorService能够同时记录多个租户的数据
	
}
