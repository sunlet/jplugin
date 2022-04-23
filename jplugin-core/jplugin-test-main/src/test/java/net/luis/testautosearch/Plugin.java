package net.luis.testautosearch;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.Beans;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;
import net.jplugin.core.kernel.api.PluginError;
import net.jplugin.core.kernel.api.PluginRegistry;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import net.luis.testautosearch.extensionid.BeanTestTest;
import net.luis.testautosearch.extensionid.ExportTest1;
import net.luis.testautosearch.extensionid.ExportTest2;
import net.luis.testautosearch.extensionid.ExtensionIdTest;
import net.luis.testautosearch.extensionid.IExtensionForIdTest;

@PluginAnnotation
public class Plugin extends AbstractPlugin {

	public Plugin() {
		System.out.println("constructing.....");
		ExtensionWebHelper.addServiceExportExtension(this,"/path1",ExportTest1.class);
		Extension.setLastExtensionId("theidabcde");

		
		this.addExtensionPoint(ExtensionPoint.create("EL_ExtensionForIdTest",IExtensionForIdTest.class));
	}
	@Override
	public void init() {
		ExtensionIdTest.test();
		new  BeanTestTest().test();
	}
	
	@Override
	public void onCreateServices() {
		ExtensionIdTest.assertExceptionInCreateService();
	}

	@Override
	public int getPrivority() {
		return 0;
	}

	@Override
	public void wire(PluginRegistry pluginRegistry, List<PluginError> errorList) {
		System.out.println("wiring....");
		super.wire(pluginRegistry, errorList);
	}
}
