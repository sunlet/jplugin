package net.jplugin.core.kernel.api;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 下午01:39:45
 **/

public abstract class AbstractPluginForTest extends AbstractPlugin{
	public final void init() {
		try{
			test();
			System.out.println("Plugin:"+this.getClass().getName()+" 测试成功!");
		}catch(Throwable e){
			System.out.println("Plugin:"+this.getClass().getName()+" 测试失败!");
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * 
	 */
	public abstract void test() throws Throwable;
	

}
