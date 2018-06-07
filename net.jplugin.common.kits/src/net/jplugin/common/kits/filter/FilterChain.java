package net.jplugin.common.kits.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterChain<T>{
	FilterChain<T> next;
	IFilter<T> filter;
	public void setNext(FilterChain<T> c){
		this.next = c;
	}
	
	public List<IFilter<T>> getFollowingFilters() {
		List<IFilter<T>> list=new ArrayList<>();
		FilterChain<T> temp = next;
		while (temp!=null){
			if (temp.filter!=null){
				list.add(temp.filter);
			}else{
				throw new RuntimeException("filter can't be null!");
			}
			temp = temp.next;
		}
		return list;
	}
	
	public Object next(T ctx) throws Throwable {
		if (this.next==null){
//			throw new RuntimeException("Can't the call the next on the final filter nodes.");
			return null;
		}else{
			return this.next.filter.filter(this.next,ctx);
		}
	}
}
