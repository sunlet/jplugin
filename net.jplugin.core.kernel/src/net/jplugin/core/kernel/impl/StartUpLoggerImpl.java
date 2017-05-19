package net.jplugin.core.kernel.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.kernel.api.IStartLogger;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class StartUpLoggerImpl implements IStartLogger {

	private static final long LIMIT_SIZE = 1024*1024*1;
	@Override
	public synchronized void log(Object o) {
		FileKit.appendFile(getFile(),header()+ o.toString()+"\r\n");
		System.out.println(o);
	}

	private String header() {
		return CalenderKit.getFormatedTimeString(System.currentTimeMillis()," yyyy-MM-dd HH:mm:ss")+"  ";
	}

	@Override
	public synchronized void log(Object o, Throwable th) {
		FileKit.appendFile(getFile(), header()+o.toString()+"\r\n");
		System.out.println(o);
		
		FileKit.appendStackTrace(getFile(),th);
		th.printStackTrace();
	}
	
	

	@Override
	public synchronized void write(Object s) {
		FileKit.appendFile(getFile(), s.toString());
		System.out.print(s);
	}
	
	static String getFile(){
		String logger = PluginEnvirement.getInstance().getLogDir()+"/jplugin-start.log";
		if (FileKit.existsAndIsFile(logger)){
			if (FileKit.getFileSize(logger) > LIMIT_SIZE){
				renameAndDel(logger);
			}
		}
		return logger;
	}

	private static void renameAndDel(String logger) {
		int max = 5;
		removeFile(logger,max);
		for (int i=max;i>=0;i--){
			renameFile(logger,i);
		}
		FileKit.createEmptyFile(logger);
	}

	private static void renameFile(String logger,int i) {
		String from,to;
		if (i==0) from = logger;
		else from = logger+"."+i+".txt";
		to = logger+"."+(i+1)+".txt";
		FileKit.renameFile(from,to);
	}

	private static void removeFile(String logger, int i) {
		String name = logger+"."+i+".txt";
		if (FileKit.existsFile(name))
			FileKit.removeFile(name);
	}
	


}
