package net.jplugin.core.das.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.common.kits.tuple.Tuple3;

class SqlTemplateHelperForMap {
	
	/**
	 * <PRE>
	 * 处理sql：
	 * 1.根据${XXX} 提取参数，转化为绑定列表
	 * 2.对于【】内的部分，根据里面的参数是否是空值，决定是否保留
	 * 3.对于绑定类型为数组的情况，转化为多个？绑定 。这种情况一般来说是IN类型的操作。
	 * </PRE>
	 * @param sql
	 * @param param
	 * @param allowNullvalueForInsureItems
	 * @return
	 */
	public static Tuple2<String, Object[]> filterSqlWithMap(String sql, Map<String, Object> param ,boolean allowNullvalueForInsureItems) {
		try {
			return filterSqlWithMapInner(sql,param,allowNullvalueForInsureItems);
		}catch(Exception e) {
			throw new DataException("Sql error:"+sql+"  param:"+StringKit.mapToString(param),e);
		}
	}

		
	private static Tuple2<String, Object[]> filterSqlWithMapInner(String sql, Map<String, Object> param ,boolean allowNullvalueForInsureItems) {
		/**
		 * 根据中括号拆分成Item列表
		 */
		List<Item> results = getItemsBySquareBrackets(sql);
		
		if (printItems) {
			System.out.println("=========after getItemsBySquareBrackets===== ");
			System.out.println( getString(results));
		}
		
		/**
		 * 循环处理每一个Item，把sql中的 参数替换成？ ，并提取出参数列表
		 */
		ParseContext ctx = new ParseContext();
		for (Item item:results) {
			parsePara(item.string ,param ,ctx );
			
			//把sql片段换成 带 ？的片段，并设置参数列表
			item.string = ctx.buffer.toString();
			item.bindings = ctx.bindList;
			ctx.clear();
		}
		
		if (printItems) {
			System.out.println("=========after generate bindings===== ");
			System.out.println( getString(results));
		}
		
		/**
		 * 处理null值，为了在处理过程中能删除，倒序遍历。
		 */
		for (int i=results.size()-1;i>=0;i--) {
			Item item = results.get(i);
			if (item.sure) {
				//如果allowNullvalueForInsureItems 为false，则检查不能有null的绑定
				if (!allowNullvalueForInsureItems && item.bindings!=null && hasNullInList(item.bindings)) {
					throw new DataException("found null value for bounding. "+item.string);
				}
			}else {
				//去除有空值绑定的item
				if (item.bindings!=null) {
					if (hasNullInList(item.bindings)) {
						results.remove(i);
						continue;
					}
				}else{
//					throw new DataException("Insure segments must have at least one binding. sql:"+sql);
				}
			}
		}
		if (results.isEmpty()) {
			throw new DataException("No segment for sql:"+sql);
		}
		
		if (printItems) {
			System.out.println("=========after handle null value===== ");
			System.out.println( getString(results));
		}
		
		/**
		 * 组装返回
		 */
		StringBuffer finalResult = new StringBuffer();
		List finalBindings = new ArrayList();
		for (Item item:results) {
			finalResult.append(item.string).append(" ");
			if (item.bindings!=null) {
				finalBindings.addAll(item.bindings);
			}
		}
		
		return Tuple2.with(finalResult.toString(),finalBindings.toArray());
			
	}

	static class Item{
		String string;
		boolean sure; //確定
		List<Object> bindings;
		
		static Item with(String s,boolean b) {
			Item o = new Item();
			o.string = s;
			o.sure = b;
			return o;
		}
	}
	

	private static boolean hasNullInList(List<Object> list) {
		boolean hasNull = false;
		for (Object p:list) {
			if (p==null) {
				hasNull = true;
			}
		}
		return hasNull;
	}
	
	/**
	 * 根据中括号拆分成Item列表
	 * @param sql
	 * @return
	 */
	private static List<Item> getItemsBySquareBrackets(String sql) {
		List<Item> results= new ArrayList();
		computeSpans(sql,results);
		return  results;
	}

	static class ParseContext{
		StringBuffer buffer = new StringBuffer();
		List<Object> bindList = null; //解析出来的绑定列表
		
		void clear() {
			buffer = new StringBuffer();
			bindList = null;
		}
	}
	
	private static void parsePara(String sql, Map<String, Object> param, ParseContext resultCtx) {

		StringBuffer finalSql = new StringBuffer();
		
		int pos = sql.indexOf("#{");
		if (pos<0) {
			resultCtx.buffer.append(sql);
			return;
		}
		
		if (resultCtx.bindList==null) {
			resultCtx.bindList = new ArrayList();
		}
		
		String prev = sql.substring(0,pos);
		String post = sql.substring(pos+2);

		//增加前面一段
		resultCtx.buffer.append(prev);
		
		/*
		 * 下面拆分后面一段，并处理
		 * 
		 */
		
		//拆分後面一段
		sql = post;
		pos = sql.indexOf("}");
		if (pos<0) throw new DataException("sql format err");
		
		prev = sql.substring(0,pos).trim();
		post = sql.substring(pos+1);
		if (StringKit.isNull(prev))
			throw new DataException("sql error");
		else {
			Object bindValue = param.get(prev);
			//注意可能是null，Object或者数组
			
			appendBindings(resultCtx,bindValue);
		}
		//递归处理后续字符串
		parsePara(post,param, resultCtx);
	}

