package net.luis.testfactory;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.IExtensionFactory;
import net.jplugin.core.kernel.api.IStartup;

@BindExtension(pointTo = "mypoint78")
public class MyExtensionFactory implements IExtensionFactory {

	@Override
	public Object create() {
		return new AAA();
	}

	@Override
	public Class getAccessClass() {
		return AAA.class;
	}

	@Override
	public boolean contentEqual(IExtensionFactory f) {
		return true;
	}

	public static class AAA implements IMyInterface{

		@Override
		public String a() {
			return "thereturn";
		}
		
	}

	
}
