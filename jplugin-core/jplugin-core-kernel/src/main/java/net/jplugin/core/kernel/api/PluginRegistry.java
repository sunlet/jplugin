package net.jplugin.core.kernel.api;

import java.util.*;

import net.jplugin.common.kits.Comparor;
import net.jplugin.common.kits.SortUtil;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.plugin_event.PluginEventListenerManager;
import net.jplugin.core.kernel.impl.PluginPrepareHelper;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 上午11:43:01
 **/

public class PluginRegistry {
	private List<AbstractPlugin> pluginList = new Vector<AbstractPlugin>();
	private List<PluginError> errorList = new Vector<PluginError>();
	private Hashtable<String, ExtensionPoint> extensionPointMap = new Hashtable<String, ExtensionPoint>();
	private Map<String, AbstractPlugin> loadedPluginMap = new Hashtable<String, AbstractPlugin>();
	private List<Class> pluginClasses=new ArrayList<>();

	public String printContent() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pluginList.size(); i++) {
			sb.append("\n");
			sb.append(pluginList.get(i).printContent());
		}
		return sb.toString();
	}


	public List<PluginError> getErrors() {
		return this.errorList;
	}

	public List<AbstractPlugin> getPluginList() {
		return Collections.unmodifiableList(this.pluginList);
	}


	public void addPluginClasses(Set<Object> pluginToLoad) {
		for (Object name:pluginToLoad){
			try {
				this.pluginClasses.add(Class.forName((String)name));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("The plugin class not found:"+name);
			}
		}
	}
