package net.jplugin.core.das.api;

public class ExtCond {
	PageCond pageCond;
	String orderBy;
	public static ExtCond create(PageCond pc){
		return create(pc,null);
	}
	public static ExtCond create(String orderby){
		return create(null,orderby);
	}
	public static ExtCond create(PageCond pc,String orderby){
		ExtCond ec = new ExtCond();
		ec.pageCond = pc;
		ec.orderBy = orderby;
		return ec;
	}
	
	public PageCond getPageCond() {
		return pageCond;
	}
	public void setPageCond(PageCond pageCond) {
		this.pageCond = pageCond;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
