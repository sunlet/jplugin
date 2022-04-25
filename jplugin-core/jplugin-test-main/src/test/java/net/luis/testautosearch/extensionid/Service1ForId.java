package net.luis.testautosearch.extensionid;

import net.jplugin.core.kernel.api.SetExtensionId;
import net.jplugin.core.service.api.BindService;

//@BindService(id = "IService1ForId")

@SetExtensionId("IService1ForId")
@BindService()

public class Service1ForId implements IService1ForId{

	public void a() {
		
	}
}
