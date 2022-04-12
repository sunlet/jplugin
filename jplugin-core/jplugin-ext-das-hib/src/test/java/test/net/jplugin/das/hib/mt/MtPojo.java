package test.net.jplugin.das.hib.mt;

import net.jplugin.core.das.api.BasicMtBean;

public class MtPojo extends BasicMtBean{
	long id;
	String name;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
