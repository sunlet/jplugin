package net.jplugin.core.das.dds.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CallableList <T>{
	List<MyCallable<T>> al = new ArrayList<MyCallable<T>>();
	public void add(MyCallable<T> mc){
		al.add(mc);
	}
	public void executeWith(T t) throws SQLException {
		for (MyCallable<T> o:al){
			o.call(t);
		}
	}
}
