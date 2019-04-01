package net.jplugin.core.das.route.impl.conn.mulqry.aggfunc;

import net.jplugin.core.das.route.api.AggFunctionEvalueContext;
import net.jplugin.core.das.route.api.IAggregationFunctionHandler;

public class CountAggFunction implements IAggregationFunctionHandler {
	final static String ROW_CNT = "RowCnt";
	@Override
	public void aggrate(AggFunctionEvalueContext ctx, Object currentRowValue, int rowCntForCurentValue,int coltype) {
		Long cnt = (Long) ctx.getAttribute(ROW_CNT);
		if (cnt==null)
			cnt = (long)rowCntForCurentValue;
		else 
			cnt += rowCntForCurentValue;
		ctx.setAttribute(ROW_CNT, cnt);	
	}

	@Override
	public Object getResult(AggFunctionEvalueContext ctx,int sqlType) {
		return ctx.getAttribute(ROW_CNT);
	}

}
