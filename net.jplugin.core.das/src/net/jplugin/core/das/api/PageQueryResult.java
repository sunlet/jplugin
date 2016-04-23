package net.jplugin.core.das.api;

import java.util.List;

public class PageQueryResult <T>{
	PageCond pageCond;
	List<T> list;
	
	public PageQueryResult() {
	}
	
	public PageQueryResult(PageCond pc,List<T> l){
		this.pageCond = pc;
		this.list = l;
	}
	
	public PageCond getPageCond() {
		return pageCond;
	}
	public void setPageCond(PageCond pageCond) {
		this.pageCond = pageCond;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
}
