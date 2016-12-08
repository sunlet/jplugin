package net.jplugin.core.das.route.impl.conn;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.api.TablesplitException;

public class SqlParamRecoder {

	List<Object> params=new ArrayList<Object>();
	public List<Object> getList(){
		return params;
	}
	public void set(int sqlIndex,Object o){
		int index = sqlIndex-1;
		if (index<0) throw new TablesplitException("sql parameter index error:"+sqlIndex);
		if (index>1000)throw new TablesplitException(" parameter index overflow:"+sqlIndex);
		
		int size = params.size();
		if (index==size) params.add(o);
		else if (index<size) params.set(index, o);
		else {
			for (int i=0;i<index - size;i++){
				params.add(null);
			}
			params.add(o);
		}
	}
}
