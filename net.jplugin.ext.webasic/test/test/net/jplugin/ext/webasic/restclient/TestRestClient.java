package test.net.jplugin.ext.webasic.restclient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.core.rclient.proxyfac.ClientProxyFactory;
import test.net.jplugin.ext.webasic.restclient.ServiceBean.Bean;

public class TestRestClient {
	
	public void test() {
		Client<IService> client = ClientFactory.getThreadLocalClient(IService.class);
		client.setServiceBaseUrl("http://localhost:8080/demo/testrestclient");
		client.setProtocal(Client.PROTOCOL_REST);
		IService service = client.getObject();
		
		AssertKit.assertEqual(3,service.add(1, 2));
		AssertKit.assertEqual("12", service.addString("1", "2"));
		
		List<Bean> list = service.getBeanList();
		AssertKit.assertEqual(2, list.size());
		AssertKit.assertEqual("lisi", list.get(1).name);

		Map<String,Bean> map = service.getBeanMap();
		AssertKit.assertEqual(2, map.size());
		AssertKit.assertEqual("lisi", map.get("lisi").name);
		
		//²âÊÔÅ×³öÆÕÍ¨exception
		boolean ret=false;
		try{
			service.ex();
		}catch(RemoteExecuteException e){
			AssertKit.assertEqual(e.getCode(), "-1");
			ret = true;
		}
		if (!ret) throw new RuntimeException("fail");
	}
	
	
	
	public void testProxyFactory() {
		IService service = ClientProxyFactory.instance.getClientProxy(IService.class);
		AssertKit.assertEqual(3,service.add(1, 2));
		AssertKit.assertEqual("12", service.addString("1", "2"));
		
		
		//²âÊÔÅ×³öretmoteexception
		boolean ret=false;
		try{
			service.remoteEx();
		}catch(RemoteExecuteException e){
			AssertKit.assertEqual(e.getCode(), "100");
			ret = true;
		}
		if (!ret) throw new RuntimeException("fail");
		
		//²âÊÔÅ×³öindirect exception
		ret=false;
		try{
			service.indirectEx();
		}catch(RemoteExecuteException e){
			AssertKit.assertEqual(e.getCode(), "-100");
			AssertKit.assertEqual(e.getMessage(), "indirectmsg");
			ret = true;
		}
		if (!ret) throw new RuntimeException("fail");
	}
}
