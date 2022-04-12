package net.jplugin.common.kits.ant;

import net.jplugin.common.kits.FileKit;

public class CreateFileTask {
	
	private String fileName;

	public void execute() throws Exception{
		FileKit.string2File("", fileName);
	}
	
	public void setFileName(String f){
		this.fileName = f;
		System.out.println("$$$ to create file = "+f);
	}
	
//	public static void main(String[] args) throws Exception {
//		FileKit.string2File("aaa", "d:/aaa.txt");
//	}

}
