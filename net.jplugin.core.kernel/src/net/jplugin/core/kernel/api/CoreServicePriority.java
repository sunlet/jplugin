package net.jplugin.core.kernel.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-2 下午05:44:04
 **/

public class CoreServicePriority {
	private static final int OFFSET = -500;
	public static final int KERNEL = 0 + OFFSET;
	public static final int SERVICE = 10 + OFFSET;
	public static final int LOG = 20 + OFFSET;
	public static final int CTX = 30 + OFFSET;
	public static final int DAS = 40 + OFFSET;
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
}
