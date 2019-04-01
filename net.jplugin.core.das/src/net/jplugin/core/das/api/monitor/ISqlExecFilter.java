package net.jplugin.core.das.api.monitor;

import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.das.monitor.SqlCall;

public interface ISqlExecFilter extends IFilter<Tuple2<StatementContext,SqlCall>> {

}
