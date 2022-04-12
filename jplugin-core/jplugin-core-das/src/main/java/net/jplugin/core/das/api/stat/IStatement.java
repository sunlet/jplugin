package net.jplugin.core.das.api.stat;

import java.util.List;


public interface IStatement {
	String getSqlClause();
	List<Object> getParams();
}
