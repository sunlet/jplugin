package test.net.jplugin.ext.token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.ext.token.api.ITokenService;

public class TokenTest {

	public void test() {
		ITokenService s = RuleServiceFactory.getRuleService(ITokenService.class);
		Map<String,String> map =new HashMap<String, String>();
		map.put("userid", "tiger");
		map.put("username", "zhangsan");
		String token = s.createToken(map, "tiger", "mobile");
		
		map = s.validAndGetTokenInfo(token);
		AssertKit.assertEqual(map.get("userid"), "tiger");
		AssertKit.assertNull(s.validAndGetTokenInfo("asdf@123"));

		map = new HashMap<String, String>();
		map.put("3", "3");
		map.put("username", "zhangsan1");
		s.putTokenInfo(token, map);
		map = s.validAndGetTokenInfo(token);
		AssertKit.assertEqual(map.size(), 3);
		AssertKit.assertEqual(map.get("username"), "zhangsan1");
		
		HashSet<String> hs = new HashSet<String>();
		hs.add("username");
		s.removeTokenInfo(token, hs);
		map = s.validAndGetTokenInfo(token);
		AssertKit.assertEqual( 2,map.size());
		
		String token2 = s.createToken(map, "tiger", "mobile");
		
		AssertKit.assertNull(s.validAndGetTokenInfo(token));
	}

}
