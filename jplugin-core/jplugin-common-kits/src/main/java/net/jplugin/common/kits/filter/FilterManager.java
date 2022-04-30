package net.jplugin.common.kits.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterManager<T> {
	// 初始化一个空的头节点
	FilterChain<T> chain = new FilterChain<T>();

	public int chainSize(){
		int cnt = 0;
		//头结点不计算
		FilterChain<T> temp = chain;
		while(temp.next!=null){
			temp = temp.next;
			cnt++;
		}
		return cnt;
	}
	/**
	 * <PRE>
	 * 加到末尾 注意：先加入的先执行，后加入的后执行
	 * 
	 * @param c
	 */
	public void addFilter(IFilter<T> c) {
		FilterChain<T> newChain = new FilterChain<T>();
		newChain.filter = c;

		FilterChain<T> prev = chain;
		while (prev.next != null) {
			prev = prev.next;
		}
		prev.next = newChain;
	}

	/**
	 * 直接执行头结点的下一个，头结点的filter为空
	 * 
	 * @param ctx
	 * @return
	 */
	public Object filter(T ctx) {
		try {
			return chain.next(ctx);
		} catch (Throwable th) {
			if (th instanceof RuntimeException) {
				throw (RuntimeException) th;
			} else {
				throw new RuntimeException(th.getMessage(), th);
			}
		}
	}

	public IFilter[] _getFilterListForDebug(){
		List<IFilter> list = new ArrayList<>();
		FilterChain fc = chain.next;
		while(fc!=null){
			list.add(fc.filter);
			fc = fc.next;
		}
		return list.toArray(new IFilter[list.size()]);
	}

}
