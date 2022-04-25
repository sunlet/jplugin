package net.luis.testautosearch.extensionid;

import java.util.List;

import net.jplugin.core.kernel.api.BindStartup;
import net.jplugin.core.kernel.api.IStartup;
import net.jplugin.core.kernel.api.PluginError;
import net.jplugin.core.kernel.api.SetExtensionId;

@SetExtensionId("BindStartUpForId")
@BindStartup()
public class BindStartUpForId implements IStartup{

	@Override
	public void startFailed(Throwable th, List<PluginError> errors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSuccess() {
		// TODO Auto-generated method stub
		
	}

}
