package net.jplugin.ext.filesvr.api;

import net.jplugin.ext.filesvr.web.StorePathGenerator;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-18 下午05:56:32
 **/

public interface IStorePathGenerator {
	public static IStorePathGenerator instance = new StorePathGenerator();
	/**
	 * 产生文件名，不包含存储目录的名称，也不包含 "/"
	 * 比如：abc.txt   a/b/c/dd.txt 
	 *  
	 * @param clientFileName
	 * @return
	 */
	public String generateStorePath(String clientFileName);

}
