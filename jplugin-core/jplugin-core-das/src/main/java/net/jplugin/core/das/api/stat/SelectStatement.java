package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.impl.stat.IFromItem;
import net.jplugin.core.das.impl.stat.IWhereSegment;
import net.jplugin.core.das.impl.stat.StringFromItem;
import net.jplugin.core.das.impl.stat.SubQueryFromItem;

public class SelectStatement extends ItemPairBasedStatement implements IStatement{
	List<IFromItem> fromList;
	String select;
	String having;
	String groupby;
	String orderby;
	
	SelectStatement(){}
	
	public static SelectStatement create(){
		return new SelectStatement();
	}

	public static SelectStatement create(String tbname,String select,String where,Object... para){
		SelectStatement ss = new SelectStatement();
		if (StringKit.isNotNull(select)){
			ss.select = select;
		}
		if (StringKit.isNotNull(tbname)){
			ss.addFrom(tbname,null);
		}
		if (StringKit.isNotNull(where)){
			ss.addWhere(where, para);
		}else{
			AssertKit.assertTrue(para==null || para.length==0);
		}
		return ss;
	}
	
	public void setSelect(String sel){
		this.select = sel;
	}
	public void setGroupby(String s){
		this.groupby = s;
	}
	public void setHaving(String s){
		this.having = s;
	}
	public void setOrderBy(String s){
		this.orderby = s;
	}
	
	public void addFrom(String tb,String alias){
		if (this.fromList==null){
			this.fromList = new ArrayList<IFromItem>(2);
		}
		AssertKit.assertNotNull(tb, "table or view name");
		AssertKit.assertTrue(tb.indexOf(' ')<0,tb);
		this.fromList.add(new StringFromItem(tb, alias));
	}
	public void addSubQryFrom(SelectStatement ss,String alias){
		if (this.fromList==null){
			this.fromList = new ArrayList<IFromItem>(2);
		}
		this.fromList.add(new SubQueryFromItem(ss, alias));
	}
	
	
	public String getSqlClause() {
		AssertKit.assertTrue(StringKit.isNull(select) || this.itemPairs==null);
		
		//组装SELECT
		StringBuffer sb = new StringBuffer("SELECT ");
		if (StringKit.isNotNull(select)){
			sb.append(select);
		}else if (this.itemPairs!=null){
			for (int i=0;i<itemPairs.size();i++){
				if (i!=0){
					sb.append(", ");
				}
				ItemPair ip = itemPairs.get(i);
				sb.append(ip.getName()).append(" ");
				if (ip.getValue()!=null){
					sb.append(ip.getValue());
				}
			}
		}else{
			throw new RuntimeException("Must have a select item in sql");
		}

		//组装from,可能存在from为空的特殊sql语句，比如mysql获取刚插入的主键
		if (fromList!=null && !fromList.isEmpty()){
			sb.append(" FROM ");
			for (int i=0;i<fromList.size();i++){
				IFromItem fi = fromList.get(i);
				if (i>0){
					sb.append(",");
				}
				fi.appendToClause(sb);
			}
		}
		//add where
		super.addWhereClause(sb);
		
		//组装groupby
		if (StringKit.isNotNull(groupby)){
			sb.append(" GROUP BY ").append(groupby);
		}
		//组装having
		if (StringKit.isNotNull(having)){
			sb.append(" HAVING ").append(having);
		}
		//orderby
		if (StringKit.isNotNull(orderby)){
			sb.append(" ORDER BY ").append(orderby);
		}
		
		return sb.toString();
	}
	public List<Object> getParams() {
		List<Object> ret =new ArrayList<Object>();
		if (fromList!=null){
			for (IFromItem fi:fromList){
				fi.addToBindList(ret);
			}
		}
		
		//add where paras
		super.addWhereParas(ret);
		
		return ret;
	}
}



