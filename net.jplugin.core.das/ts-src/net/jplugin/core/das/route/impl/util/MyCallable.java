package net.jplugin.core.das.route.impl.util;

import java.sql.SQLException;

public interface  MyCallable <T>{
	public void call(T t) throws SQLException;
}
