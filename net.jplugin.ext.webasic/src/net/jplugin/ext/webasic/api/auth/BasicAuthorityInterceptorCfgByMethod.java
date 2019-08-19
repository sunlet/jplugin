package net.jplugin.ext.webasic.api.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.kernel.api.Initializable;

/**
 * <pre>
 * 因方案调整，这个类目前没使用！
 * 
 * 这个类是用以权限校验的基类，可以继承该类实现 SSO，User，Tenant,APP等验证拦截器抽象类。
 * 
 * 用法：
 *   1.平台继承这个类实现具体的验证抽象类，比如 SSOAuthorityInterceptor,此时需要实现validate方法。
 *   2.应用中继承SSOAuthorityInterceptor实现自己的验证类，需要实现initPermissionSetting方法。
 *     如果只需要token验证，则initPermissionSetting方法的实现为空方法即可。
 *     配置自己验证类的注解，指定哪些类和哪些方法需要验证。
 * 
 * 验证规则：
 * 1.Token验证：所有经过类拦截的都要进行Token验证，所以不需要token验证的，请不要配置对应的拦截器的注解。
 * 2.权限验证：如果需要进一步的权限验证（比如验证角色或者功能权限），
 * 		则需要在initPermissionSetting当中调用addPermissionRule来增加规则，
 * 		第一个参数为实现类名字，第二个参数为method的过滤器规则可以支持通配符等，第三个参数以后表示权限名称。
 * 3.initPermissionSetting中的权限设定按照先设定先有效规则！比如：
 * 		initPermissionSetting(classA, "*","PERM1")
 * 		initPermissionSetting(classA,"methodA","PERM2")
 * 		上面的写法调用classA的methodA方法，验证的权限是PERM1，因为这条匹配的在前面。
 * 
 * </pre>
 * @author LiuHang
 *
 */
public abstract class BasicAuthorityInterceptorCfgByMethod extends BasicAuthorityInterceptor implements Initializable{
	private ArrayList<ConfigItem> configItemList = new ArrayList<>();
	/**
	 * 这个方法在初始化被调用，用以制定哪些方法需要哪些权限
	 */
	public abstract void initPermissionSetting();


	@Override
	public void initialize() {
		initPermissionSetting();
		super.setPermissionMap(parseConfig());
	}
	
	public void reInitialize(){
		this.configItemList.clear();
		initPermissionSetting();
		//假定赋值原子性
		super.setPermissionMap(parseConfig());
	}
	
	protected BasicAuthorityInterceptorCfgByMethod addPermissionRule(Class clazz, String methodFilter, String... perms) {
		if (clazz == null)
			throw new AuthorityException("Class is null");
		if (methodFilter == null)
			throw new AuthorityException("Method filter is null");
		if (perms == null || perms.length == 0) {
			throw new AuthorityException("perm param is null. You should use  validPerm(clazz,method,perm1,perm2...)");
		}
		this.configItemList.add(new ConfigItem(clazz, methodFilter, perms));
		return this;
	}


	Map<Class, Map<String, String[]>> parseConfig() {
		Map<Class, Map<String, String[]>> tempMap=new HashMap<>();
		
		// 找出来涉及到的全部class
		Set<Class> classSet = new HashSet<>();
		for (ConfigItem item : configItemList) {
			classSet.add(item.clazz);
		}
		// 遍历这些class
		for (Class c : classSet) {
			Map<String, String[]> map = new HashMap();
			Set<String> methods = ReflactKit.getMethodNamesExceptObject(c);
			for (String mn : methods) {
				// 命中了，才放进map。map当中的肯定是需要校验的，否则不放进去！
				ConfigItem targetConfig = getMaching(c, mn);
				if (targetConfig != null) {
					map.put(mn, targetConfig.perms);
				}
			}
			tempMap.put(c,map);
		}
		return tempMap;
	}

	private ConfigItem getMaching(Class c, String mn) {
		// 规则是：先命中优先！！！！
		for (ConfigItem ci : configItemList) {
			if (ci.clazz == c && ci.macher.match(mn))
				return ci;
		}
		return null;
	}

	private static class ConfigItem {
		Class clazz;
		String methodFilter;
		String[] perms;
		StringMatcher macher;// 通过methodFilter计算的

		public ConfigItem(Class c, String mf, String[] p) {
			this.clazz = c;
			this.methodFilter = mf;
			this.perms = p;
			this.macher = new StringMatcher(methodFilter);
		}
	}
}
