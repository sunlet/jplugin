package net.jplugin.core.das.route.impl.sqlhandler2;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.das.route.api.DataSourceInfo;

public class InSectUtil {

	public static DataSourceInfo[] computeInsect(List<DataSourceInfo[]> list) {
		List<String> tables = new ArrayList<>();
		List<DataSourceInfo> ret = new ArrayList();
		
		//目前取第一个作为比较源，其实也可以找数据源个数最少的一个座位比较对象
		for (DataSourceInfo dsi:list.get(0)){
			String dsName = dsi.getDsName();
			tables.clear();
			//拿第一个中的每一项出来比较
			for (String tbname:dsi.getDestTbs()){
				boolean exists = true;
				//从第一个找到最后一个
				for (int i=1;i<list.size();i++){
					if (!tableExists(dsName,tbname,list.get(i))){
						exists = false;
						break;
					}
				}
				if (exists){
					tables.add(tbname);
				}
			}
			
			if (!tables.isEmpty()){
				DataSourceInfo temp = new DataSourceInfo();
				temp.setDsName(dsName);
				temp.setDestTbs(tables.toArray(new String[tables.size()]));
				ret.add(temp);
			}
		}
		return ret.toArray(ret.toArray(new DataSourceInfo[ret.size()]));
	}

	private static boolean tableExists(String dsName, String tbname, DataSourceInfo[] dataSourceInfos) {
		for (DataSourceInfo dsi:dataSourceInfos){
			if (dsName.equals(dsi.getDsName())){
				String[] tbs = dsi.getDestTbs();
				for (String tb:tbs){
					if (tbname.equals(tb)) 
						return true;
				}
			}
		}
		return false;
	}

}
