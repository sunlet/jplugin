package net.jplugin.core.kernel.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.jplugin.common.kits.Comparor;
import net.jplugin.common.kits.SortUtil;
import net.jplugin.common.kits.StringKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-22 上午11:43:01
 **/

public class PluginRegistry {
	private List<AbstractPlugin> pluginList = new Vector<AbstractPlugin>();
	private List<PluginError> errorList = new Vector<PluginError>();
	private Hashtable<String, ExtensionPoint> extensionPointMap =  new Hashtable<String, ExtensionPoint>();
	private Map<String,AbstractPlugin> loadedPluginMap = new Hashtable<String, AbstractPlugin>();
	
	public List<PluginError> getErrors(){
		return this.errorList;
	}
	
	/**
	 * @param plugin
	 */
	public void addPlugin(IPlugin plugin){
		this.pluginList.add((AbstractPlugin) plugin);
	}
	/**
	 * 先剔除重名
	 * 再按照优先级排序，小数字有线；相同数字的，按照名称排序
	 */
	public void sort(){
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
				
				int nameCompResult = StringKit.compare(p1.getName(),p2.getName());
				if (nameCompResult ==0 ) 
					throw new RuntimeException("shoudln't come here");
				
				return nameCompResult > 0;
				
			}
		});
	}
	
	/**
	 * 各个plugin自己验证自己,如果错误，设置error状态
	 */
	public void valid(){
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin p = pluginList.get(i);
			List<PluginError> ret = p.valid(this);
			if (!ret.isEmpty()){
				p.setStatus(IPlugin.STAT_ERROR);
				errorList.addAll(ret);
			}
		}
	}
	
	/**
	 * 各个Plugin自己加载自己，碰到异常则抛出
	 */
	public void load(){
		//加载
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			/**
			 * 如果经过valid以后，不是错误状态(是初始状态),则加载，并修改为LOADED状态
			 */
			if (plugin.getStatus() == IPlugin.STAT_INIT){
				List<PluginError> ret = plugin.load();
				if (ret!=null && !ret.isEmpty()){
					plugin.setStatus(IPlugin.STAT_ERROR);
					errorList.addAll(ret);
				}else{
					plugin.setStatus(IPlugin.STAT_LOADED);
				}
			}
		}
		//add loaded plugin to pluginmap
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = pluginList.get(i);
			this.loadedPluginMap.put(plugin.getName(),plugin);
		}
	}
	
	
	/**
	 * 绑定Extension和ExtensionPoint之间关系，
	 * 只处理状态正常的plugin，不正常的忽略掉
	 * @return 
	 */
	public void contrib(){
		//valid的时候曾经放置过extensionPointMap,要全部清理掉
		this.extensionPointMap.clear();
		
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED){
				plugin.contrib(this,this.errorList);
			}
		}
	}
	
	/**
	 * 对Extension处理annotation的属性值
	 */
	public void wire() {
		IAnnoForAttrHandler[] handlers = getExtensionPointMap().get(net.jplugin.core.kernel.Plugin.EP_ANNO_FOR_ATTR).getExtensionObjects(IAnnoForAttrHandler.class);
		
		//valid
		HashSet checkSet = new HashSet();
		for (IAnnoForAttrHandler h:handlers){
			if (checkSet.contains(h.getAnnoClass())){
				throw new RuntimeException("Duplicate handler for annotation class:"+h.getAnnoClass().getName());
			}else{
				checkSet.add(h.getAnnoClass());
			}
		}
		//
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED){
				plugin.wire(handlers,errorList);
			}
		}
		
	}
	
	
	/**
	 * 启动，执行各个Plugin的init
	 * @param testAll 
	 * @param testTarget 
	 */
	public void start(boolean testAll, String testTarget){
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			if (plugin.getStatus() == IPlugin.STAT_LOADED){
				try{
					System.out.println("StartPlugin :["+i+"] "+plugin.getName());
					plugin.init();
					
					//如果不是全部测试，并且启动到目标插件，则停止
					if (!testAll && plugin.getName().equals(testTarget)){
						break;
					}
				}catch(Exception e){
//					e.printStackTrace();
					this.errorList.add(new PluginError(plugin.getName(),"error when start,"+e.getMessage(),e));
					throw new RuntimeException("error to start:"+plugin.getName(),e);
				}
			}
		}
		System.out.println("Total "+pluginList.size()+" plugin started!");
	}
	
	/**
	 * @param errorList2 
	 */
	private void checkSameName(List<PluginError> eList) {
		HashMap<String, IPlugin> tmp = new HashMap<String, IPlugin>();
		
		for (int i=0;i<pluginList.size();i++){
			IPlugin p = pluginList.get(i);
			String name = p.getName();
			if (StringKit.isNull(name)){
				eList.add(new PluginError("null","plugin name is null"));
				continue;
			}
			if (tmp.get(name)!=null){
				eList.add(new PluginError(name,"plugin same name with privious,removed"));
				continue;
			}
			tmp.put(name, p);
		}
	}
	
	public Map<String,ExtensionPoint> getExtensionPointMap(){
		return this.extensionPointMap;
	}
	
	public IPlugin getLoadedPlugin(String nm){
		AbstractPlugin ret = this.loadedPluginMap.get(nm);
		if (ret==null){
			throw new RuntimeException("the plugin not load correctly :"+nm);
		}
		return ret;
	}

	public void destroy() {
		for (int i=0;i<pluginList.size();i++){
			AbstractPlugin plugin = (AbstractPlugin) pluginList.get(i);
			try{
				plugin.onDestroy();
			}catch(Exception e){e.printStackTrace();}
		}
	}


}
