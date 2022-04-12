package net.jplugin.core.das.dds.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;

import net.jplugin.core.das.dds.api.IConnectionSettable;

public class Util {

	public static void trySetConnection(Statement s,DummyConnection c) {
		if (s instanceof IConnectionSettable) {
			((IConnectionSettable)s).setConnection(c);
		}
		
	}

}
