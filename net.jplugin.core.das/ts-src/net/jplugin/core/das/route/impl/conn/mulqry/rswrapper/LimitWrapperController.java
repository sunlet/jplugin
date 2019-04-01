package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

import net.jplugin.core.das.route.impl.CombinedSqlContext;
import net.jplugin.core.das.route.impl.util.SelectSqlKit;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;

public class LimitWrapperController implements WrapperController{

	private static final String LIMIT_INFO = "LIMIT-INFO";

	@Override
	public boolean needWrap() {
		Object limitinfo = CombinedSqlContext.get().getAttribute(LIMIT_INFO);
		if (limitinfo !=null)
			return true;
		return false;
	}

	@Override
	public ResultSet wrap(ResultSet rs) {
		LimitInfo info = (LimitInfo) CombinedSqlContext.get().getAttribute(LIMIT_INFO);
		return new LimitWrapper(rs,info);
	}

	@Override
	public void handleContextInitial(CombinedSqlContext ctx) {
		SelectBody bd = ctx.getStatement().getSelectBody();
		PlainSelect inner = SelectSqlKit.getMostInnerSelect(bd, ctx.getOriginalSql());
		Limit limit = inner.getLimit();
		if (limit!=null){
			LimitInfo limitInfo = makeLimitInfo(limit);
			ctx.setAttribute(LIMIT_INFO,limitInfo);
			inner.getLimit().setOffset(new LongValue(0L));
			inner.getLimit().setRowCount(new LongValue(limitInfo.offset + limitInfo.rowCount));
		}
	}
	
	private LimitInfo makeLimitInfo(Limit limit) {
		Long rc = getIntValue(limit.getRowCount());
		Long offset = getIntValue(limit.getOffset());
		if (rc==null) throw new RuntimeException("Row count can't be null.");
		if (offset==null) offset = 0L;
		LimitInfo li = new LimitInfo(rc,offset);
		return li;
	}

	private Long getIntValue(Expression e) {
		if (e instanceof LongValue){
			return ((LongValue) e).getValue();
		}else if (e instanceof JdbcParameter){
			throw new RuntimeException("Only const value is allowed for limit query parameter.");
		}else
			return null;
	}

	public static class LimitInfo{
		private Long offset;
		private Long rowCount;

		public LimitInfo(Long rc, Long offset) {
			this.offset = offset;
			this.rowCount = rc;
		}

		public Long getOffset() {
			return offset;
		}

		public Long getRowCount() {
			return rowCount;
		}
		
	}

}
