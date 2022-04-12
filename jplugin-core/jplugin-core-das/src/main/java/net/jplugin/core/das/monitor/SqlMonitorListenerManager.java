package net.jplugin.core.das.monitor;

import net.jplugin.core.das.api.monitor.ISqlMonitorListener;
import net.jplugin.core.das.api.monitor.ResultSetContext;
import net.jplugin.core.das.api.monitor.StatementContext;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class SqlMonitorListenerManager {
	public static SqlMonitorListenerManager instance = new SqlMonitorListenerManager();
	ISqlMonitorListener[] arr;

	public void init() {
		ISqlMonitorListener[] a = PluginEnvirement.getInstance()
				.getExtensionObjects(net.jplugin.core.das.Plugin.EP_SQL_LISTENER, ISqlMonitorListener.class);
		if (a != null && a.length > 0) {
			arr = a;
			PluginEnvirement.getInstance().getStartLogger().log("Sql Monitor listener size:"+arr.length);
		}
	}

	public boolean hasListener() {
		return arr != null && arr.length > 0;
	}

	public void beforeExecute(StatementContext ctx) {
		for (ISqlMonitorListener o : arr) {
			if (!o.beforeExecute(ctx))
				throw new RuntimeException(
						"The sql is prevent by sql filter. sql:" + ctx.getSql() + " listener:" + o.getClass());
		}
	}

	public void beforeNext(ResultSetContext ctx) {
		for (ISqlMonitorListener o : arr) {
			if (!o.beforeNext(ctx))
				throw new RuntimeException(
						"The cursor fetch is prevent by sql filter. sql:" + ctx.getSql() + " listener:" + o.getClass());
		}
	}

	public void afterExecute(StatementContext ctx) {
		for (int i = arr.length - 1; i >= 0; i--) {
			arr[i].afterExecute(ctx);
		}

	}

	public void afterNext(ResultSetContext ctx) {
		for (int i = arr.length - 1; i >= 0; i--) {
			arr[i].afterNext(ctx);
		}
	}

}