	/**
	 * 如果碰到List,则增加多个？，否则增加一个
	 * @param ctx
	 * @param bindValue
	 */
	private static void appendBindings(ParseContext ctx, Object bindValue) {
		if (bindValue==null) {
			ctx.buffer.append(" ? ");
			ctx.bindList.add(null);
		}else if (bindValue instanceof List) {
			List list = (List) bindValue;
			for (int i=0;i<list.size();i++ ) {
				if (i==0)
					ctx.buffer.append(" ? ");
				else
					ctx.buffer.append(" , ? ");
			}
			
			ctx.bindList.addAll(list);
		}else if (bindValue.getClass().isArray()) {
			
			for (int i=0;i<Array.getLength(bindValue);i++) {
				if (i==0)
					ctx.buffer.append(" ? ");
				else
					ctx.buffer.append(" , ? ");
				
				ctx.bindList.add(Array.get(bindValue, i));
			}
		}else {
			ctx.buffer.append(" ? ");
			ctx.bindList.add(bindValue);
		}
	}

	/**
	 * 未来可以用解析器来做，目前算法很简单。
	 * 1.先找【，拆分字符串
	 * 2.前面的部分为必选
	 * 2.后面的部分找】分拆，前面的部分为必选，后面的部分递归调用处理
	 * @param sql
	 * @return
	 */
	private static void computeSpans(String sql , List<Item> results) {
		int pos = sql.indexOf('[');
		if (pos<0) {
			//整个必选
			results.add(Item.with(sql,true));//true 表示必选
		}else {
			String prev = sql.substring(0,pos);
			String post = sql.substring(pos+1);
			
			//前面一段必选
			if (!StringKit.isNull(prev)) {
				results.add(Item.with(prev,true));
			}
			//后面一段用]继续拆分,前面一段为可选。后面一段参加递归调用
			sql = post;
			pos = sql.indexOf(']');
			if (pos<0)
				throw new DataException("[] not cupuled");
			
			prev = sql.substring(0,pos);
			post = sql.substring(pos+1);
						
			if (StringKit.isNull(prev)) {
				//这里存在一个[],没意义
			}else {
				//必选
				results.add(Item.with(prev,false));
			}
			if (StringKit.isNull(post)) {
				//]为结束,完了
			}else {
				//继续递归
				computeSpans(post,results);
			}
		}
	}

	static boolean printItems=false;
	
	public static void main(String[] args) {
		printItems = true;
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("pa", "av");
		map.put("pb", "bv");
		map.put("arr", new Object[] {"i1","i2","i3"});
		
		test("select * from ta where  pa= #{pa}",map,true);
		AssertKit.assertException(()->{test("select * from ta where  pa= #{pc}",map,false);});
		AssertKit.assertException(()->{test("select * from ta where 1=1 [and  pa=#{pa}] and  pa= #{pc}",map,false);});
		
		test("select * from ta where  pa= #{pc}",map,true);
		
		test("select * from ta where  pa= #{pa }",map,false);
		
		AssertKit.assertException(()->{test("select * from ta where  pa= #{ }",map,true);});
		
		Tuple2<String, Object[]> r = test("select * from ta where  pa in #{arr }",map,false);
		AssertKit.assertEqual(3, r.second.length);
		
	
		test("select * from ta where  pa= #{pa} [and pb=#{pb} and]pc=pc",map,true);
		
		test("select * from ta where  pa= #{pa} [A] pc=pc",map,true);
		
		
//		test("select * from ta where  1=1 [and pa= #{pa}] [and pc= #{pc}] group by aaa",map);
		
//		test("aaa [ neg] aaa [neg]sss[neg][][]" ,map);
//		test("",map);
//		test("[]",map);
//		
//		test("[neg]aaa]",map);
		
		
	}
	


	private static Tuple2<String, Object[]> test(String sql,Map<String,Object> map,boolean b) {
		System.out.println("now to handle sql:"+sql);
		Tuple2<String, Object[]> result = filterSqlWithMap(sql,map,b);
		System.out.println("\n\nSQL = " +result.first);
		System.out.println("bindings = "+StringKit.arrToString(result.second));
		return result;
	}

	private static String getString(List<Item> results) {
		StringBuffer sb = new StringBuffer();
		for (Item item:results) {
			sb.append("\n").append(item.sure).append("  ").append(item.string).append("  bindings=");
			if (item.bindings==null) {
				sb.append("null");
			}else {
				sb.append(StringKit.list2String(item.bindings));
			}
		}
		return sb.toString();
	}
	
}
