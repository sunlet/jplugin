package net.jplugin.common.kits;

import java.util.List;



/**
 * @author LiuHang
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SortUtil {

	/**
	 * 冒泡排序的笨方法（可以进一步优化）
	 * nxn 可以优化为 nxn/2
	 * @param arr
	 * @param comparor
	 */
	public static void sort(Object[] arr,Comparor comparor){
		Object tmp;
		for (int i=0;i<arr.length-1;i++){
			for (int j=0;j<arr.length-1;j++){
				if (comparor.isGreaterThen(arr[j],arr[j+1])){
					tmp=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=tmp;
				}
			}
		}
	}

	/**
	 * @param pluginList
	 * @param comparor
	 */
	public static void sort(List list, Comparor comparor) {
		//目前先用一个慢速的实现，后续再优化
		Object[] data = new Object[list.size()];
		for (int i=0;i<list.size();i++){
			data[i] = list.get(i);
		}
		list.clear();
		
		sort(data,comparor);
		
		for (Object elem:data){
			list.add(elem);
		}
		
	}
}
