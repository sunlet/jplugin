package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.impl.stat.IWhereSegment;


class ItemPairBasedStatement extends WhereBasedStatement{
	protected List<ItemPair> itemPairs;

	/**
	 * item 主要用来支持 select中别名为空的情况
	 * @param name
	 */
	public void addItem(String name){
		this.addItemPair(name, null);
	}

	public void addItems(String[] names){
		for (String nm:names){
			this.addItemPair(nm, null);
		}
	}

	public void addItemPair(String name,String value){
		if (this.itemPairs == null){
			this.itemPairs = new ArrayList<ItemPairBasedStatement.ItemPair>();
		}
		this.itemPairs.add(new ItemPair(name,value));
	}

	protected static class ItemPair {
		public ItemPair(String n,String v){
			this.name = n;
			this.value = v;
		}
		String name;
		String value;
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
	}
}
