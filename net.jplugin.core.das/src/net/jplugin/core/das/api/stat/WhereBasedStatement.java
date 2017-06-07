package net.jplugin.core.das.api.stat;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.impl.stat.IWhereSegment;
import net.jplugin.core.das.impl.stat.StringWhereSegment;
import net.jplugin.core.das.impl.stat.SubQueryWhereSegment;

public class WhereBasedStatement {

	protected List<IWhereSegment> whereSegments;

	public void addWhere(String where, Object... para) {
		if (this.whereSegments==null){
			this.whereSegments = new ArrayList<IWhereSegment>();
		}
		this.whereSegments.add(new StringWhereSegment(where, para));
	}

	public void addSubQryWhere(SelectStatement ss) {
		if (this.whereSegments==null){
			this.whereSegments = new ArrayList<IWhereSegment>();
		}
		this.whereSegments.add(new SubQueryWhereSegment(ss));
	}
	
	protected void addWhereClause(StringBuffer sb) {
		//组装where
		if (whereSegments!=null && !whereSegments.isEmpty()){
			sb.append(" WHERE ");
			for (IWhereSegment wi:whereSegments){
				sb.append(wi.getString()).append(" ");
			}
		}
		sb.append(" ");
	}
	
	protected void addWhereParas(List<Object> list) {
		if (whereSegments!=null){
			for (IWhereSegment ws:whereSegments){
				ws.addToBindList(list);
			}
		}
	}

}
