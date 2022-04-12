package net.jplugin.common.kits.ant;

import net.jplugin.common.kits.FileKit;

public class CreateBinsTask {
	private String dir;

	public void execute() throws Exception{
		String s = FileKit.classPathFile2String(this.getClass(), "startup.cmd", "utf-8");
		FileKit.string2File(s, dir+"/startup.cmd");
		s = FileKit.classPathFile2String(this.getClass(), "startup.sh", "utf-8");
		FileKit.string2File(s, dir+"/startup.sh");
	}
	
	public void setDir(String f){
		FileKit.makeDirectory(f);
		this.dir = f;
	}
}
