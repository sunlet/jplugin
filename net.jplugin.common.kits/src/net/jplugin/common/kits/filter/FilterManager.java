package net.jplugin.common.kits.filter;

public class FilterManager<T> {
	// 初始化一个空的头节点
	FilterChain<T> chain = new FilterChain<T>();

	/**
	 * <PRE>
	 * 加到末尾
	 * 注意：先加入的先执行，后加入的后执行
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
	 * @param ctx
	 * @return
	 */
	public Object filter(T ctx) {
		return chain.next(ctx);
	}
}
