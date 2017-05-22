package net.jplugin.core.kernel.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.org.apache.log.Hierarchy;
import net.jplugin.org.apache.log.LogTarget;
import net.jplugin.org.apache.log.Logger;
import net.jplugin.org.apache.log.format.PatternFormatter;
import net.jplugin.org.apache.output.io.SafeFileTarget;
import net.jplugin.org.apache.output.io.StreamTarget;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午03:50:05
 **/

public class StartupLogger {
	
	private Logger logger;
	private Hierarchy hierarchy;

	public void addLog(String msg){
		addLog(msg,null);
	}
	public void addLog(String msg,Throwable th){
		Logger log = getLogger();
		log.info(msg,th);
	}
	
	/**
	 * @return
	 */
	private synchronized Logger getLogger() {
		if (logger==null){
			try {
				initLogger();
			} catch (IOException e) {
				throw new RuntimeException("init logger error",e);
			}
		}
		return logger;
	}


	/**
	 * 
	 */
	static String pattern = "[#thread]%{thread}[#time]%{time}%{message}\\n%{throwable} " ;
//	static String pattern = "%{message}\\n%{throwable} " ;
	
	private void initLogger() throws IOException {
		hierarchy = new Hierarchy();
		
		PatternFormatter formatter = new PatternFormatter( pattern );
		File file = new File( PluginEnvirement.getInstance().getWorkDir()	+ "/logs/"+getStartupLogFileName());
		
		net.jplugin.org.apache.log.Logger logger = hierarchy.getLoggerFor("KernelLogger");
		
//		AsyncLogTarget target = new AsyncLogTarget(new FileTarget(file,true,formatter));
//		Thread th = new Thread(target);
//		th.setPriority(Thread.MIN_PRIORITY); 
//		th.start();
		
		SafeFileTarget target = new SafeFileTarget(file, true, formatter);
		StreamTarget wt = new StreamTarget(System.out, formatter);
		
		logger.setLogTargets(new LogTarget[] {target,wt});
	}

	/**
	 * @return
	 */
	private static String getStartupLogFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
		return "startup_"+sdf.format(new Date());
	}


}
