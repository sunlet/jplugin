package test.net.jplugin.ext.webasic.restclient;

import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.rclient.api.Client;
import net.jplugin.core.rclient.api.ClientFactory;
import net.jplugin.core.rclient.proxyfac.ClientProxyFactory;
import test.net.jplugin.ext.webasic.restclient.RestServiceBean.Bean;

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
		
	}
	
	public void testProxyFactory() {
		IService service = ClientProxyFactory.instance.getClientProxy(IService.class);
		AssertKit.assertEqual(3,service.add(1, 2));
		AssertKit.assertEqual("12", service.addString("1", "2"));
	}
}
