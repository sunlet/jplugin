package net.jplugin.ext.gtrace.api;

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
	
	public Span(int spanType, String newSpanId,String parentSpanId) {
		this.type = spanType;
		this.id = newSpanId;
		this.parentId = parentSpanId;
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
}
