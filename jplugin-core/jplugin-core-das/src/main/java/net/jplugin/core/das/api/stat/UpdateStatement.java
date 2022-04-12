package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

public class UpdateStatement extends ItemPairBasedStatement implements IStatement{
	String tbName;
	String setString;
	List<Object> itemBindList;

	private UpdateStatement(){}
	
	public static UpdateStatement create(){
		return new UpdateStatement();
	}
	public static UpdateStatement create(String tbname,String setStr,String where,Object... para){
		UpdateStatement ret=new UpdateStatement();
		if (StringKit.isNotNull(tbname)){
			ret.setTableName(tbname);
		}
		if (StringKit.isNotNull(setStr)){
			ret.setSetString(setStr);
		}
		if (StringKit.isNotNull(where)){
			ret.addWhere(where, para);
		}else{
			AssertKit.assertNull(para);
		}
		return ret;
	}
	
	public void addItemBind(Object o){
		if (this.itemBindList==null){
			this.itemBindList = new ArrayList<Object>();
		}
		this.itemBindList.add(o);
	}
	
	public void setTableName(String nm){
		this.tbName = nm;
	}
	
	public void setSetString(String s){
		this.setString = s;
	}
	
	public String getSqlClause() {
		AssertKit.assertTrue(StringKit.isNull(setString) || this.itemPairs==null);
		
		AssertKit.assertNotNull(tbName, "update table name");
		StringBuffer sb=new StringBuffer("UPDATE ");
		sb.append(tbName);
		sb.append(" SET ");
		
		if (this.setString!=null){
			sb.append(setString);
		}else if (this.itemPairs!=null){
			for (int i=0;i<itemPairs.size();i++){
				ItemPair ip = itemPairs.get(i);
				if (i>0){
					sb.append(", ");
				}
				sb.append(ip.getName()).append("=");
				AssertKit.assertNotNull(ip.getValue(), "value seted");
				sb.append(ip.getValue());
			}
		}
		super.addWhereClause(sb);
		
		return sb.toString();
	}

	public List<Object> getParams() {
		List<Object> ret = new ArrayList<Object>();
		if (this.itemBindList!=null){
			ret.addAll(itemBindList);
		}
		super.addWhereParas(ret);
		return ret ;
	}

}
