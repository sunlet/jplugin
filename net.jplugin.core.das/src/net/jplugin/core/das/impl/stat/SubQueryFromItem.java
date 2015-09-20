package net.jplugin.core.das.impl.stat;

import java.util.List;

import net.jplugin.core.das.api.stat.SelectStatement;

public class SubQueryFromItem implements IFromItem {
	SelectStatement subQry;
	private String alias;

	public SubQueryFromItem(SelectStatement ss, String ali) {
		this.subQry = ss;
		this.alias = ali;
	}

	public void appendToClause(StringBuffer sb) {
		sb.append(" (").append(subQry.getSqlClause()).append(")");
		if (alias != null) {
			sb.append(" ").append(alias);
		}
	}

	public void addToBindList(List<Object> list) {
		List<Object> params = subQry.getParams();
		if (params != null) {
			list.addAll(params);
		}
	}
}