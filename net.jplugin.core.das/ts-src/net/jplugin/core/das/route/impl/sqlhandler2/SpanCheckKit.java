package net.jplugin.core.das.route.impl.sqlhandler2;

import net.jplugin.core.das.route.impl.parser.SqlWordsWalker;

public class SpanCheckKit {

	/**
	 * 16-11-18修改为从全串检索，不能只看第一个select后面。因为select可能会被框架自动添加select count(*) from (XXXX)
	 * @param walker
	 * @return
	 */
	public static boolean isSpanTable(String sql) {
		int pos1 = sql.indexOf("/*");
		if (pos1<0) return false;
		
		int pos2 = sql.indexOf("*/", pos1+2);
		if (pos2<0) return false;
		
		String str = sql.substring(pos1+2,pos2).trim();
		return "spantable".equalsIgnoreCase(str);
//		
//		for (int i=0;i<walker.size();i++){
//			if (walker.wordAt(i).startsWith("/*")){
//				String tmp = walker.wordAt(i);
//				if (tmp.endsWith("*/") && tmp.length()>5 && tmp.substring(2, tmp.length()-2).trim().equalsIgnoreCase("spantable"))
//					return true;
//			}
//		}
//		return false;
	}
}
