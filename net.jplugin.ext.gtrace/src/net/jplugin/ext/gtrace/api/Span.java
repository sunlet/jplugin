package net.jplugin.ext.gtrace.api;

import net.jplugin.common.kits.StringKit;

public class Span {
	public static final int EXPORT=0;
	public static final int CALLOUT=1;
	public static final int SYSTEM_INIT=2;	
	public static final int INTERNAL_CALL=3;
	public static final int INTERNAL_ASYN=4;
	public static final int SQL_EXEC=5;
	public static final int SCHDULE_EXEC=6;
	public static final int SPAN_DEFAULT = 9;//用来容错的默认span
	
	int type;
	String id;
	String parentId;
	int lastChildIndex=0;//上一次的child序号，初始0，然后从1开始
	
	public Span(int spanType, int theIndex,String parentSpanId) {
		this.type = spanType;
		this.id = makeId(theIndex,parentSpanId);
		this.parentId = parentSpanId;
	}
	private String makeId(int theIndex, String parentSpanId) {
		if (StringKit.isNull(parentSpanId)){
			return theIndex+"";
		}else{
			if (parentSpanId.length()>512)
				parentSpanId = parentSpanId.substring(0,510)+"..";
			return parentSpanId+"."+theIndex;
		}
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getLastChildIndex() {
		return lastChildIndex;
	}
	public void setLastChildIndex(int lastChildIndex) {
		this.lastChildIndex = lastChildIndex;
	}
	
}
