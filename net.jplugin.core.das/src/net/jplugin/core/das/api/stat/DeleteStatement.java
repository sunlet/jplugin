package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

public class DeleteStatement extends ItemPairBasedStatement implements IStatement{
	String tbName;

	private DeleteStatement(){}
	public static DeleteStatement create(){
		return new DeleteStatement();
	}

	
	public static DeleteStatement create(String tbname,String where,Object... para){
		DeleteStatement ret=new DeleteStatement();
		ret.setTableName(tbname);
		if (StringKit.isNotNull(where)){
			ret.addWhere(where, para);
		}else{
			AssertKit.assertNull(para);
		}
		return ret;
	}

	
	public void setTableName(String nm){
		this.tbName = nm;
	}
	
	public String getSqlClause() {
		AssertKit.assertNotNull(tbName, "table name");
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ").append(tbName);
		super.addWhereClause(sb);
		return sb.toString();
	}

	public List<Object> getParams() {
		List<Object> ret = new ArrayList<Object>();
		super.addWhereParas(ret);
		return ret ;
	}

}
