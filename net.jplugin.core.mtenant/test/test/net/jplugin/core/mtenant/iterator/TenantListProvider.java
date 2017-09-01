package test.net.jplugin.core.mtenant.iterator;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.mtenant.api.ITenantListProvidor;

public class TenantListProvider implements ITenantListProvidor {

	@Override
	public List<String> getTenantList() {
		ArrayList list = new ArrayList<>();
		list.add("1001");
		list.add("1002");
		return list;
	}

}
