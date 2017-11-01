package net.jplugin.core.mtenant.handler2;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor.Mode;
import net.jplugin.core.mtenant.api.ITenantStoreStrategyProvidor.Strategy;

public class MtSqlHandlerManager {

	public static String handle(String sql, String dataSource) {
		Strategy stg = getStrategy(sql, dataSource);
		
		
		
		
		return null;
	}

	private static Strategy getStrategy(String sql, String dataSource) {
		String tid = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		if (StringKit.isNull(tid))
			throw new RuntimeException("Can't find tenantid when handle sql");
		
		Strategy stragegy=null;
		if (TenantStoreStrategyManager.instance.isProviderExist()){
			stragegy = TenantStoreStrategyManager.instance.getStragegy(tid);
			if (stragegy==null)
				throw new RuntimeException("Can't get tenant store stragegy for tenent:"+tid);
		}else{
			stragegy = getDefaultStrategy(sql,dataSource,tid);
		}
		return stragegy;
	}

	private static Strategy getDefaultStrategy(String sql, String dataSource, String tid) {
		Strategy s = new Strategy();
		s.setMode(Mode.ONESELF);
		s.setSchema(dataSource+"_"+tid);
		return s;
	}
}
