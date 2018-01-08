package net.jplugin.core.das.route.impl.conn.mulqry;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import net.jplugin.core.das.route.api.TablesplitException;

public class ResultSetOrderByTool {
	enum OrderColumnType{STRING,BIGINTEGER,BIGDECIMAL,SQLDATE,SQLTIME,SQLTIMESTAMP}
	enum Direction{ASC,DESC}
	ColumnMeta[] columnMetas;
//	TreeSet<OrderComparor> comparorSet;
	PriorityQueue<OrderComparor> comparorQueue;
	
	public ResultSetOrderByTool(List<String> orderParam, ResultSet aResultSet) {
		//下面初始化columnMetas
		if (orderParam!=null && orderParam.size()>0){
			try {
				initColumnMetas(orderParam,aResultSet);
			} catch (SQLException e) {
				throw new TablesplitException(e.getMessage(),e);
			}
		}
		//下面初始化comparorSet
		if (orderParam!=null && orderParam.size()>0)
			comparorQueue = new PriorityQueue<OrderComparor>(new Comparator<OrderComparor>() {
				@Override
				public int compare(OrderComparor o1, OrderComparor o2) {
					return doCompare(o1.getComparorValue(),o2.getComparorValue(),columnMetas);
				}

				private int doCompare(Object[] v1, Object[] v2, ColumnMeta[] m) {
					
					for (int i=0;i<m.length;i++){
						Comparable o1 = (Comparable)v1[i];
						Comparable o2 = (Comparable)v2[i];
						ColumnMeta cm = m[i];
						
						int result = CompareKit.compareSupportNull(o1, o2);

						if (result!=0 && cm.getDirection()==Direction.DESC){
							result = - result;
						}
						//胜负分出来了，则返回
						if (result!=0) 
							return result;
					}
					//到这里可能没有比较出大小，一定是相等
					return 0;
				}
			});
		else 
			comparorQueue = new PriorityQueue<OrderComparor>(new Comparator<OrderComparor>() {
				@Override
				public int compare(OrderComparor o1, OrderComparor o2) {
					return o1.rsIndex - o2.rsIndex;
				}
			});
	}

	private void initColumnMetas(List<String> orderParam, ResultSet aResultSet) throws SQLException {
		//数组长度是逗号个数+1
		int cnt=0;
		for(String s:orderParam){
			if (",".equals(s)) cnt++;
		}
		//创建数组
		this.columnMetas = new ColumnMeta[cnt+1];
		//填充name 和direction
		ResultSetMetaData rsMeta = aResultSet.getMetaData();
		String colName,colDirection;
		int pos = 0 ;
		int metaIndex = 0;
		while(pos<orderParam.size()){
			String first = orderParam.get(pos);
			if (",".equals(first)){
				throw new TablesplitException(" [,] is not expected here. order params="+ toString(orderParam));
			}
			
			//第一个为colName
			colName = first;
			colDirection = null;
			
			//如果后面的一个不是 ",",那么就是 direction，并且pos向前推动
			if  ((pos<orderParam.size()-1) && !",".equals(orderParam.get(pos+1))){
				pos++;
				colDirection = orderParam.get(pos);
			}
			ColumnMeta cm = new ColumnMeta();
			cm.setColumnName(colName);
			cm.setDirection("desc".equalsIgnoreCase(colDirection)? Direction.DESC:Direction.ASC);
			
			//填充index
			int colIndex = getColumnIndex(cm.getColumnName(),rsMeta);
			if (colIndex!=-1){
				cm.setColumnIndex(colIndex);
			}else{
				throw new TablesplitException("The order by column ["+cm.getColumnName()+"] can't be found in the resultSet");
			}
			//填充type
			OrderColumnType  colType = getColunType(rsMeta,colIndex);
			if (colType!=null){
				cm.setColumnType(colType);
			}else{
				throw new TablesplitException("The order by column ["+cm.getColumnName()+"] type not support!");
			}
			
			columnMetas[metaIndex++]=cm;
			//把pos向前推2,因为如果有下一个一定是,
			pos+=2;
		}
	}

	private String toString(List<String> list) {
		if (list==null) return "";
		StringBuffer sb = new StringBuffer();
		for (String s:list){
			sb.append(" "+s);
		}
		return sb.toString();
	}

