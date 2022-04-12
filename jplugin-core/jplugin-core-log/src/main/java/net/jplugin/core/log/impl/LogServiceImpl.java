package net.jplugin.core.log.impl;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.kits.KernelKit;
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
	private boolean inited=false;
	public LogServiceImpl() {
//        init();
    }
	
	public void initFromConfig(){
		Map<String, String> configs = ConfigFactory.getStringConfigInGroup("log4j");
		if (configs!=null && !configs.isEmpty()){
			PluginEnvirement.getInstance().getStartLogger().log("Using global logger config");
			Properties p = new Properties();
			for (Entry<String, String> e:configs.entrySet()){
				p.setProperty(e.getKey(), e.getValue());
			}
			initFromProperties(p);
		}else{
			PluginEnvirement.getInstance().getStartLogger().log("Using local logger config");
			initWithLocalConfig();
		}
		
		//set init 
		inited = true;
		LoggerListCacher.createLog4jLoggers();
	}

	private void initWithLocalConfig(){
//		String path = PluginEnvirement.getInstance().getConfigDir()+"/log4j.properties";
		String path = KernelKit.getConfigFilePath("log4j.properties");
		Properties prop =null;
		try{
			if (FileKit.existsFile(path))
				prop = PropertiesKit.loadProperties(path);
			else{
				PluginEnvirement.getInstance().getStartLogger().log("Using classpath logger config");
				prop = PropertiesKit.loadFromClassPath(this.getClass(),"log4j.properties");
			}
		}catch(Exception e){
			PluginEnvirement.INSTANCE.getStartLogger().log("Warnning : Log4j.properties not found at:"+path);
			return;
		}
		initFromProperties(prop);
	}
	
	private void initFromProperties(Properties prop){
		PropertiesKit.replaceVar(prop, PluginEnvirement.WORK_DIR, PluginEnvirement.getInstance().getWorkDir());
		PropertyConfigurator.configure(prop);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogService#getLogger(java.lang.String)
	 */
	public Logger getLogger(String name){
		if (!inited)
			return new Logger4Log4j(name);
		else
			return new Logger4Log4j(org.apache.log4j.Logger.getLogger(name));
	}
	
	
	Hashtable<String,Logger> calledNames=new Hashtable<String, Logger>();
	/* (non-Javadoc)
	 * @see net.luis.plugin.log.ILogService#createSpecicalLogger(java.lang.String)
	 */
	public Logger getSpecicalLogger(String filename){
		if (!inited) 
			throw new RuntimeException("Can't call getSpecicalLogger before inited!");
		
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
