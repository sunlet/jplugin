package net.jplugin.ext.webasic.api.esf;

import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.ext.webasic.api.InvocationContext;
import net.jplugin.ext.webasic.impl.ESFRPCContext;

public interface IESFRpcFilter extends IFilter<Tuple2<ESFRPCContext, InvocationContext>>{

}
