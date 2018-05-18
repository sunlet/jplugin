package net.jplugin.core.kernel.api;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.Plugin;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.impl.AnnotationResolveHelper;
import net.jplugin.core.kernel.impl.PluginPrepareHelper;
import net.jplugin.core.kernel.impl.StartUpLoggerImpl;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午03:03:01
 **/

public class PluginEnvirement {
	public static final int STAT_LEVEL_PREPAREING=0;
	public static final int STAT_LEVEL_LOADING=10;
	public static final int STAT_LEVEL_WIRING=20;
	public static final int STAT_LEVEL_INITING=30;
	public static final int STAT_LEVEL_WORKING=40;
	
	public static final String WORK_DIR = "work-dir";
	public static PluginEnvirement INSTANCE = new PluginEnvirement();
	private PluginRegistry registry = new PluginRegistry();
//	private StartupLogger startupLog = new StartupLogger();
	String workdir=null;
	private AnnotationResolveHelper annoResolveHelper=new AnnotationResolveHelper(this);
	private int stateLevel=STAT_LEVEL_PREPAREING;
	
	private IStartLogger startLogger = new StartUpLoggerImpl();
	private boolean unitTesting = false;


	private PluginFilterManager<Tuple2<Boolean, String>> startFilterManager = new PluginFilterManager<>(
			net.jplugin.core.kernel.Plugin.EP_PLUGIN_ENV_INIT_FILTER, (fc, ctx) -> {
				registry.start(ctx.first, ctx.second);
				return null;
			});
	
	public static PluginEnvirement getInstance() {
		return INSTANCE;
	}
	
	public  void initStartFilter(){
		this.startFilterManager.init();
	}
	
	public IStartLogger getStartLogger() {
		return startLogger;
	}
	
	public int getStateLevel(){
		return this.stateLevel;
	}
	
	public boolean isUnitTesting() {
		return unitTesting;
	}

	public void setUnitTesting(boolean unitTesting) {
		this.unitTesting = unitTesting;
	}

	public void stop(){
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ now to stop plugin envirment");
		this.registry.destroy();
		this.registry = new PluginRegistry();
		this.started = false;
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ plugin envirment stopped");
	}

	public PluginRegistry getPluginRegistry() {
		return registry;
	}
	
	public boolean getStarted(){
		return this.started;
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

	public void setWorkDir(String dir){
		workdir = dir;
	}
	
	public String getWorkDir() {
		if (workdir==null){
			workdir = System.getProperty(WORK_DIR, "./nswork");
		}
		return workdir;
	}
	

	String configDir = null;
	
	public synchronized String getConfigDir() {
		if (configDir == null) {
			configDir = PluginEnvirement.class.getClassLoader().getResource("")
					.getFile()
					+ "config";
//			System.setProperty("plugin-config.path", configDir);
			//System.out.println("!!!!!!!!!!configdir="+configDir);
		}
		return configDir;
	}
	
	public void setConfigDir(String dir){
//		if (this.configDir==null){
//			configDir = System.getProperty(dir);
//		}
		this.configDir = dir;
//		System.setProperty("plugin-config.path", configDir);
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
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ ConfigDir="+this.getConfigDir());
			PluginEnvirement.INSTANCE.getStartLogger().log("$$$ WorkDir="+this.getWorkDir());
			Set<Object> pluginToLoad = new HashSet<Object>();
			
			if (plgns==null){
				if (FileKit.existsFile(getConfigDir() + "/plugin.cfg")){
					Properties prop = PropertiesKit.loadProperties(getConfigDir() + "/plugin.cfg");
					pluginToLoad.addAll(prop.keySet());
				}
				pluginToLoad.addAll(CorePlugin.get());
				pluginToLoad.addAll(ExtPlugin.get());
				pluginToLoad.addAll(PluginAutoDetect.get(pluginToLoad));
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
			
			PluginPrepareHelper.preparePlugins(pluginToLoad);
			
			
			for (Object obj : pluginToLoad) {
				addPlugin(obj);
				
				//加载测试插件
				if (testAll){
					addPluginIfExists("test."+obj);
				}else{
					if ( ("test."+obj).equals(testTarget)){
						addPluginIfExists("test."+obj);
					}
				}
			}

			registry.sort();
			registry.valid();
			this.stateLevel = STAT_LEVEL_LOADING;
			registry.load();
			this.stateLevel = STAT_LEVEL_WIRING;
			registry.wire();
			registry.makeServices();
			registry.clearClassCache();
			this.stateLevel = STAT_LEVEL_INITING;


			if (registry.getErrors() == null || registry.getErrors().isEmpty()){
				try{
					ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();
					
					this.annoResolveHelper.resolveHistory();
					startFilterManager.filter(Tuple2.with(testAll,testTarget));

				}finally{
					ThreadLocalContextManager.instance.releaseContext();
				}
			}
			
			if (registry.getErrors() == null || registry.getErrors().isEmpty()){
				trigStartListener(null,null);
			
				logStart(registry.getErrors());
			}else{
				trigStartListener(null,registry.getErrors());
				
				logStart(registry.getErrors());
				
				try{
					Thread.sleep(3000);
				}catch(Exception th){}
				System.exit(-2);
			}
			this.stateLevel = STAT_LEVEL_WORKING;
		} catch (Exception e) {
			PluginEnvirement.INSTANCE.getStartLogger().log("初始化过程发生错误",e);
//			logError(e);
			if (PluginEnvirement.getInstance().hasExtensionPoint(Plugin.EP_STARTUP)){
				trigStartListener(e, null);
			}
//			getStartLogger().log(e.getMessage(),e);
			
			try{
				Thread.sleep(3000);
			}catch(Exception th){}
			
			System.exit(-2);
//			throw new PluginRuntimeException(e);
		}
	}

	private void addPluginIfExists(Object obj) {
		String cname = (String) obj;
		try {
			Class.forName(cname);
		} catch (ClassNotFoundException e) {
			return;
		}
		addPlugin(obj);
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
//	/**
//	 * @param e
//	 */
//	private void logError(Exception e) {
//		e.printStackTrace();
//	}

	/**
	 * @param errors
	 */
	private void logStart(List<PluginError> errors) {
		if (errors == null || errors.size() == 0) {
			PluginEnvirement.INSTANCE.getStartLogger().log("@@Plugin Loaded successfully!");
		} else {
			PluginEnvirement.INSTANCE.getStartLogger().log("@@Plugin Loaded with errors ");
			for (PluginError e : errors) {
				PluginEnvirement.INSTANCE.getStartLogger().log(e.toString());
			}
		}
	}

	public void resolveRefAnnotation(Object o) {
		this.annoResolveHelper.resolveOne(o);
	}
	
	public String getEnvType(){
		return System.getProperty("plugin.env");
	}
	
	public String getLogDir(){
		return this.getWorkDir()+"/logs";
	}
	

}
