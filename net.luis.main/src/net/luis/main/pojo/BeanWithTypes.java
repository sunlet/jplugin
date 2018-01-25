package net.luis.main.pojo;

import java.util.Date;

import net.jplugin.core.das.hib.api.Entity;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-24 下午04:16:44
 **/
@Entity(indexes={"long1","id","LONG2","f1,  f2"})
public class BeanWithTypes {
	long id;
	long long1 =1l;
	Long long2 =2l;
	String string ="aa";
	Date theDate =new Date();
	Date theTime =new Date();
	double d1 =1.1d;
	Double d2 =1.2d;
	int integer1 =1;
	Integer integer2=2;
	float f1 =1.1f;
	Float f2 =1.2f;
	boolean b1 = true;
	Boolean b2 = false;
	
	public boolean getB1() {
		return b1;
	}
	public void setB1(boolean b1) {
		this.b1 = b1;
	}
	public Boolean getB2() {
		return b2;
	}
	public void setB2(Boolean b2) {
		this.b2 = b2;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLong1() {
		return long1;
	}
	public void setLong1(long long1) {
		this.long1 = long1;
	}
	public Long getLong2() {
		return long2;
	}
	public void setLong2(Long long2) {
		this.long2 = long2;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public Date getTheDate() {
		return theDate;
	}
	public void setTheDate(Date theDate) {
		this.theDate = theDate;
	}
	public Date getTheTime() {
		return theTime;
	}
	public void setTheTime(Date theTime) {
		this.theTime = theTime;
	}
	
	
	public double getD1() {
		return d1;
	}
	public void setD1(double d1) {
		this.d1 = d1;
	}
	public Double getD2() {
		return d2;
	}
	public void setD2(Double d2) {
		this.d2 = d2;
	}
	
	

	public float getF1() {
		return f1;
	}
	public void setF1(float f1) {
		this.f1 = f1;
	}
	public Float getF2() {
		return f2;
	}
	public void setF2(Float f2) {
		this.f2 = f2;
	}
	public int getInteger1() {
		return integer1;
	}
	public void setInteger1(int integer1) {
		this.integer1 = integer1;
	}
	public Integer getInteger2() {
		return integer2;
	}
	public void setInteger2(Integer integer2) {
		this.integer2 = integer2;
	}

}
