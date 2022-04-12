package test.net.jplugin.common.kits;

public  class Clazz extends Clazz1{
	String a;
	String b;
	private int c;
	
	public void print(){
		super.print();
		System.out.println(a+" "+b+" "+c);
	}
}

class Clazz1{
	String x;
	private String y;
	private String z;
	public void print(){
		System.out.println(x+" "+y+" "+z);
	}
}