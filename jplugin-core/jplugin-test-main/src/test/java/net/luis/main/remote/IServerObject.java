package net.luis.main.remote;

import java.util.HashMap;
import java.util.Map.Entry;

import net.jplugin.core.ctx.api.Rule;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-14 上午09:01:35
 **/

public interface IServerObject {
	@Rule
	public String concact(String[] arr);
	@Rule
	public void printString(String s);
	@Rule
	public void print();
	@Rule
	public HashMap<String, String> testHashMap(HashMap<String, String> para);
	@Rule
	public void testex();
}
