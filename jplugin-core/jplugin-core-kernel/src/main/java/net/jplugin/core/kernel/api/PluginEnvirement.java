package net.jplugin.core.kernel.api;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ExceptionKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.PropertiesKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.Plugin;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.kernel.impl.AnnotationResolveHelper;
import net.jplugin.core.kernel.impl.StartUpLoggerImpl;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-2-6 下午03:03:01
 **/

public class PluginEnvirement {
	public static final int STAT_LEVEL_ORIGIN=0;
	public static final int STAT_LEVEL_PREPAREING=5;
	public static final int STAT_LEVEL_CONSTRUCTED=15;
	public static final int STAT_LEVEL_LOADING=10;
	public static final int STAT_LEVEL_LOADED=15;
	public static final int STAT_LEVEL_WIRING=20;
	public static final int STAT_LEVEL_WIRED=22;
	public static final int STAT_LEVEL_MAKINGSVC=25;

	public static final int STAT_LEVEL_MADESVC=26;

	public static final int STAT_LEVEL_RESOLVING_HIST=27;
//	public static final int STAT_LEVEL_RESOLVED_HIST=28;
	public static final int STAT_LEVEL_INITING=30;
	public static final int STAT_LEVEL_WORKING=40;

	public static final int STARTTYPE_BOTH=0;
	public static final int STARTTYPE_FIRST =1;
	public static final int STARTTYPE_SECOND =2;
	
	public static final String WORK_DIR = "work-dir";
	public static PluginEnvirement INSTANCE = new PluginEnvirement();
	private PluginRegistry registry = new PluginRegistry();
//	private StartupLogger startupLog = new StartupLogger();
	String workdir=null;
	private AnnotationResolveHelper annoResolveHelper=new AnnotationResolveHelper(this);
	private int stateLevel=STAT_LEVEL_ORIGIN;
	
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

	public <T> T getExtensionById(String id,Class<T> t){
		return ExtensionObjects.get(id,t);
	}
	public Object getExtensionById(String id){
		return ExtensionObjects.get(id);
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
//		this.stateLevel =
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ plugin envirment stopped");
	}

	public PluginRegistry getPluginRegistry() {
		return registry;
	}
	
