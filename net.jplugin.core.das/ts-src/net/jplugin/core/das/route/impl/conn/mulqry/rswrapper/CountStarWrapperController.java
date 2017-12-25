package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

import net.jplugin.core.das.route.impl.RouterConnectionCallContext;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser.Meta;

public class CountStarWrapperController implements WrapperController{

	@Override
	public boolean needWrap() {
		CombinedSqlParser.Meta meta = (Meta) RouterConnectionCallContext.getMeta();
		return meta.getCountStar()==Meta.COUNG_STAR_YES;
	}

	@Override
	public ResultSet wrap(ResultSet rs) {
		return new CountStarWrapper(rs);
	}

}
