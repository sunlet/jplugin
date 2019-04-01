package net.luis.main.pojo;

import java.util.Date;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-18 下午04:19:02
 **/

public class UserBean {
	long id;
	String name;
	int age;
	Date born;
	Date updatetime;
	
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBorn() {
		return born;
	}
	public void setBorn(Date born) {
		this.born = born;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}
