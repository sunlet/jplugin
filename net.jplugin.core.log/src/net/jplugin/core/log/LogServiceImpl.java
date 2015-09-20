package net.jplugin.core.log;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.log.api.Logger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-7 下午11:34:28
 **/

public class LogServiceImpl implements ILogService {
	public void init(){
		Properties prop = PropertiesKit.loadProperties(PluginEnvirement.getInstance().getConfigDir()+"/log4j.properties");
		PropertiesKit.replaceVar(prop, PluginEnvirement.WORK_DIR, System.getProperty(PluginEnvirement.WORK_DIR));
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
	private Logger createLogger(String filename) {
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
}
