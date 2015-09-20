package net.jplugin.ext.filesvr.web;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.ext.webasic.api.AbstractExController;

public class FileServlet extends AbstractExController {
	public void requestFileIds(){
		String clientFilePaths = getStringAttr("clientFilePaths");
		AssertKit.assertNotNull(clientFilePaths, "client file path");
		List<String> pathlist = JsonKit.json2ListBean(clientFilePaths, String.class);
		
		
	}
}
