package test.net.jplugin.core.das.route;

import net.jplugin.core.das.api.ISqlMonitorListener;
import net.jplugin.core.das.api.SqlMonitorListenerContext;

public class SqlMonitorListenerTest implements ISqlMonitorListener {

	@Override
	public boolean beforeExecute(SqlMonitorListenerContext ctx) {
//		System.out.println("###Before sql execute "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
		return true;
	}

	@Override
	public boolean beforeNext(SqlMonitorListenerContext ctx) {
//		System.out.println("###Before cursor next "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
		return true;
	}

	@Override
	public void afterExecute(SqlMonitorListenerContext ctx) {
		System.out.println("###After  execute "+" ds="+ctx.getDataSourceName()+ " "+ctx.getSql());
	}

	@Override
	public void afterNext(SqlMonitorListenerContext ctx) {
//		System.out.println("###After  next "+" ds="+ctx.getDataSourceName()+" "+ctx.getSql());
	}

}
