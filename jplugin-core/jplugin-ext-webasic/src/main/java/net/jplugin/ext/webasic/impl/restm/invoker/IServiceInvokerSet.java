package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.Map;
import java.util.Set;

import net.jplugin.ext.webasic.api.ObjectDefine;
/**
  * 
 * <P>该接口用户已文本形式调用一个已经发布的Rest服务，并得到返回结果。<br>
 * 可以按照如下方式获取该接口单例实例： ServiceInvokerSet.instance;
 * 调用的方法，调用call方法。 ServiceInvokerSet.instance.call( callParam)
 * <br>
 * 参数CallParam可以用如下方法构造 : CallParam.create(path,operation,map);
 * 其中,各参数如下：
 * 		<li>path:服务请求路径比如  /svc/cust</li>
 * 		<li>operation:服务请求的方法名，比如 addCustomer</li>
 * 		<li>map:传入的参数 ，分别对应到Java方法的每个参数，复杂类型采用JSON格式</li>
 * <br>
 * <br>
 * 返回值和目前restful服务返回结构相同，可以参照旧文档。
 *  
 * @author Luis LiuHang
* 
 *
 */
public interface IServiceInvokerSet {
	public Set<String>  getPathSet();
	public void call(CallParam cp)  throws Throwable;
	public Set<String> getAcceptPaths();
	//为了支持ESF添加
	public IServiceInvoker getServiceInvoker(String path);
	public void addServices(Map<String, Object> defs);
}
