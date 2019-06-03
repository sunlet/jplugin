package net.jplugin.core.kernel.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.reso.ResolverKit;

/**
 * @author: LiuHang
 * @version 创建时间：2015-2-22 上午11:43:12
 **/

public abstract class AbstractPlugin implements IPlugin {

	private List<ExtensionPoint> extensionPoints = new ArrayList<ExtensionPoint>();
	private List<Extension> extensions = new ArrayList<Extension>();
	private String pluginName = this.getClass().getName();
	private int status = IPlugin.STAT_INIT;
	private Hashtable<String,String> configreus = new Hashtable<String, String>();
	private Set<Class> containedClasses = null;

	@Override
	public void onCreateServices() {
	}
	
	protected void searchAndBindExtensions(){
		List<IBindExtensionHandler> handlers = AutoBindExtensionManager.INSTANCE.getHandlers();
		for (IBindExtensionHandler h:handlers){
			h.handle(this);
		}
	}

//	@Override
//	public void init() {
//		// TODO Auto-generated method stub
//		
//	}
//	
	public void addExtensionPoint(ExtensionPoint ep) {
		this.extensionPoints.add(ep);
	}

	public void addExtension(Extension e) {
		this.extensions.add(e);
	}

	public void addConfigure(String name,String val){
		if (this.configreus.containsKey(name)){
			throw new RuntimeException("duplicate config name:"+name);
		}
		this.configreus.put(name,val);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getExtensionPoints()
	 */
	public List<ExtensionPoint> getExtensionPoints() {
		return this.extensionPoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getExtensions()
	 */
	public List<Extension> getExtensions() {
		return this.extensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getName()
	 */
	public String getName() {
		return this.pluginName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.luis.common.kernal.IPlugin#getPrivority()
	 */
	public abstract int getPrivority();

	public int getStatus() {
		return this.status;
	}

	public List<PluginError> load() {
		List<PluginError> errList = null;
		// 如果不是初始状态，异常
		if (this.status != IPlugin.STAT_INIT)
			throw new RuntimeException(
					"Not init state,can't call load,plugin name:"
							+ this.getName());

		//逐个加载
		for (int i = 0; i < this.extensions.size(); i++) {
			try {
				this.extensions.get(i).load();
			} catch (Exception e) {
				if (errList==null){
					errList = new ArrayList<PluginError>();
				}
				errList.add(new PluginError(this.getName(), "extension load error."+this.extensions.get(i).getClazz(),e));
			}
		}
		return errList;
	}

	/**
	 * 逐个plugin先把ExtensionPoint放到池中，在去处理Extension，可以避免Plugin之间交叉引用：
	 * plugin1中的扩展引用plugin2中的扩展点，同时
	 * plugin2中的扩展引用plugin1中的扩展点
	 * @param pluginRegistry
	 * @param errorList 
	 */
	public void wire(PluginRegistry pluginRegistry, List<PluginError> errorList) {
		for (int i=0;i<this.getExtensionPoints().size();i++){
			ExtensionPoint ep = this.getExtensionPoints().get(i);
			pluginRegistry.getExtensionPointMap().put(ep.getName(), ep);
		}
		
		for (int i=0;i<this.getExtensions().size();i++){
			Extension e = this.getExtensions().get(i);
			String pname = e.getExtensionPointName();
			ExtensionPoint point = pluginRegistry.getExtensionPointMap().get(pname);
			
			if (point ==null){
//				throw new RuntimeException("Shoudn't be null ,because have valided! "+pname);
				errorList.add(new PluginError(this.getName(),"Couldn't find extension point for extension ,perhaps the extensionpoint plugin is not load correctly.extension= "+e.getName() +" pointname="+pname));
			}else{
				if (point.validExtensionName(e.getName())){
					point.addExtension(e);
				}else{
					errorList.add(new PluginError(this.getName(),"The extension name must be unique and notnull. extension= "+e.getName() +" pointname="+pname));
				}
			}
		}
	}

	
	/**
	 * @param pluginRegistry
	 * @param errorList 
	 */
	public List<PluginError> valid(PluginRegistry pluginRegistry) {
		List<PluginError> errors = new ArrayList<PluginError>();
		//检查point
		for (ExtensionPoint ep:this.extensionPoints){
			if (StringKit.isNull(ep.getName())){
				errors.add(new PluginError(this.getName(),"extension point name is null"));
			}
			ExtensionPoint finder = pluginRegistry.getExtensionPointMap().get(ep.getName());
			if (finder!=null){
				errors.add(new PluginError(this.getName(),"point name duplicated with old points:"+ep.getName()));
			}
			
			pluginRegistry.getExtensionPointMap().put(ep.getName(), ep);
		}
		//检查extension的工作放在extensionpoint后面，顺序不能颠倒
		for (Extension e:this.extensions){
			String pname = e.getExtensionPointName();
			ExtensionPoint finder = pluginRegistry.getExtensionPointMap().get(pname);
			if (finder == null){
				errors.add(new PluginError(this.getName(),"can't find extension point for:"+e.getName() +" pointname="+pname));
				continue;
			}
			
			if(! ReflactKit.isTypeOf(e.getClazz(),finder.getExtensionClass())){
				errors.add(new PluginError(this.getName(),"The extension is not sub class of the point required. extClass="+e.getClazz()+" required="+finder.getExtensionClass()+" point="+pname));
			}
			
			if (e.getClass().equals(String.class)){
				if (e.getProperties().size()!=1){
					errors.add(new PluginError(this.getName(),"String type extension must has one property with the val."+e.getName() +" pointname="+pname));
				}
			}
		}
		//由于目前不采用配置文件，所以不用检查extension和point中的类的存在性了。
		return errors;
	}

	/**
	 * @param statError
	 */
	public void setStatus(int st) {
		this.status = st;
	}
	
	
	public Map<String,String> getConfigures(){
		return this.configreus;
	}
	

	public void onDestroy() {
	}
	
	/**
	 * 下面两个方法维护类Plugin包含类的缓存，启动过程可以使用
	 * @param pkgPath 
	 * @return
	 */
	public Set<Class> getContainedClasses(){
		
		if (PluginEnvirement.INSTANCE.getStateLevel()>PluginEnvirement.STAT_LEVEL_INITING)
			throw new RuntimeException("Can't call in working state");
		
		if (this.containedClasses==null){
			ResolverKit kit = new ResolverKit();
			kit.find(this.getClass().getPackage().getName(), (c)->true);
			this.containedClasses = kit.getClasses();
		}
		return this.containedClasses;
	}
	
	public Set<Class> filterContainedClassesByChecker(String pkgPath, IClassChecker checker) {
		AssertKit.assertTrue(StringKit.isNull(pkgPath) || pkgPath.startsWith("."));
		
		//初始化一下
		getContainedClasses();

		//计算prefix
		String prefix = this.getClass().getPackage().getName();
		if (StringKit.isNull(pkgPath))
			prefix = prefix + ".";
		else
			prefix = prefix + pkgPath + ".";
		
		//生成结果
		Set<Class> temp = new HashSet();
		for (Class c : this.containedClasses) {
			if (c.getName().startsWith(prefix)) {
				if (checker.check(c))
					temp.add(c);
			}
		}
		return temp;
	}
			
	public Set<Class> filterContainedClasses(String pkgPath, Class annoClazz) {
		return this.filterContainedClassesByChecker(pkgPath, c->annoClazz == null || c.getAnnotation(annoClazz) != null);
	}
	
	public void _cleanContainedClasses(){
		if (this.containedClasses==null) 
			return;
		this.containedClasses.clear();
		this.containedClasses = null;
	}

	public void afterPluginsContruct() {
	}
}
