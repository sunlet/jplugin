package jplugincoretest.net.jplugin.ext.webasic.restclient;

import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.api.RemoteExecuteException;
import net.jplugin.core.rclient.proxyfac.ClientProxyFactory;

public class TestRestClient {
	
	public void test() {
		Client<IService> client = ClientFactory.getThreadLocalClient(IService.class);
		client.setServiceBaseUrl("http://localhost:8080/demo/testrestclient");
		client.setProtocal(Client.PROTOCOL_REST);
		IService service = client.getObject();
		
		AssertKit.assertEqual(3,service.add(1, 2));
		AssertKit.assertEqual("12", service.addString("1", "2"));
		
		List<ServiceBean.Bean> list = service.getBeanList();
		AssertKit.assertEqual(2, list.size());
		AssertKit.assertEqual("lisi", list.get(1).name);

		Map<String, ServiceBean.Bean> map = service.getBeanMap();
		AssertKit.assertEqual(2, map.size());
		AssertKit.assertEqual("lisi", map.get("lisi").name);
		
		//测试抛出普通exception
		boolean ret=false;
		try{
			service.ex();
		}catch(RemoteExecuteException e){
			AssertKit.assertEqual(e.getCode(), "-1");
			ret = true;
		}
		if (!ret) throw new RuntimeException("fail");
		
		int r = service.testFullMatch(1);
		AssertKit.assertEqual(2, r);
	}
	
	
	
	public void testProxyFactory() {
		IService service = ClientProxyFactory.instance.getClientProxy(IService.class);
		AssertKit.assertEqual(3,service.add(1, 2));
		AssertKit.assertEqual("12", service.addString("1", "2"));
		
		
		//测试抛出retmoteexception
		boolean ret=false;
		try{
			service.remoteEx();
		}catch(RemoteExecuteException e){
			AssertKit.assertEqual(e.getCode(), "100");
			ret = true;
		}
		if (!ret) throw new RuntimeException("fail");
		
		//测试抛出indirect exception
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
