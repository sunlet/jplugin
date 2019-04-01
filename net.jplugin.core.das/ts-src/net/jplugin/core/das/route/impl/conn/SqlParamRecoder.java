package net.jplugin.core.das.route.impl.conn;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class SqlParamRecoder {

	List<Object> params=new ArrayList<Object>();
	public List<Object> getList(){
		return params;
	}
	static Integer maxBindingParams=null;
	private static Integer getMaxBindingParams(){
		if (maxBindingParams==null){
			synchronized (SqlParamRecoder.class) {
				if (maxBindingParams==null){
					maxBindingParams=ConfigFactory.getIntConfig("platform.route-sql-max-param-num", 500000);
					PluginEnvirement.getInstance().getStartLogger().log("platform.route-sql-max-param-num = "+maxBindingParams);
				}
			}
		}
		return maxBindingParams;
	}
	public void set(int sqlIndex,Object o){
		int index = sqlIndex-1;
		if (index<0) throw new TablesplitException("sql parameter index error:"+sqlIndex);
		if (index>getMaxBindingParams())throw new TablesplitException(" parameter index overflow:"+sqlIndex);
		
		int size = params.size();
		if (index==size) params.add(o);
		else if (index<size) params.set(index, o);
		else {
			for (int i=0;i<index - size;i++){
				params.add(null);
			}
			params.add(o);
		}
	}
}
