package test.net.jplugin.core.das.route;

import net.jplugin.core.das.api.monitor.ISqlMonitorListener;
import net.jplugin.core.das.api.monitor.ResultSetContext;
import net.jplugin.core.das.api.monitor.StatementContext;

public class SqlMonitorListenerTest implements ISqlMonitorListener {

	@Override
	public boolean beforeExecute(StatementContext ctx) {
//		System.out.println("###Before sql execute "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
		return true;
	}

	@Override
	public boolean beforeNext(ResultSetContext ctx) {
//		System.out.println("###Before cursor next "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
		return true;
	}

	@Override
	public void afterExecute(StatementContext ctx) {
		System.out.println("###After  execute "+" ds="+ctx.getDataSourceName()+ " "+ctx.getSql());
	}

	@Override
	public void afterNext(ResultSetContext ctx) {
//		System.out.println("###After  next "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
	}

}
