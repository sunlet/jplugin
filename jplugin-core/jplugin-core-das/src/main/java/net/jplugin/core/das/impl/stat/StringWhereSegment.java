package net.jplugin.core.das.impl.stat;

import java.util.List;

public class StringWhereSegment  implements IWhereSegment{
	Object[] bindList;
	String sqlSegment;
	
	public StringWhereSegment(String condstr,Object[] para){
		this.sqlSegment = condstr;
		this.bindList = para;
	}
	
	public String getString() {
		return sqlSegment;
	}

	public void addToBindList(List<Object> list) {
		if (bindList==null){
			return;
		}
		for (Object o:bindList){
			list.add(o);
		}
	}
	
}