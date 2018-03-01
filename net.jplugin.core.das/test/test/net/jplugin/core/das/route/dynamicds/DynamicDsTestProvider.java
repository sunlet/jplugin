package test.net.jplugin.core.das.route.dynamicds;

import net.jplugin.core.das.api.IDynamicDataSourceProvider;

public class DynamicDsTestProvider implements IDynamicDataSourceProvider {

	@Override
	public String computeDataSourceName(String s) {
		return "database";
	}

}
