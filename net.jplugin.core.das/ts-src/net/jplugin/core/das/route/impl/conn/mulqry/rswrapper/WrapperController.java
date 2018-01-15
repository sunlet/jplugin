package net.jplugin.core.das.route.impl.conn.mulqry.rswrapper;

import java.sql.ResultSet;

import net.jplugin.core.das.route.impl.CombinedSqlContext;

public interface WrapperController {
	
	/**
	 * 用来初始化context。比如把Limit部分拿出来供Wrapper使用，并修改一下Statement。
	 * @param ctx
	 */
	void handleContextInitial(CombinedSqlContext ctx);
	
	/**
	 * 判断是否需要包装ResultSet
	 * @return
	 */
	boolean needWrap();
	
	/**
	 * 产生真正的包装ResultSet
	 * @param rs
	 * @return
	 */
	ResultSet wrap(ResultSet rs);

}