package net.jplugin.core.das.route.impl.conn.mulqry.aggfunc;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Types;

import net.jplugin.core.das.route.api.AggFunctionEvalueContext;
import net.jplugin.core.das.route.api.IAggregationFunctionHandler;
import net.jplugin.core.das.route.impl.util.SqlTypeKit;

/**
 * 经过验证，mysql的int类型Object类型为Int，但是sql类型为Decimial，不一致！
 * 调整方案，使用第一个不为null的java类型，而不用sql类型来判断了！！
 * 
 * @author LiuHang
 *
 */
public abstract class BaseMathAggFunctionHandler implements IAggregationFunctionHandler {
	private static final String JAVA_TYPE = "JAVA_TYPE";

	@Override
	public void aggrate(AggFunctionEvalueContext ctx, Object currentRowValue, int rowCntForCurentValue, int colType)
			throws SQLException {
		// 碰到null，什么也不做,这一行不聚集
		if (currentRowValue == null)
			return;

		Class javaType = (Class) ctx.getAttribute(JAVA_TYPE);
		if (javaType == null) {
			javaType = currentRowValue.getClass();
			ctx.setAttribute(JAVA_TYPE, javaType);
		}

		aggrateValue(ctx, javaType,currentRowValue, rowCntForCurentValue);
	}

	abstract void aggrateValue(AggFunctionEvalueContext ctx, Class javaType, Object currentRowValue, int rowCount)
			throws SQLException ;

////	private Object add(Class javaType, Object a, Object b) throws SQLException {
////		if (javaType == Integer.class || javaType == int.class) {
////			return (Integer) a + (Integer) b;
////		}
////		if (javaType == Short.class || javaType == short.class) {
////			return (Short) a + (Short) b;
////		}
////		if (javaType == Byte.class || javaType == short.class) {
////			return (Byte) a + (Byte) b;
////		}
////		if (javaType == Float.class || javaType == float.class) {
////			return (Float) a + (Float) b;
////		}
////		if (javaType == Double.class || javaType == double.class) {
////			return (Double) a + (Double) b;
////		}
////		if (javaType == BigDecimal.class) {
////			return ((BigDecimal) a).add((BigDecimal) b);
////		}
////		throw new SQLException("unsupport type to sum:" + javaType);
////	}
//
//	@Override
//	public Object getResult(AggFunctionEvalueContext ctx, int sqlType) throws SQLException {
//		Object sumvalue = ctx.getAttribute(OLD_VALUE);
//		if (sumvalue == null)
//			return null;
//
//		return sumvalue;
//
//	}

}