//	/**
//	 * @param plugin
//	 */
//	public void addPlugin(IPlugin plugin) {
//		this.pluginList.add((AbstractPlugin) plugin);
//	}

	public void afterPluginsContruct() {
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin p = pluginList.get(i);
			p.afterPluginsContruct();
		}
	}

	public void afterPluginLoad() {
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin p = pluginList.get(i);
			p.afterPluginsLoad();
		}
	}

	public void afterWire() {
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin p = pluginList.get(i);
			p.afterWire();
		}
	}


	/**
	 * 先剔除重名
	 * 再按照优先级排序，小数字有线；相同数字的，按照名称排序
	 */
	public void sort() {
		//验证name是否有相同，相同则报错,加入错误列表
		checkSameName(errorList);
		//排序
		SortUtil.sort(this.pluginList, new Comparor() {

			public boolean isGreaterThen(Object o1, Object o2) {
				AbstractPlugin p1 = (AbstractPlugin) o1;
				AbstractPlugin p2 = (AbstractPlugin) o2;

				if (p1.getPrivority() > p2.getPrivority())
					return true;
				if (p1.getPrivority() < p2.getPrivority())
					return false;

				int nameCompResult = StringKit.compare(p1.getName(), p2.getName());
				if (nameCompResult == 0)
					throw new RuntimeException("shoudln't come here");

				return nameCompResult > 0;

			}
		});

		//优先级别一样的进行最优顺序推测
		PluginRegistryHelper.reorderSamePriorityPlugins(this.pluginList);
	}

	//推断空的point引用
	public void inferencePointToOfExtension(){
		Set<String> currentPointNameSet = new HashSet();
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin p = pluginList.get(i);
			for (ExtensionPoint point:p.getExtensionPoints()){
				currentPointNameSet.add(point.getName());
			}
			for (Extension extension:p.getExtensions()){
				if (StringKit.isNull(extension.getExtensionPointName())){
					String result = inference(extension,currentPointNameSet);
					if (result==null){
						errorList.add(new PluginError(p.getName(),"Can't inference extension's pointTo attribute. extension class is:"+extension.getClazz().getName() +"。 " +
								" In order to inference a extension's pointTo , the clazz must has a direct interface or subclass who's name is an extension point."  ) );
					}else{
						extension.setRefExtensionPoint( result);
					}
				}
			}
		}
		currentPointNameSet.clear();

		if (errorList.size()>0)
			throw new RuntimeException(errorList.toString());
	}

	private String inference(Extension extension, Set<String> pointNameSet) {
		List<String> nameList = new ArrayList<>();
		Class superclass = extension.getClazz().getSuperclass();
		if (!superclass.equals(Object.class)){
			nameList.add(superclass.getName());
		}
		for (Class c:extension.getClazz().getInterfaces()){
			nameList.add(c.getName());
		}

		String matched = null;
		for (String name:nameList){
			if (pointNameSet.contains(name)){
				if (matched==null){
					matched = name;
				}else{
					PluginEnvirement.getInstance().getStartLogger().log("ERROR: Dulicate matched when inference pointTo , names:["+matched+" , "+name+" ]");
					return null;
				}
			}
		}
		if (matched==null)
			PluginEnvirement.getInstance().getStartLogger().log("ERROR: All of the  names are not extension point when inference pointTo :"+ printContent(nameList));
		return matched;
	}

	private String printContent(List<String> names) {
		StringBuffer sb= new StringBuffer("[");
		for (String s:names){
			sb.append(s).append(" , ");
		}
		sb.append("]");
		return sb.toString();
	}


	/**
	 * 各个plugin自己验证自己,如果错误，设置error状态
	 */
	public void validAndFillExtensionPointMap() {
		this.extensionPointMap.clear();
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin p = pluginList.get(i);
			List<PluginError> ret = p.valid(this);
			if (!ret.isEmpty()) {
				p.setStatus(IPlugin.STAT_ERROR);
				errorList.addAll(ret);
			}
		}
		if (errorList.size()>0)
			throw new RuntimeException(errorList.toString());
	}

	void printPluginSequence(String title){
		StringBuffer sb = new StringBuffer(title).append("\n");

		for (int i=0;i<this.pluginList.size();i++){
			AbstractPlugin p = this.pluginList.get(i);
			sb.append("[").append(i).append("] ").append(p.getName()).append(" ").append(p.getPrivority()).append("  ").append("\n");
			for (ExtensionPoint ep:p.getExtensionPoints()){
				sb.append("  "+ep.getName()).append("\n");
			}
			if (p.getExtensions().size()>0){
				sb.append("  {");
				for (Extension e:p.getExtensions()){
					sb.append(e.getExtensionPointName()).append("  ");
				}
				sb.append("}\n");
			}
		}
		PluginEnvirement.getInstance().getStartLogger().log(sb.toString());
	}
	/**
	 * 各个Plugin自己加载自己，碰到异常则抛出
	 */
	public void load() {
		//加载
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			/**
			 * 如果经过valid以后，不是错误状态(是初始状态),则加载，并修改为LOADED状态
			 */
			if (plugin.getStatus() == IPlugin.STAT_INIT) {
				List<PluginError> ret = plugin.load();
				if (ret != null && !ret.isEmpty()) {
					plugin.setStatus(IPlugin.STAT_ERROR);
					errorList.addAll(ret);
				} else {
					plugin.setStatus(IPlugin.STAT_LOADED);
				}
			}
		}
		//add loaded plugin to pluginmap
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = pluginList.get(i);
			this.loadedPluginMap.put(plugin.getName(), plugin);
		}

	}


	/**
	 * 绑定Extension和ExtensionPoint之间关系，
	 * 只处理状态正常的plugin，不正常的忽略掉
	 *
	 * @return
	 */
	public void wire() {
		//valid的时候曾经放置过extensionPointMap,要全部清理掉
		this.extensionPointMap.clear();

		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED) {
				plugin.wire(this, this.errorList);
			}
		}

		//如果支持priority，进行一次sort
