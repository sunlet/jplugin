package net.luis.main;

import java.util.HashMap;

import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class TestCall {

	public static void test() {
		ThreadLocalContextManager.instance.createContext();
		try{
			ThreadLocalContextManager.instance.getCurrentContext().getRequesterInfo().setCurrentTenantId("t01");
			HashMap<String,Object> para = new HashMap<String,Object>();
			
			HttpKit.post("http://172.21.15.3:8101/call/test.do", para);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			ThreadLocalContextManager.instance.releaseContext();
		}
		
		
	}

}
