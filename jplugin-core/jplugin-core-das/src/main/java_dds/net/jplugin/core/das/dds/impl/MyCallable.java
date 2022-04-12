package net.jplugin.core.das.dds.impl;

import java.sql.SQLException;

public interface  MyCallable <T>{
	public void call(T t) throws SQLException;
}
