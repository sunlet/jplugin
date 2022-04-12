package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

public class InsertStatement  implements
		IStatement {
	String tbName;
	String itemString;
	List<String> itemList;
	String valuesString;
	List<String> valuesList;
	List<Object> bindList;

	private InsertStatement() {
	}

	public static InsertStatement create() {
		return new InsertStatement();
	}

	public static InsertStatement create(String tb, String colsStr, String valuesStr,
			Object... para) {
		InsertStatement ret = new InsertStatement();
		if (StringKit.isNotNull(colsStr)) {
			ret.setItemString(colsStr);
		}
		if (StringKit.isNotNull(tb)) {
			ret.setTableName(tb);
		}
		if (StringKit.isNotNull(valuesStr)) {
			ret.setValeusString(valuesStr);
		}
		if (para != null) {
			ret.addParams(para);
		}
		return ret;
	}

	public void setTableName(String nm) {
		this.tbName = nm;
	}

	public void setItemString(String s) {
		this.itemString = s;
	}

	public void setValeusString(String s) {
		this.valuesString = s;
	}

	public void addItem(String s) {
		if (itemList == null) {
			itemList = new ArrayList<String>();
		}
		itemList.add(s);
	}

	public void addValue(String s) {
		if (valuesList == null) {
			valuesList = new ArrayList<String>();
		}
		valuesList.add(s);
	}

	public void addParams(Object[] oarr) {
		for (Object o : oarr) {
			addParam(o);
		}
	}

	public void addParam(Object o) {
		if (bindList == null) {
			bindList = new ArrayList<Object>();
		}
		bindList.add(o);
	}

	public String getSqlClause() {
		AssertKit.assertTrue(StringKit.isNull(itemString) || this.itemList==null);
		AssertKit.assertTrue(StringKit.isNull(valuesString) || this.valuesList==null);
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ").append(this.tbName);
		if (StringKit.isNotNull(this.itemString)) {
			sb.append("(");
			sb.append(itemString);
			sb.append(")");
		} else if (itemList != null) {
			sb.append("(");
			for (int i = 0; i < itemList.size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(itemList.get(i));
			}
			sb.append(")");
		}
		sb.append(" VALUES (");
		if (this.valuesString!=null){
			sb.append(valuesString);
		}else if (this.valuesList!=null){
			for (int i=0;i<this.valuesList.size();i++){
				if (i>0){
					sb.append(", ");
				}
				sb.append(valuesList.get(i));
			}
		}
		sb.append(" )");
		return sb.toString();
	}

	public List<Object> getParams() {
		if (bindList == null) {
			return new ArrayList<Object>(0);
		} else {
			return bindList;
		}
	}

}
