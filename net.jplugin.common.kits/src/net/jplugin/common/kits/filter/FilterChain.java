package net.jplugin.common.kits.filter;

public class FilterChain<T>{
	FilterChain<T> next;
	IFilter<T> filter;
	public void setNext(FilterChain<T> c){
		this.next = c;
	}
	
	public Object next(T ctx) {
		if (this.next==null){
			throw new RuntimeException("Can't the call the next on the final filter nodes.");
		}else{
			return this.next.filter.filter(this.next,ctx);
		}
	}
}