	public boolean getStarted(){
		return this.stateLevel!=STAT_LEVEL_ORIGIN;
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
	/**
	 * 获取通过ExtensionPoint.createList()方法创建的扩展点上 注册的 扩展对象数组
	 * @param pointName
	 * @return
	 */
	public Object[] getExtensionObjects(String pointName) {
		return getExtensionObjects(pointName, Object.class);
	}

	/**
	 * 获取通过ExtensionPoint.createNamed()方法创建的扩展点上 注册的 扩展对象Map
	 * @param <T>
	 * @param pointName 扩展点名
	 * @param type 泛型类型
	 * @return
	 */
	public <T> Map<String,T> getExtensionMap(String pointName,Class<T> type){
		return (Map<String, T>) getExtensionMap(pointName);
	}
	
	/**
	 * 获取通过ExtensionPoint.createNamed()方法创建的扩展点上 注册的 扩展对象Map
	 * @param pointName
	 * @return
	 */
	public Map<String,Object> getExtensionMap(String pointName){
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		if (point == null)
			throw new PluginRuntimeException("Can't find the extension point:"
					+ pointName);

		return point.getExtensionMap();
	}
	
	/**
	 * 获取通过ExtensionPoint.createUnique()方法创建的扩展点上注册的 扩展对象
	 * @param <T>
	 * @param pointName  扩展点名称
	 * @param t  泛型类型
	 * @return
	 */
	public <T> T getExtension(String pointName,Class<T> t){
		ExtensionPoint point = this.registry.getExtensionPointMap().get(
				pointName);
		if (point == null)
			throw new PluginRuntimeException("Can't find the extension point:"
					+ pointName);

		return point.getExtension(t);
	}


	/**
	 * 获取通过ExtensionPoint.createList()方法创建的扩展点上 注册的 扩展对象数组
	 * @param <T>
	 * @param pointName  扩展点名称
	 * @param t  泛型类型
	 * @return
	 */
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

//	boolean started = false;

	public synchronized void startup() {
		startup(null,STARTTYPE_BOTH);
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
	public synchronized void startup(Set plgns ,int startType) {
		try {
			if (startType== STARTTYPE_FIRST || startType==STARTTYPE_BOTH) {
				AssertKit.assertTrue(this.stateLevel==STAT_LEVEL_ORIGIN);
				ready(plgns);
			}
			if (startType== STARTTYPE_SECOND || startType==STARTTYPE_BOTH) {
				AssertKit.assertTrue(this.stateLevel==STAT_LEVEL_MADESVC);
				resolveAndinitialize();
			}

			//处理启动过程中registry.getErrors()，有可能退出
			if (!handleStartErrors()){
				System.exit(-2);
			}
		} catch (Throwable e) {
			e = ExceptionKit.getRootCause(e);
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

	private void resolveAndinitialize() {

		this.stateLevel = STAT_LEVEL_RESOLVING_HIST;

		if (registry.getErrors() == null || registry.getErrors().isEmpty()) {
			try {
				ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();

				this.annoResolveHelper.resolveHistory();
			} finally {
				ThreadLocalContextManager.instance.releaseContext();
			}
		}
//		this.stateLevel = STAT_LEVEL_RESOLVED_HIST;


		if (registry.getErrors() == null || registry.getErrors().isEmpty()) {
			try {
				ThreadLocalContext ctx = ThreadLocalContextManager.instance.createContext();

				this.stateLevel = STAT_LEVEL_INITING;

				this.annoResolveHelper.initHistory();
				startFilterManager.filter(Tuple2.with(testAll,testTarget));

			}finally{
				ThreadLocalContextManager.instance.releaseContext();
			}
		}

		this.stateLevel = STAT_LEVEL_WORKING;
	}

	/**
	 * 返回是否需要继续
	 * @return
	 */
	private boolean handleStartErrors() {
		if (registry.getErrors() == null || registry.getErrors().isEmpty()){
			trigStartListener(null,null);

			logStart(registry.getErrors());
			return true;
		}else{
			trigStartListener(null,registry.getErrors());

			logStart(registry.getErrors());

			try{
				Thread.sleep(3000);
			}catch(Exception th){}
			return false;
		}
	}

	private void ready(Set plgns) {
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ ConfigDir="+this.getConfigDir());
		PluginEnvirement.INSTANCE.getStartLogger().log("$$$ WorkDir="+this.getWorkDir());
		PluginAutoDetect.addAutoDetectPackage("net.jplugin.extension");
		PluginAutoDetect.addAutoDetectPackage("net.jplugin.app");
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

		initTestingProperty();

		//查找需要加载的测试Plugin
		Set<String> testPluginClasses = new HashSet<>();
		for (Object obj : pluginToLoad) {
			//加载测试插件
			if (testAll){
				String c = getPluginClazzIfExists("test."+obj);
				if (c!=null){
					testPluginClasses.add(c);
				}
			}else{
				if ( ("test."+obj).equals(testTarget)){
//						addPluginIfExists("test."+obj);
					String c = getPluginClazzIfExists("test."+obj);
					if (c!=null){
						testPluginClasses.add(c);
					}
				}
			}
		}
		pluginToLoad.addAll(testPluginClasses);

		//add plugin classes
		registry.addPluginClasses(pluginToLoad);

		//Prepare
		registry.prepare();

		//Construct
		registry.construct();

		this.stateLevel = STAT_LEVEL_CONSTRUCTED;

		registry.sort();
		registry.inferencePointToOfExtension();

		registry.handleDuplicateExtension();
		registry.validAndFillExtensionPointMap();
		registry.afterPluginsContruct();

		this.stateLevel = STAT_LEVEL_LOADING;
		registry.load();

		this.stateLevel = STAT_LEVEL_LOADED;
		registry.afterPluginLoad();
		ExtensionObjects.initFromPluginList();//所有Extension加入ExtensionFactory

		this.stateLevel = STAT_LEVEL_WIRING;
		registry.wire();

		this.stateLevel = STAT_LEVEL_WIRED;
		registry.afterWire();

		this.stateLevel = STAT_LEVEL_MAKINGSVC;
		registry.makeServices();
		registry.clearClassCache();

		this.stateLevel = STAT_LEVEL_MADESVC;
	}

	boolean testAll = false;
	String testTarget = null;
	private void initTestingProperty(){
		//有testAll和testTarget两个属性，testAll优先
		testTarget = System.getProperty("testTarget");
		if ("true".equals(System.getProperty("testAll"))){
			testAll = true;
		}
	}


	private String getPluginClazzIfExists(String cname) {
		try {
			Class.forName(cname);
			return cname;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

//	private void addPluginIfExists(Object obj) {
//		String cname = (String) obj;
//		try {
//			Class.forName(cname);
//		} catch (ClassNotFoundException e) {
//			return;
//		}
//		addPlugin(obj);
//	}

//	/**
//	 * @param obj
//	 */
//	private void addPlugin(Object obj) {
//		String cname = (String) obj;
//		Object plugin;
//		try {
//			plugin = Class.forName(cname).newInstance();
//		} catch (Exception e) {
//			throw new RuntimeException("plugin instance create error,"+e.getMessage()+obj, e);
//		}
//		registry.addPlugin((IPlugin) plugin);
//	}


	private void trigStartListener(Throwable e, List<PluginError> errors) {
		IStartup[] listeners = getExtensionObjects(Plugin.EP_STARTUP,IStartup.class);
		if (e==null && errors==null ){
			for (int i=listeners.length-1;i>=0;i--){
				IStartup s = listeners[i];
				s.startSuccess();
			}
		}else{
			for (int i=listeners.length-1;i>=0;i--){
				IStartup s = listeners[i];
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
