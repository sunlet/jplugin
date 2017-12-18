package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.route.api.KeyValueForAlgm.Operator;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2.KeyFilter;
import net.jplugin.core.das.route.impl.sqlhandler2.AbstractCommandHandler2.Value;
import net.sf.jsqlparser.expression.Expression;

public class VisitorExpressionManager {
	public static List<KeyFilter> getKeyFilterList(Expression where,String fieldName){
		VisitorForAndExpression andVisitor = new VisitorForAndExpression(fieldName);
		where.accept(andVisitor);
		
		//如果不需要继续找交集列表，则直接返回
		KeyFilter knownFilter = andVisitor.getKnownFilter();
		if (knownFilter!=null) {
			ArrayList list = new ArrayList<>(1);
			list.add(knownFilter);
			return list;
		}
		
		//提前返回null
		List<Expression> intersectExpList = andVisitor.getOtherIntersectExpressions();
		if (intersectExpList==null || intersectExpList.isEmpty())
			return null;
		
		//获取需要先并后交的大列表套装
		List<List<KeyFilter>> resultsToInsect = new ArrayList(intersectExpList.size());
		VisitorForOrExpression orVisitor = new VisitorForOrExpression();
		for (Expression exp:intersectExpList){
			orVisitor.clear();
			List<KeyFilter> result = getResultToIntersect(andVisitor, orVisitor, exp);
			resultsToInsect.add(result);
		}
		
		//每一个并集做技术型合并
		List<KeyFilter> resultFilters=new ArrayList<>(resultsToInsect.size());
		for (List<KeyFilter> list:resultsToInsect){
			KeyFilter filter = getUnionFilter(list);
			if (filter!=null)
				resultFilters.add(filter);
		}

		//返回
		if (resultFilters.size() == 0)
			return null;
		else
			return resultFilters;
	}

	/**
	 * 因为or节点一定有不少下级节点的，对下级节点分析时，得到null仍然加入list。所以这个返回的list不会为空。但会包含多个null值。
	 * @param andVisitor
	 * @param orVisitor
	 * @param exp
	 * @return
	 */
	private static List<KeyFilter> getResultToIntersect(VisitorForAndExpression andVisitor,
			VisitorForOrExpression orVisitor, Expression exp) {
		orVisitor.travelUnionExpressions(exp);
		List<Expression> unionExpressions = orVisitor.get();
		//直接再查询一次
		List<KeyFilter> result = new ArrayList(unionExpressions.size());
		for (Expression uExp:unionExpressions){
			andVisitor.clear();
			uExp.accept(andVisitor);
			//注意，这里，即使获取到null仍然要加入到队列，不能过滤掉的，否则范围就变小了！
			result.add(andVisitor.getKnownFilter());
		}
		//
		return result;
	}

	/**
	 * 只处理 in ,eq类型的合并
	 * @param list
	 * @return
	 */
	private static KeyFilter getUnionFilter(List<KeyFilter> list) {
		AssertKit.assertTrue(list.size()>1);
		List<Value> values = new ArrayList<>();
		for (KeyFilter kf:list){
			/*注意：只要存在空的就尽早返回,所以空不能在前面过滤掉*/
			if (kf==null || kf.operator==Operator.ALL/*貌似不太可能出现这个*/ || kf.operator == Operator.BETWEEN) 
				return null;
			if (kf.operator==Operator.EQUAL || kf.operator==Operator.IN){
				for (Value v:kf.value){
					values.add(v);
				}
			}
		}
		return new KeyFilter(Operator.IN, values.toArray(new Value[values.size()]));
	}
}
