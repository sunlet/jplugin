package net.jplugin.core.ctx.impl;

import java.lang.reflect.Method;

import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.kernel.api.PluginEnvirement;

/**
 * <PRE>
 * 2019-5-28
 * 当从方法查找Rule标记，查找不到时候，从这里获取默认的Rule标记。
 * 用这种方法支持默认的Rule标记。
 * 是否启用这个默认的Rule标记，由配置文件来确定。
 * </PRE>
 * @author Administrator
 *
 */
public class DefaultRuleAnnoConfig {
	private DefaultRuleAnnoConfig() {
	}
	
	private static Rule defaultRuleAnnotation;
	private static boolean init = false;
	
	public static Rule findDefaultRuleAnnotation(){
		if (!init){
			synchronized (DefaultRuleAnnoConfig.class) {
				defaultRuleAnnotation = retrive();	
				PluginEnvirement.getInstance().getStartLogger().log("DefaultRuleAnnotation is "+defaultRuleAnnotation);
				init = true;
			}
		}
		return defaultRuleAnnotation;
	}

	@Rule
	public static void aMethod(){
		throw new RuntimeException("can't call");
	}
	
	private static Rule retrive() {
		if ("true".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("platform.use-default-rule-anno"))){
			Method m = ReflactKit.findSingeMethodExactly(DefaultRuleAnnoConfig.class, "aMethod");
			Rule rule = m.getAnnotation(Rule.class);
			if (rule==null)
				throw new RuntimeException("Can't find the rule anno");
			return rule;
		}else{
			return null;
		}
	}
}
