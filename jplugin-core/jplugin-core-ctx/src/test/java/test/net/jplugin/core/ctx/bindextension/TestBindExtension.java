package test.net.jplugin.core.ctx.bindextension;

import java.util.List;

import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.kernel.api.RefExtensions;

public class TestBindExtension extends RefAnnotationSupport{

	@RefExtensions(pointTo="testExtensionPoint")
	List<IService1> list;
	public void test(){
		
		for (IService1 s:list){
			s.greet();
		}
	}
}
