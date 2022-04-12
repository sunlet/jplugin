package test.net.jplugin.core.ctx.bindextension;

import net.jplugin.core.kernel.api.BindExtension;

@BindExtension(pointTo="testExtensionPoint",name="abc")
public class Service1Impl2 implements IService1{

	@Override
	public void greet() {
		System.out.println("haha "+this.getClass().getName());
		
	}
	
}
