package net.jplugin.core.das.impl.stat;

import java.util.List;

public interface IFromItem {
	public void appendToClause(StringBuffer sb);
	public void addToBindList(List<Object> list);
}
