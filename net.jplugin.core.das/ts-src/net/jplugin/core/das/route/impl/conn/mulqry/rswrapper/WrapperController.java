package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

public interface WrapperController {
	
	boolean needWrap();
	
	ResultSet wrap(ResultSet rs);

}