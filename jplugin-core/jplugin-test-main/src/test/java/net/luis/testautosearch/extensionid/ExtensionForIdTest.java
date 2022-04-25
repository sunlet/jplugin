package net.luis.testautosearch.extensionid;

import net.jplugin.core.kernel.api.BindExtension;
import net.jplugin.core.kernel.api.SetExtensionId;

//@BindExtension(pointTo = "EL_ExtensionForIdTest", id="ExtensionForIdTest")

@SetExtensionId("ExtensionForIdTest")
@BindExtension(pointTo = "EL_ExtensionForIdTest")
public class ExtensionForIdTest implements IExtensionForIdTest{

	@Override
	public void aaa() {
		// TODO Auto-generated method stub
		
	}

}
