package test.net.jplugin.core.das.route.where.funcs;

import net.jplugin.core.das.route.api.IFunctionHandler;

public class MytestFunctionHandler implements IFunctionHandler{

	@Override
	public Object getResult(Object[] args) {
		long ret = 0;
		for (Object o:args){
			if (o!=null){
				if (! (o instanceof Long)){
					throw new RuntimeException(" must be int type param.");
				}
				ret += (Long)o;
			}
		}
		return ret;
	}

}
