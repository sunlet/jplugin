package net.jplugin.core.das.impl.stat;

import java.util.List;

import net.jplugin.core.das.api.stat.SelectStatement;

public class SubQueryWhereSegment implements IWhereSegment{
	SelectStatement subQry;
	public SubQueryWhereSegment(SelectStatement ss) {
		this.subQry = ss;
	}

	public String getString() {
		return "("+subQry.getSqlClause()+")";
	}

	public void addToBindList(List<Object> list) {
		List<Object> subparams = subQry.getParams();
		if (subparams!=null){
			list.addAll(subQry.getParams());
		}
	}
}
