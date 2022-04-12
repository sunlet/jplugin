package net.jplugin.core.das.dds.api;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.das.dds.impl.IStatementContextCallable;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.sf.jsqlparser.statement.Statement;

/**
 * <PRE>
 * 由于sql解析的成本很高，这个类用来对解析进行缓存。方法是：
 * 1.最外一层调用创建Context和清除context。
 * 2.开发上确保所有的sql变化都是修改statement，然后生成的sql。
 * 
 * 同时，这里做一个封装，把非sql异常封装成为sql异常。
 * </PRE>
 * @author LiuHang
 */
public class RouterExecutionContext {

	static ThreadLocal<RouterExecutionContext> statementCacheContext=new ThreadLocal<RouterExecutionContext>();
	static boolean CHECK;
	static {
		CHECK = "true".equalsIgnoreCase(ConfigFactory.getStringConfigWithTrim("platform.route_ctx_getstmt_with_check"));
		PluginEnvirement.getInstance().getStartLogger().log("$$$ platform.route_ctx_getstmt_with_check ="+CHECK);
	}
	
	net.sf.jsqlparser.statement.Statement statement=null;
	
	public static Object call(IStatementContextCallable callable) throws SQLException {
		try{
			return callInner(callable);
		}catch(Exception e) {
//			e.printStackTrace();
			if (e instanceof SQLException) 
				throw e;
			else
				throw new SQLException(e);
		}
	}
	
	private static Object callInner(IStatementContextCallable callable) throws SQLException {
		RouterExecutionContext ctx = statementCacheContext.get();
		if (ctx==null) {
			//如果本线程第一次获取上下文，则生成和销毁上下文
			try {
				statementCacheContext.set(new RouterExecutionContext());
				return callable.call();
			}finally {
				statementCacheContext.set(null);
			}
		}else {
			//如果已有上下文，则不理
			return callable.call();
		}
	}
	
	public static RouterExecutionContext get() {
		RouterExecutionContext result = statementCacheContext.get();
		if (result==null) {
			throw new RuntimeException("Not in context now!");
		}else {
			return result;
		}
	}
	
	private RouterExecutionContext() {}

	
	public net.sf.jsqlparser.statement.Statement getStatement(String sql) {
		if (statement!=null) {
			if (CHECK) {
				check(sql,statement);
			}
			return statement;
		}else {
			statement = net.jplugin.core.das.dds.kits.SqlParserKit.parse(sql);
			return statement;
		}
	}
	
	//检查这个SQL和statement是否一致。这个用来校验，确保传入的SQL是通过这里的Statement生成的。
	//或者sql本身和stmt.toString（）一致，或者sql解析以后toString()和stmt.toString()一致
	private void check(String sql, Statement stmt) {
		String stmtString = stmt.toString();
		if (stmtString.equals(sql)) {
			return;
		}
		
		Statement temp = net.jplugin.core.das.dds.kits.SqlParserKit.parse(sql);
		if (stmtString.equals(temp.toString())) {
			return;
		}
		
		throw new RuntimeException ("Check Error:sql is "+sql+" statement:"+stmtString);
	}
	
}
