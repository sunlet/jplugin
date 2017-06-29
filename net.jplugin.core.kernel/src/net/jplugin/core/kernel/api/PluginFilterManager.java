package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.filter.FilterManager;
import net.jplugin.common.kits.filter.IFilter;

public class PluginFilterManager<T> extends FilterManager<T>{
	
	String point;
	IFilter<T> lastFilter;
	
	public PluginFilterManager(String p,IFilter<T> f) {
		this.point = p;
		this.lastFilter = f;
	}
	
	public  void init() {
		IFilter<T>[] arr = PluginEnvirement.getInstance()
				.getExtensionObjects(point, IFilter.class);

		for (IFilter<T> o : arr) {
			this.addFilter(o);
		}
		
		if (lastFilter!=null)
			this.addFilter(lastFilter);
	}

}
