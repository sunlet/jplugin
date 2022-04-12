package net.jplugin.core.das.route.impl.conn.mulqry.aggfunc;

import java.math.BigDecimal;
import java.sql.SQLException;

import net.jplugin.core.das.route.api.AggFunctionEvalueContext;

/**
 * 经过验证，mysql的int类型Object类型为Int，但是sql类型为Decimial，不一致！
 * 调整方案，使用第一个不为null的java类型，而不用sql类型来判断了！！
 * 
 * @author LiuHang
 *
 */
public class SumAggFunction extends BaseMathAggFunctionHandler {
	final static String SUM_VALUE = "SUM";

	@Override
	public Object getResult(AggFunctionEvalueContext ctx, int sqlType) throws SQLException {
		return ctx.getAttribute(SUM_VALUE);
	}

	@Override
	void aggrateValue(AggFunctionEvalueContext ctx, Class javaType, Object currentRowValue, int rowCount)
			throws SQLException {
		Object currentValue = currentRowValue;
		Object sumValue = ctx.getAttribute(SUM_VALUE);

		if (sumValue == null)
			sumValue = currentValue;
		else
			sumValue = add(javaType, sumValue, currentValue);

		ctx.setAttribute(SUM_VALUE, sumValue);
	}

	private Object add(Class javaType, Object a, Object b) throws SQLException {
		if (javaType == Integer.class || javaType == int.class) {
			return (Integer) a + (Integer) b;
		}
		if (javaType == Short.class || javaType == short.class) {
			return (Short) a + (Short) b;
		}
		if (javaType == Byte.class || javaType == short.class) {
			return (Byte) a + (Byte) b;
		}
		if (javaType == Float.class || javaType == float.class) {
			return (Float) a + (Float) b;
		}
		if (javaType == Double.class || javaType == double.class) {
			return (Double) a + (Double) b;
		}
		if (javaType == BigDecimal.class) {
			return ((BigDecimal) a).add((BigDecimal) b);
		}
		throw new SQLException("unsupport type to sum:" + javaType);
	}

}
