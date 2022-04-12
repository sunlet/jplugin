package test.net.jplugin.core.das.route;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.kits.SqlWordsWalker;

public class SqlWordsWalkerTest {
	
	public static void main(String[] args) {
		String s = "select * from a where a!=b";
		SqlWordsWalker w = SqlWordsWalker.createFromSql(s);
		System.out.println(w.toSql());
		AssertKit.assertEqual(w.toSql(), "select * from a where a != b");
		
	}


}
