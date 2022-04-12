package net.jplugin.core.mtenant.api;

import java.util.List;
/**
 * 提供租户列表
 * @author Administrator
 *
 */
public interface ITenantListProvidor {
	List<String> getTenantList();
}
