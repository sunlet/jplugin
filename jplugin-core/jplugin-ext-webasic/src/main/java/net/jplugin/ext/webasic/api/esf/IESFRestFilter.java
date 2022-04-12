package net.jplugin.ext.webasic.api.esf;

import net.jplugin.common.kits.filter.IFilter;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.ext.webasic.impl.ESFRestContext;
import net.jplugin.ext.webasic.impl.restm.invoker.CallParam;

public interface IESFRestFilter extends IFilter<Tuple2<ESFRestContext, CallParam>> {

}
