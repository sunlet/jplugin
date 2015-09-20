package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

public class CreateIndexStatement implements IStatement{
	String tbname;
	List<String> cols;
	String indexName;
	boolean unique;
	private CreateIndexStatement(){}
	public static CreateIndexStatement create(){
		CreateIndexStatement ret = new CreateIndexStatement();
		return ret;
	}
	public static CreateIndexStatement create(String idxname,String tbname,String colsString){
		CreateIndexStatement ret = new CreateIndexStatement();
		ret.setIndexName(idxname);
		ret.setTableName(tbname);
		String[] arr = StringKit.splitStr(colsString, " ");
		for (String c:arr){
			ret.addColumn(c);
		}
		return ret;
	}

	public void setUnique(boolean b){
		this.unique = b;
		throw new RuntimeException("unique not support now");
	}
	public void setIndexName(String idxname){
		this.indexName = idxname;
	}
	public void setTableName(String s){
		this.tbname = s;
	}
	
	public void addColumn(String col){
		if (this.cols == null){
			this.cols = new ArrayList<String>(4);
		}
		this.cols.add(col);
	}
	
	public String getSqlClause() {
		AssertKit.assertNotNull(this.tbname,"tbname");
		AssertKit.assertNotNull(this.indexName, "idxname");
		AssertKit.assertNotNull(this.cols, "cols");
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE INDEX ").append(indexName).append(" ON ");
		sb.append(tbname).append(" (");
		
		for (int i=0;i<this.cols.size();i++){
			if (i>0){
				sb.append(", ");
			}
			sb.append(cols.get(i));
		}
		
		sb.append(")");
		
		return sb.toString();
	}
	public List<Object> getParams() {
		return new ArrayList(0);
	}
}
