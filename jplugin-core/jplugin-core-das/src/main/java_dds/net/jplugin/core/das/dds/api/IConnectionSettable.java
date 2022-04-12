package net.jplugin.core.das.dds.api;

import java.sql.Connection;

/**
 * 这个接口用了,但是暂时没有调用设置...................
 * @author LiuHang
 *
 */
public interface IConnectionSettable {
	void setConnection(Connection conn);
}
