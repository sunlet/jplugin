package net.jplugin.core.das.impl.stat;

import java.util.List;

public interface IWhereSegment {
	public String getString();
	public void addToBindList(List<Object> list);
}
