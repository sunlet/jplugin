package net.jplugin.core.kernel.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.tuple.Tuple2;

public class PluginRegistryHelper {

	/**
	 * <PRE>
	 * 相同级别推测最佳顺序。
	 * 
	 * 逐趟比较，每一趟找一个出来，规则：
	 * 1.相关插件相互之间被扩展关系，依赖其他插件个数A
	 * 2.插件扩展点的个数B
	 * 3.插件的扩展的个数为C
	 * 公式： -1000000A + 1000B +C. 越大优先级高。
	 * </PRE>
	 * @param pluginList
	 */
	public static void reorderSamePriorityPlugins(List<AbstractPlugin> list) {
		int pos=0;
		while(pos<list.size()-1) {
			if (list.get(pos).getPrivority() == list.get(pos+1).getPrivority()) {
				List<AbstractPlugin> temp = getSimplePriorityPlugins(list,pos);
				
				PluginEnvirement.getInstance().getStartLogger().log("Found simple priority plugins :"+getNames(temp));
				reOrder(temp);
				PluginEnvirement.getInstance().getStartLogger().log("After reorder :"+getNames(temp));
				putBack(list,temp,pos);
				
				//直接往后跳temp.size（）个
				pos += temp.size();
			}else {
				pos ++;
			}
		}
	}


	private static String getNames(List<AbstractPlugin> temp) {
		StringBuffer sb= new StringBuffer();
		for (AbstractPlugin item:temp) {
			sb.append(item.getName()).append(" , ");
		}
		sb.append("]");
		return sb.toString();
	}

	private static void putBack(List<AbstractPlugin> allList, List<AbstractPlugin> temp, int startPos) {
		for (int i=0;i<temp.size();i++) {
			allList.set(startPos+i, temp.get(i));
		}
	}
	
	private static class PluginScore{
		AbstractPlugin plugin;
		int score;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	private static void reOrder(List<AbstractPlugin> targetList) {
		List<ExtendRelation> extendRelations= getExtendRelations(targetList);
		
		List<AbstractPlugin> tempSortList = new ArrayList(targetList.size());
		tempSortList.addAll(targetList);
		targetList.clear();
		
		
		//每一遍找一个最大的,提取出来，提取完为止
		while(!tempSortList.isEmpty()) {
			List<PluginScore> pluginScoreList = getPluginScores(extendRelations, tempSortList);
	
			PluginScore maxPluginScore = null;
			for (PluginScore current:pluginScoreList) {
				if (maxPluginScore==null)
					maxPluginScore = current;
				else {
					if (current.score>maxPluginScore.score) {
						maxPluginScore = current;
					}
				}
			}
			targetList.add(maxPluginScore.plugin);
			//从列表里面删除
			tempSortList.remove(maxPluginScore.plugin);
			//删除相关的关系
			for (int i=extendRelations.size()-1;i>=0;i--) {
				ExtendRelation er = extendRelations.get(i);
				if (er.extend==maxPluginScore.plugin || er.extended==maxPluginScore.plugin) {
					extendRelations.remove(i);
				}
			}
		}

	}

	private static List<PluginScore> getPluginScores(List<ExtendRelation> extendRelations,
			List<AbstractPlugin> tempSortList) {
		List<PluginScore> pluginScoreList = new ArrayList(tempSortList.size());
		tempSortList.forEach(p->pluginScoreList.add( makePluginScore(p,extendRelations)));
		return pluginScoreList;
	}
	
	private static PluginScore makePluginScore(AbstractPlugin p, List<ExtendRelation> extendRelations) {
		int extendNum=0;
		for (ExtendRelation er:extendRelations) {
			if (er.extend == p) {
				extendNum++;
			}
		}

		PluginScore ps = new PluginScore();
		ps.plugin = p;
		ps.score = (extendNum* -1000000) + (p.getExtensionPoints().size()*1000) + (p.getExtensions().size());
		return ps;
	}

	private static class ExtendRelation{
		AbstractPlugin extend;
		AbstractPlugin extended;
	}
	private static List<ExtendRelation> getExtendRelations(List<AbstractPlugin> list) {
		List<ExtendRelation>  results = new ArrayList();
		for (AbstractPlugin plugin:list) {
			List<Extension> exts = plugin.getExtensions();
			if (exts.size()>0) {
				//找到这个Plugin所有扩展扩展点名字Set
				Set<String> pointToNameSet = new HashSet<String>();
				exts.forEach(e ->pointToNameSet.add(e.getExtensionPointName()));
				
				for (String pointToName:pointToNameSet) {
					for (AbstractPlugin p:list) {
						if (p!=plugin && checkExtend(pointToName,p)) {
							ExtendRelation er = new ExtendRelation();
							er.extend = plugin;
							er.extended = p;
							results.add(er);
							//只要找个一个对应的扩展，就算扩展到了,break
							break;
						}
					}
					//break to here
				}
			}
		}
		return results;
	}

	private static boolean checkExtend(String pointToName, AbstractPlugin plugin) {
		for (ExtensionPoint point:plugin.getExtensionPoints()) {
			if (point.getName().equals(pointToName)) {
				return true;
			}
		}
		return false;
	}

	private static List<AbstractPlugin> getSimplePriorityPlugins(List<AbstractPlugin> allPluginList, int startPos) {
		List<AbstractPlugin> result = new ArrayList();
		result.add(allPluginList.get(startPos));
		
		int priority = allPluginList.get(startPos).getPrivority();
		
		for (int i=startPos+1;i<allPluginList.size();i++) {
			if (priority == allPluginList.get(i).getPrivority()) {
				result.add(allPluginList.get(i));
			}
		}
		AssertKit.assertTrue(result.size()>=2);
		return result;
	}
	
}
