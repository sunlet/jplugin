package net.jplugin.core.das.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-17 下午04:59:13
 **/

public class PageCond {
	private int pageSize;
	private int pageIndex;

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
	public int getFirstRow() {
		return (pageIndex-1)*pageSize;
	}

	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

}
