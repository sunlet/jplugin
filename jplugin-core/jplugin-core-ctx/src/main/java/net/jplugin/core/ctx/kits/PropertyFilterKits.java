package net.jplugin.core.ctx.kits;

import net.jplugin.core.kernel.api.Extension;

public class PropertyFilterKits {

	public static String filterProperty(String applyTo) {
		/**
		 * 这里代码有点丑，先这样了，暂时用Extension.propertyFilter
		 */
		if (Extension.propertyFilter!=null)
			return Extension.propertyFilter.filte(applyTo);
		else
			return applyTo;
	}
}
