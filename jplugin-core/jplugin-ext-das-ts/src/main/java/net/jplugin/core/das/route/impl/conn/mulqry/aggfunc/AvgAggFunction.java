package net.jplugin.core.das.route.impl.conn.mulqry.aggfunc;

import java.math.BigDecimal;
import java.sql.SQLException;

import net.jplugin.core.das.route.api.AggFunctionEvalueContext;
import net.jplugin.core.das.route.impl.util.SqlTypeKit;

/**
 * 经过验证，mysql的int类型Object类型为Int，但是sql类型为Decimial，不一致！
 * 调整方案，使用第一个不为null的java类型，而不用sql类型来判断了！！
 * 
 * @author LiuHang
 *
 */
public class AvgAggFunction extends BaseMathAggFunctionHandler {
	final static String TOTAL_VALUE = "TOTAL_VALUE";
	final static String TOTAL_CNT = "TOTAL_CNT";

	@Override
	public Object getResult(AggFunctionEvalueContext ctx, int sqlType) throws SQLException {
		BigDecimal totalValue = (BigDecimal) ctx.getAttribute(TOTAL_VALUE);
		if (totalValue == null)
			return null;
		Long totalCnt = (Long) ctx.getAttribute(TOTAL_CNT);
		BigDecimal result = totalValue.divide(new BigDecimal(totalCnt));
		return result;
	}

	@Override
	void aggrateValue(AggFunctionEvalueContext ctx, Class javaType, Object currentRowValue, int rowCount)
			throws SQLException {
		BigDecimal currentValue = SqlTypeKit.get(currentRowValue,BigDecimal.class).multiply(new BigDecimal(rowCount));
		
		BigDecimal oldValue = (BigDecimal) ctx.getAttribute(TOTAL_VALUE);
		Long oldCount = (Long) ctx.getAttribute(TOTAL_CNT);
		if (oldValue == null){
			oldValue = currentValue;
			oldCount = (long)rowCount;
		}
		else{
			oldValue = oldValue.add(currentValue);
			oldCount = oldCount + rowCount;
		}

		ctx.setAttribute(TOTAL_VALUE, oldValue);
		ctx.setAttribute(TOTAL_CNT, oldCount);
	}
}
