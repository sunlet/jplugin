package net.jplugin.core.ctx.impl.filter4clazz;

public class RuleCallFilterDefineBean {
	Class filterClass;
	String applyTo;
	int priority;
	
	public Class getFilterClass() {
		return filterClass;
	}
	public void setFilterClass(Class filterClass) {
		this.filterClass = filterClass;
	}
	public String getApplyTo() {
		return applyTo;
	}
	public void setApplyTo(String applyFor) {
		this.applyTo = applyFor;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
