package net.jplugin.core.das.spands;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.das.spands.ExpressionEvaluator.Value;

public class SqlNumricOperationKit {

	static Map<JDBCType,Integer> typeLevel=new HashMap<JDBCType, Integer>();
	static {
		typeLevel.put(JDBCType.BIT, 1);//Integer
		typeLevel.put(JDBCType.SMALLINT, 2);//Integer
		typeLevel.put(JDBCType.INTEGER, 3);//Integer
		typeLevel.put(JDBCType.BIGINT, 4);//Long
		typeLevel.put(JDBCType.FLOAT, 5);//Float
		typeLevel.put(JDBCType.DOUBLE, 6);//Double
		typeLevel.put(JDBCType.DECIMAL, 7);//Decimal
	}
//	//java 只有 Integer,Long,Float,Double,
//	static Value transform(Value v, JDBCType totype) {
//		
//	}
//	public static Value add(Value vl, Value vr) {
//		
//		
//	}

}