	static HashMap<Integer,OrderColumnType> typeMapping=new HashMap();
	static{
		typeMapping.put(Types.TINYINT, OrderColumnType.BIGINTEGER);
		typeMapping.put(Types.SMALLINT, OrderColumnType.BIGINTEGER);
		typeMapping.put(Types.INTEGER, OrderColumnType.BIGINTEGER);
		typeMapping.put(Types.BIGINT, OrderColumnType.BIGINTEGER);
		typeMapping.put(Types.FLOAT, OrderColumnType.BIGDECIMAL);
		typeMapping.put(Types.REAL, OrderColumnType.BIGDECIMAL);
		typeMapping.put(Types.DOUBLE, OrderColumnType.BIGDECIMAL);
		typeMapping.put(Types.NUMERIC, OrderColumnType.BIGDECIMAL);
		typeMapping.put(Types.DECIMAL, OrderColumnType.BIGDECIMAL);
		typeMapping.put(Types.CHAR, OrderColumnType.STRING);
		typeMapping.put(Types.VARCHAR, OrderColumnType.STRING);
		typeMapping.put(Types.LONGVARCHAR, OrderColumnType.STRING);
		typeMapping.put(Types.DATE, OrderColumnType.SQLDATE);
		typeMapping.put(Types.TIME, OrderColumnType.SQLTIME);
		typeMapping.put(Types.TIMESTAMP, OrderColumnType.SQLTIMESTAMP);
	}
	private OrderColumnType getColunType(ResultSetMetaData rsMeta, int index) throws SQLException {
		int sqlType = rsMeta.getColumnType(index);
		return  typeMapping.get(sqlType);
	}
	private int getColumnIndex(String columnName, ResultSetMetaData rsMeta) throws SQLException {
		int count = rsMeta.getColumnCount();
		columnName = columnName.toUpperCase();
		int index = -1;
		for (int i=1;i<=count;i++){
			String name = rsMeta.getColumnName(i).toUpperCase();
			if (name.endsWith(columnName)){
				int nameLen = name.length();
				int columnNameLen = columnName.length();
				//规则：相等  或者  是 .后面的后缀
				if ((nameLen == columnNameLen) ||(name.charAt(nameLen - columnNameLen-1)=='.')){
					index = i;
					break;
				}
			}
		}
		return index;
	}

	//取出
	public OrderComparor pollFirst(){
//		return comparorSet.pollFirst();
		return comparorQueue.poll();
	}
	
	//放入
	public void refreshAndAdd(OrderComparor oc,ResultSet rs){
		try{
			//放入，重新排序
			update(oc,rs);
			comparorQueue.add(oc);
		}catch(SQLException s){
			throw new TablesplitException(s.getMessage(),s);
		}
	}
	
	private void update(OrderComparor oc, ResultSet rs) throws SQLException {
		if (columnMetas==null || columnMetas.length==0){
			//不需要update
			return;
		}
		
		Object[] values = new Object[columnMetas.length];
		for (int i=0;i<columnMetas.length;i++){
			ColumnMeta meta = columnMetas[i];
			int colIndex = meta.getColumnIndex();
			Object value ;
			switch(meta.getColumnType()){
			case STRING:
				value = rs.getString(colIndex);
				break;
			case BIGDECIMAL:
				value = rs.getBigDecimal(colIndex);
				break;
			case BIGINTEGER:
				value = rs.getLong(colIndex);
				break;
			case SQLDATE:
				value = rs.getDate(colIndex);
				break;
			case SQLTIME:
				value = rs.getTime(colIndex);
				break;
			case SQLTIMESTAMP:
				value = rs.getTimestamp(colIndex);
				break;
			default:
				throw new RuntimeException("error type:"+meta.getColumnType());
			}
			values[i]= value;
			
			//设置
			oc.setComparorValue(values);
		}
	}

	static class ColumnMeta{
		String columnName;
		Direction direction;
		private OrderColumnType columnType;
		private int columnIndex;
		
		public int getColumnIndex() {
			return columnIndex;
		}
		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}
		public String getColumnName() {
			return columnName;
		}
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
		public OrderColumnType getColumnType() {
			return columnType;
		}
		public void setColumnType(OrderColumnType columnType) {
			this.columnType = columnType;
		}
		public Direction getDirection() {
			return direction;
		}
		public void setDirection(Direction direction) {
			this.direction = direction;
		}
	}
	
	static class OrderComparor {
		int rsIndex;
		Object[] comparorValue;
		public int getRsIndex() {
			return rsIndex;
		}
		public void setRsIndex(int rsIndex) {
			this.rsIndex = rsIndex;
		}
		public Object[] getComparorValue() {
			return comparorValue;
		}
		public void setComparorValue(Object[] comparorValue) {
			this.comparorValue = comparorValue;
		}
	}
}
