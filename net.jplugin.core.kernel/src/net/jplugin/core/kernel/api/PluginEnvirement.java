package net.jplugin.core.kernel.api;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.core.kernel.Plugin;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午03:03:01
 **/

public class PluginEnvirement {
	public static final String WORK_DIR = "work-dir";
	private static PluginEnvirement instance = new PluginEnvirement();
	private PluginRegistry registry = new PluginRegistry();
//	private StartupLogger startupLog = new StartupLogger();

	public static PluginEnvirement getInstance() {
		return instance;
	}

	public PluginRegistry getPluginRegistry() {
		return registry;
	}

	public boolean hasExtensionPoint(String pointName){
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		return point != null;
	}
	
	public ExtensionPoint getExtensionPoint(String pointName) {
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		if (point == null)
			throw new PluginRuntimeException("Can't find the point:"
					+ pointName);
		return point;
	}

	public Object[] getExtensionObjects(String pointName) {
		return getExtensionObjects(pointName, Object.class);
	}

	public <T> Map<String,T> getExtensionMap(String pointName,Class<T> type){
		return (Map<String, T>) getExtensionMap(pointName);
	}
	
	public Map<String,Object> getExtensionMap(String pointName){
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		if (point == null)
			throw new PluginRuntimeException("Can't find the extension point:"
					+ pointName);

		return point.getExtensionMap();
	}

	
	public <T> T[] getExtensionObjects(String pointName, Class<T> t) {
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		if (point == null)
			throw new PluginRuntimeException("Can't find the extension point:"
					+ pointName);

		return point.getExtensionObjects(t);
	}

	public String getWorkDir() {
		return System.getProperty(WORK_DIR, "./nswork");
	}

	String configDir = null;
	
	public void setConfigDir(String dir){
		this.configDir = dir;
		System.setProperty("plugin-config.path", configDir);
	}

	public synchronized String getConfigDir() {
		if (configDir == null) {
			configDir = PluginEnvirement.class.getClassLoader().getResource("")
					.getFile()
					+ "config";
			System.setProperty("plugin-config.path", configDir);
			//System.out.println("!!!!!!!!!!configdir="+configDir);
		}
		return configDir;
	}
	
	String webRootPath = null;
	public void setWebRootPath(String s){
		this.webRootPath = s;
	}
	public String getWebRootPath(){
		return webRootPath;
	}

	boolean started = false;

	public synchronized void startup() {
		startup(null);
	}
	
	/**
	 * <pre>
	 * 有testAll和testTarget两个属性
	 * testAll优先
	 * testAll：加载所有的测试插件，并全部运行
	 * testTarget：只加载特定的测试插件，并运行到这里为止
	 * </pre>
	 * @param plgns
	 */
	public synchronized void startup(Set plgns) {
		if (started)
			return;
		started = true;
		try {
			Set<Object> pluginToLoad = new HashSet<Object>();
			
			if (plgns==null){
				Properties prop = PropertiesKit.loadProperties(getConfigDir() + "/plugin.cfg");
				pluginToLoad.addAll(prop.keySet());
				pluginToLoad.addAll(CorePlugin.get());
				pluginToLoad.addAll(ExtPlugin.get());
			}else{
				pluginToLoad.addAll( plgns);
			}


			//有testAll和testTarget两个属性，testAll优先
			boolean testAll = false;
			String testTarget = null;
			testTarget = System.getProperty("testTarget");
			if ("true".equals(System.getProperty("testAll"))){
				testAll = true;
			}
			
			for (Object obj : pluginToLoad) {
				addPlugin(obj);
				
				//加载测试插件
				if (testAll){
					try{
						addPlugin("test."+obj);
					}catch(Exception e){}
				}else{
					if ( ("test."+obj).equals(testTarget)){
						try{
							addPlugin("test."+obj);
						}catch(Exception e){}
					}
				}
			}

			registry.sort();
			registry.valid();
			registry.load();
			registry.wire();
			if (registry.getErrors() == null || registry.getErrors().isEmpty()){
				try{
					ThreadLocalContextManager.instance.createContext();
					registry.start(testAll,testTarget);	
				}finally{
					ThreadLocalContextManager.instance.releaseContext();
				}
			}
			
			logStart(registry.getErrors());
			if (registry.getErrors() == null || registry.getErrors().isEmpty()){
				trigStartListener(null,null);
			}else{
				trigStartListener(null,registry.getErrors());
			}
		} catch (Exception e) {
			System.out.println("初始化过程发生错误");
			logError(e);
			if (PluginEnvirement.getInstance().hasExtensionPoint(Plugin.EP_STARTUP)){
				trigStartListener(e, null);
			}
			e.printStackTrace();
			throw new PluginRuntimeException(e);
		}
	}

	/**
	 * @param obj
	 */
	private void addPlugin(Object obj) {
		String cname = (String) obj;
		Object plugin;
		try {
			plugin = Class.forName(cname).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("plugin instance create error,"+e.getMessage()+obj, e);
		}
		registry.addPlugin((IPlugin) plugin);
	}

	/**
	 * @param e
	 * @param object
	 */
	private void trigStartListener(Exception e, List<PluginError> errors) {
		IStartup[] listeners = getExtensionObjects(Plugin.EP_STARTUP,IStartup.class);
		if (e==null && errors==null ){
			for (IStartup s:listeners){
				s.startSuccess();
			}
		}else{
			for (IStartup s:listeners){
				s.startFailed(e, errors);
			}
		}
	}

	public ConfigHelper getConfigHelper(String pluginname){
		return new ConfigHelper(registry.getLoadedPlugin(pluginname).getConfigures());
	}
	/**
	 * @param e
	 */
	private void logError(Exception e) {
		e.printStackTrace();
	}

	/**
	 * @param errors
	 */
	private void logStart(List<PluginError> errors) {
		if (errors == null || errors.size() == 0) {
			System.out.println("@@Plugin Loaded successfully!");
		} else {
			System.out.println("@@Plugin Loaded with errors ");
			for (PluginError e : errors) {
				System.out.println(e.toString());
			}
		}
	}
	

}
