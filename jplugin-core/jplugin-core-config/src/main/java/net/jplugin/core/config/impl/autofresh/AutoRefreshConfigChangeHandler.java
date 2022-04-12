package net.jplugin.core.config.impl.autofresh;

import java.util.List;

import net.jplugin.core.config.api.IConfigChangeContext;
import net.jplugin.core.config.api.IConfigChangeHandler;

public class AutoRefreshConfigChangeHandler implements IConfigChangeHandler{

	@Override
	public void onChange(IConfigChangeContext ctx) {
		List<String> keys = ctx.getChangedKeys();
		RefConfigAutoRefresher.instance.keysChanged(keys);
	}

}
