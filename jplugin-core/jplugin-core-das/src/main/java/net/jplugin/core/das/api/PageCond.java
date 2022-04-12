package net.jplugin.core.das.api;

import net.jplugin.common.kits.JsonKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 下午04:59:13
 **/


public class PageCond {
	private int pageSize;
	private int pageIndex;
	private boolean shdCount;
	private long count;

	public PageCond (){}
	public PageCond (int aPageSize,int aPageIndex){
		if (aPageSize<1){
			throw new RuntimeException("page size must over 1. val = "+aPageSize);
		}
		if (aPageIndex<1){
			throw new RuntimeException("page index must over 1. val="+aPageIndex);
		}
		this.pageSize = aPageSize;
		this.pageIndex = aPageIndex;
	}
	/**
	 * @return
	 */
	public int _getFirstRow() {
		return (pageIndex-1)*pageSize;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	public boolean isShdCount() {
		return shdCount;
	}
	public void setShdCount(boolean shdCount) {
		this.shdCount = shdCount;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public static void main(String[] args) {
		PageCond pc = new PageCond(10, 3);
		System.out.println("pc is :"+JsonKit.object2Json(pc));
		
		pc = JsonKit.json2Object("{\"pageSize\":10,\"getCount\":false,\"count\":0}", PageCond.class);
		System.out.println("pc is :"+JsonKit.object2Json(pc));
		
	}
}
