package net.jplugin.core.log.impl;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午11:34:28
 **/

public class LogServiceImpl implements ILogService {

	public LogServiceImpl() {
        init();
    }

	private void init(){
		String path = PluginEnvirement.getInstance().getConfigDir()+"/log4j.properties";
		Properties prop =null;
		try{
			if (FileKit.existsFile(path))
				prop = PropertiesKit.loadProperties(path);
			else
				prop = PropertiesKit.loadFromClassPath(this.getClass(),"log4j.properties");
		}catch(Exception e){
			System.out.println("Warnning : Log4j.properties not found at:"+path);
			return;
		}
		PropertiesKit.replaceVar(prop, PluginEnvirement.WORK_DIR, PluginEnvirement.getInstance().getWorkDir());
		PropertyConfigurator.configure(prop);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogService#getLogger(java.lang.String)
	 */
	public Logger getLogger(String name){
		return new Logger4Log4j(org.apache.log4j.Logger.getLogger(name));
	}
	
	
	Hashtable<String,Logger> calledNames=new Hashtable<String, Logger>();
	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogService#createSpecicalLogger(java.lang.String)
	 */
	public Logger getSpecicalLogger(String filename){
		Logger ret = (Logger) calledNames.get(filename);
		if (ret==null){
			synchronized (this) {
				ret = (Logger) calledNames.get(filename);
				if (ret==null){
					ret = createLogger(filename);
					calledNames.put(filename, ret);
				}
			}
		}
		return ret;
	}

	/**
	 * @param filename
	 * @return
	 */
	private Logger createLoggerOld(String filename) {
		org.apache.log4j.Logger theSpecifialLog = org.apache.log4j.Logger.getLogger("$"+filename);
		
		theSpecifialLog.setAdditivity(false);
		theSpecifialLog.setLevel(Level.DEBUG);
		DailyRollingFileAppender append;
		try {
			append = new DailyRollingFileAppender(new PatternLayout(),PluginEnvirement.getInstance().getWorkDir()+"/logs/"+filename,"'.'yyyy-MM-dd'.log'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		theSpecifialLog.addAppender(append);
		return new Logger4Log4j(theSpecifialLog);
	}
	/**
	 * @param filename
	 * @return
	 */
	private Logger createLogger(String filename) {
		org.apache.log4j.Logger theSpecifialLog = org.apache.log4j.Logger.getLogger("$"+filename);
		
		theSpecifialLog.setAdditivity(false);
		theSpecifialLog.setLevel(Level.DEBUG);
		RollingFileAppender append;
		try {
			PatternLayout layout = new PatternLayout();
			layout.setConversionPattern("%d %m %n");
			append = new RollingFileAppender(layout,PluginEnvirement.getInstance().getWorkDir()+"/logs/"+filename);
			append.setMaxBackupIndex(25);
			append.setMaxFileSize("20MB");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		theSpecifialLog.addAppender(append);
		return new Logger4Log4j(theSpecifialLog);
	}
}
