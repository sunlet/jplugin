package test.net.jplugin.core.mtenant.mdbsqlhelper;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.mtenant.handler.MultiDbSqlHelper;

public class MultidbSqlHelperTest {

	public void test(){
		String source,target;
		
		source = "select * from abc";
		target = "select * from sss.abc";
		check(source,target);

		source = "select * from abc;select * from xyz";
		target = "select * from sss.abc ; select * from sss.xyz";
		check(source,target);

	}

	private void check(String source, String target) {
		
		String t = MultiDbSqlHelper.handle(source, "sss");
		AssertKit.assertEqual(t,target);
	}
}
