package net.jplugin.core.das.route.api;




public class KeyValueForAlgm{
	/**
	 * <PRE>
	 * EQUAL:等于
	 * IN:枚举多个
	 * BETWEEN:在某个范围
	 * ALL:所有数据，无条件
	 *</PRE>
	 */
	public enum Operator{EQUAL,IN,BETWEEN,ALL}
	Operator operator;
	Object[] constValue;
	
	public KeyValueForAlgm(Operator o,Object[] obj){
		this.operator = o;
		this.constValue = obj;
	}
	public Operator getOperator() {
		return operator;
	}
	public Object[] getConstValue() {
		return constValue;
	}
	
}