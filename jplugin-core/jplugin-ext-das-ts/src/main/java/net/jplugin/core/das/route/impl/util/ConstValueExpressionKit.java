package net.jplugin.core.das.route.impl.util;

import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;

public class ConstValueExpressionKit {
	
	public static Object tryGetConstValue(Expression item){
		if (item instanceof StringValue){
			return ((StringValue)item).getValue();
		}else if (item instanceof LongValue){
			return ((LongValue)item).getValue();
		}else if (item instanceof DoubleValue){
			return ((DoubleValue)item).getValue();
		}else if (item instanceof DateValue){
			return  ((DateValue)item).getValue();
		}else if (item instanceof TimeValue){
			return  ((TimeValue)item).getValue();
		}else if (item instanceof TimestampValue){	
			return  ((TimestampValue)item).getValue();
		}
		return null;
	}
}
