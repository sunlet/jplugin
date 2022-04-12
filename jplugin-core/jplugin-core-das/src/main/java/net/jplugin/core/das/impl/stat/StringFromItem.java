package net.jplugin.core.das.impl.stat;

import java.util.List;

public class StringFromItem implements IFromItem{
	String name;
	private String alias;
	
	public StringFromItem(String nm,String ali){
		this.name = nm;
		this.alias = ali;
	}
	
	public void appendToClause(StringBuffer sb) {
		if (name!=null){
			sb.append(" ").append(name);
		}
		if (alias!=null){
			sb.append(" ").append(alias);
		}
	}
	public void addToBindList(List<Object> list) {
	}
}