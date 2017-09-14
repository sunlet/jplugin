package net.jplugin.core.ctx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.reso.ResolverKit;
import net.jplugin.core.ctx.api.BindRuleService;
import net.jplugin.core.ctx.api.BindRuleService.DefaultInterface;
import net.jplugin.core.ctx.api.RuleServiceDefinition;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午10:22:41
 **/

public class ExtensionCtxHelper {
	public static void addRuleExtension(AbstractPlugin plugin,String name,Class intf,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, name,RuleServiceDefinition.class,new String[][]{{"interf",intf.getName()},{"impl",impl.getName()}} ));
	}
	
	public static void addRuleExtension(AbstractPlugin plugin,Class intf,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, intf.getName(),RuleServiceDefinition.class,new String[][]{{"interf",intf.getName()},{"impl",impl.getName()}} ));
	}
	
	public static void addTxMgrListenerExtension(AbstractPlugin plugin,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_TXMGR_LISTENER, impl ));
	}

	public static void addRuleServiceFilterExtension(AbstractPlugin plugin,Class impl){
		plugin.addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE_FILTER, impl ));
	}

	/**
	 * 自动遍历pkgPath子包下面的所有类，找到BindRuleService 标注，自动注册为RuleService扩展。
	 * @param p
	 * @param pkgPath
	 */
	public static void autoBindRuleServiceExtension(AbstractPlugin p, String pkgPath) {
		for(Class c:p.filterContainedClasses(pkgPath,BindRuleService.class)){
			BindRuleService anno = (BindRuleService) c.getAnnotation(BindRuleService.class);
			Class interfaceClazz = anno.interfaceClass();
			if (interfaceClazz.getName().equals(DefaultInterface.class.getName())){
				interfaceClazz = computeInterfaceCls(c);
			}
			if (StringKit.isNull(anno.name())){
				addRuleExtension(p, interfaceClazz, c);
				PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for rule service: interface="
						+ interfaceClazz.getName() + " impl=" + c.getName());
			}else{
				addRuleExtension(p, anno.name(),interfaceClazz, c);
				PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for rule service: interface="
						+ interfaceClazz.getName() + " impl=" + c.getName());
			}
		}
	}

	private static Class computeInterfaceCls(Class impClazz) {
		Class[] clazzs = impClazz.getInterfaces();
		if (clazzs.length==0){
			throw new RuntimeException("Class must implement a interface, so as to be defined as a RuleService impl. "+impClazz.getName());
		}
		if (clazzs.length>1){
			throw new RuntimeException("Class with multiple interfaces, must specify one interface in annotation. "+impClazz.getName());
		}
		return clazzs[0];
	}

	/**
	 * <PRE>
	 * 此方法自动遍历指定包下面的类，发布为RuleService。
	 * 对于 “Plugin类所属包的apiPkgPath子包” 下面接口(Intf1.class)，如果在“Plugin类所属包的implPkgPath子包”下找到了唯一一个实现类(Impl1.class)。
	 * 则自动注册为RuleService. 相当于在Plugin构造函数当中调用代码：
	 * 		ExtensionCtxHelper.addRuleExtension(this,Intf1.class.getName(),Impl1.class.getName());
	 * 
	 * 请注意：如果对于Intf1.class接口，在给定实现包路径下找不到实现类，或者找到多个实现类，都不会自动注册。
	 * 可以在控制台或者系统启动日志中搜索“$$$ Auto add extension”查看相关自动注册情况。 
	 * 
	 * </PRE>
	 * @param p   对应的Plugin类
	 * @param apiPkgPath  接口类相对于Plugin类的相对包路径。
	 * @param implPkgPath  实现类相对于Plugin类的相对包路径。
	 */
	@Deprecated
	public static void autoAddRuleServiceExtension(AbstractPlugin p, String apiPkgPath, String implPkgPath) {
		String pkg = p.getClass().getPackage().getName() + apiPkgPath;
		ResolverKit kit = new ResolverKit<>();
		kit.find(pkg, (c) -> {
			return c.isInterface();
		});
		Set apiClazzes = kit.getClasses();

		HashMap<Class, List<Class>> mappings = new HashMap();
		for (Object o : apiClazzes) {
			mappings.put((Class) o, new ArrayList<Class>());
		}

		pkg = p.getClass().getPackage().getName() + implPkgPath;
		kit = new ResolverKit<>();
		kit.find(pkg, (c) -> {
			for (Entry<Class, List<Class>> en : mappings.entrySet()) {
				if (en.getKey().isAssignableFrom(c)){
					if (!c.isInterface())
						en.getValue().add(c);
				}
			}
			return false;
		});

		for (Entry<Class, List<Class>> en : mappings.entrySet()) {
			if (en.getValue().size() == 1) {
				ExtensionCtxHelper.addRuleExtension(p, en.getKey(), en.getValue().get(0));
				PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto add extension for rule service: interface="
						+ en.getKey().getName() + " impl=" + en.getValue().get(0).getName());
			} else {
				if (en.getValue().size() == 0) {
					// PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Auto
					// Add Rule Extension Success , Can't find impl for
					// interface:"+en.getKey().getName());
				} else {
					PluginEnvirement.INSTANCE.getStartLogger()
							.log("$$$ Auto add extension for rule service failed , Multiple impl found for interface "
									+ en.getKey().getName());
				}
			}
		}
	}
}
