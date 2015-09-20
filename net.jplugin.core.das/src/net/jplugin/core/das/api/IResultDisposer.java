package net.jplugin.core.das.api;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-8 下午11:16:58
 **/

public interface IResultDisposer {
	void readRow(ResultSet rs) throws SQLException;
}
