package net.jplugin.core.app;

import net.jplugin.core.ctx.ExtensionCtxHelper;
import net.jplugin.core.das.mybatis.api.ExtensionMybatisDasHelper;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.ext.webasic.ExtensionWebHelper;

/**
 * <PRE>
 * 这是一个结构化的注册扩展的Plugin基类，也就是说：如果插件程序结构符合规定的目录标准，集成自该类的插件会自动注册常用的扩展。
 * 具体来说，会自动注册
 *      export 包下的 服务。
 *      svc.api  svc.impl 包下的RuleService。 (接口在svc.api下，实现类在 svc.impl下面）
 *      controller包下的Web控制器。
 *      mapper包下的MybatisMapping。
 *      
 * 用户可以在控制台或者系统启动日志中搜索“$$$ Auto add extension”查看相关自动注册情况。 
 * 
 * </PRE>
 * @author LiuHang
 *
 */
public abstract class AbstractStruturedPlugin extends AbstractPlugin{
	public AbstractStruturedPlugin(){
		ExtensionWebHelper.autoAddServiceExportExtension(this,".export");
		ExtensionWebHelper.autoAddWebControllerExtension(this, ".controller");
		ExtensionCtxHelper.autoAddRuleServiceExtension(this, ".svc.api", ".svc.impl");
		ExtensionMybatisDasHelper.autoAddMappingExtension(this, "database", ".mapper");
	}
}
