package net.jplugin.core.das.route.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.jplugin.core.das.route.api.IAggregationFunctionHandler;
import net.jplugin.core.das.route.api.IFunctionHandler;
import net.jplugin.core.kernel.api.ClassDefine;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class FunctionHandlerManager {
	public static FunctionHandlerManager INSTANCE = new FunctionHandlerManager();
	Map<String, IFunctionHandler> funcHandlers = new HashMap();
	Map<String, ClassDefine> aggFunctions;

	public void init() {
		aggFunctions = PluginEnvirement.getInstance()
				.getExtensionMap(net.jplugin.core.das.route.Plugin.EP_SQL_AGG_FUNCTION, ClassDefine.class);

		funcHandlers = PluginEnvirement.getInstance()
				.getExtensionMap(net.jplugin.core.das.route.Plugin.EP_SQL_FUNCTION, IFunctionHandler.class);
		
	}
	
	/**
	 * 普通函数返回单例。 聚集函数返回新实例。
	 */
	public IFunctionHandler getFunctionHandler(String name) {
		if (name == null)
			return null;
		IFunctionHandler f = funcHandlers.get(name);
		return f;
	}
	
	public IAggregationFunctionHandler createAggFunctionHandler(String name){
		ClassDefine aggFuncDef = aggFunctions.get(name);
		if (aggFuncDef == null)
			return null;
		try {
			return (IAggregationFunctionHandler) aggFuncDef.getClazz().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(name + " error", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(name + " error", e);
		}

	}
}
