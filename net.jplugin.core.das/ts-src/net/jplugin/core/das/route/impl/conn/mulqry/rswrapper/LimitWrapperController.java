package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

import net.jplugin.core.das.route.impl.CombinedSqlContext;

public class LimitWrapperController implements WrapperController{

	@Override
	public boolean needWrap() {
//		Statement stmt = RouterConnectionCallContext.getStatement();
//		if (stmt instanceof Select){
//			SelectBody body = ((Select)stmt).getSelectBody();
//			if (body)
//		}
		return false;
	}

	@Override
	public ResultSet wrap(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleContextInitial(CombinedSqlContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
