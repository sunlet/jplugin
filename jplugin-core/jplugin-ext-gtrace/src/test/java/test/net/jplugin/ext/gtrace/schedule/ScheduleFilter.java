package test.net.jplugin.ext.gtrace.schedule;

import net.jplugin.common.kits.filter.FilterChain;
import net.jplugin.core.kernel.api.IScheduledExecutionFilter;
import net.jplugin.ext.gtrace.api.Span;
import net.jplugin.ext.gtrace.kits.GTraceKit;

public class ScheduleFilter implements IScheduledExecutionFilter {

	@Override
	public Object filter(FilterChain fc, Object ctx) throws Throwable {
		Span span = GTraceKit.getCurrentSpan();
		int type = span.getType();
		System.out.println("===spantype:"+type+" spanid:"+span.getId());
		return fc.next(ctx);
	}

}