//		for (ExtensionPoint point:this.extensionPointMap.values()) {
//			if (point.supportPriority()) {
//				List<Extension> list = point.getExtensions();
//				SortUtil.sort(list, (o1,o2)-> ((Extension)o1).getPriority() >((Extension)o2).getPriority());
//			}
//		}
	}

	public void makeServices() {
		PluginEnvirement.INSTANCE.getStartLogger().log("==Now to create services==");
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED) {
				plugin.onCreateServices();
				//触发Plugin事件
				PluginEventListenerManager.afterCreateServices(plugin.getName());
			}
		}
	}


	/**
	 * 启动，执行各个Plugin的init
	 *
	 * @param testAll
	 * @param testTarget
	 */
	public void start(boolean testAll, String testTarget) {
		PluginEnvirement.INSTANCE.getStartLogger().log("==Now to init plugins==");
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED) {
				try {
					PluginEnvirement.INSTANCE.getStartLogger().log("StartPlugin :[" + i + "] " + plugin.getName());
					plugin.init();

					//如果不是全部测试，并且启动到目标插件，则停止
					if (!testAll && plugin.getName().equals(testTarget)) {
						break;
					}
				} catch (Exception e) {
//					e.printStackTrace();
					this.errorList.add(new PluginError(plugin.getName(), "error when start," + e.getMessage(), e));
					throw new RuntimeException("error to start:" + plugin.getName(), e);
				}
			}
		}
		PluginEnvirement.INSTANCE.getStartLogger().log("Total " + pluginList.size() + " plugin started!");
	}

	/**
	 * @param eList
	 */
	private void checkSameName(List<PluginError> eList) {
		HashMap<String, IPlugin> tmp = new HashMap<String, IPlugin>();

		for (int i = 0; i < pluginList.size(); i++) {
			IPlugin p = pluginList.get(i);
			String name = p.getName();
			if (StringKit.isNull(name)) {
				eList.add(new PluginError("null", "plugin name is null"));
				continue;
			}
			if (tmp.get(name) != null) {
				eList.add(new PluginError(name, "plugin same name with privious,removed"));
				continue;
			}
			tmp.put(name, p);
		}
	}

	public Map<String, ExtensionPoint> getExtensionPointMap() {
		return this.extensionPointMap;
	}

	public IPlugin getLoadedPlugin(String nm) {
		AbstractPlugin ret = this.loadedPluginMap.get(nm);
		if (ret == null) {
			throw new RuntimeException("the plugin not load correctly :" + nm);
		}
		return ret;
	}

	public void destroy() {
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			try {
				plugin.onDestroy();
			} catch (Exception e) {
				PluginEnvirement.INSTANCE.getStartLogger().log(e.getMessage(), e);
			}
		}
	}

	public void clearClassCache() {
		for (int i = 0; i < pluginList.size(); i++) {
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			plugin._cleanContainedClasses();
		}
	}

	public void handleDuplicateExtension() {
		Set<Extension> tempSet = new HashSet();

		for (AbstractPlugin p : this.pluginList) {

			List<Extension> extList = p.getExtensions();

			for (int i = extList.size() - 1; i >= 0; i--) {
				Extension ext = extList.get(i);
				if (tempSet.contains(ext)) {
					throw new RuntimeException("Warnning: Found dup extension in plugin:" + p.getName() + " ext=" + ext);
//					PluginEnvirement.getInstance().getStartLogger().log("Warnning: Found dup extension in plugin:" + p.getName() + " ext=" + ext);
//					extList.remove(i);
				} else {
					tempSet.add(ext);
				}
			}
		}
		tempSet.clear();
	}


	public void prepare() {
		PluginPrepareHelper.preparePlugins(this.pluginClasses);
	}

	public void construct() {
		for (Class c:this.pluginClasses){
			try {
				this.pluginList.add((AbstractPlugin) c.newInstance());
			} catch (InstantiationException e) {
				throw new RuntimeException("plugin contruct error:" + c.getName(),e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("plugin contruct error:"+c.getName(),e);
			}
		}
	}

	public List<Class> getPluginClasses() {
		return this.pluginClasses;
	}
}


