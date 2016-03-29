package net.jplugin.core.das.api.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.jplugin.core.ctx.api.TransactionHandler;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContext;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextListener;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;

public class TxManagedDataSource implements DataSource, TransactionHandler {
	private String DBCONN_IN_CTX;
	private static final String DBCONN_IN_CTX_PREFIX = "DBCONN_IN_CTX";
	DataSource inner;
	
	public TxManagedDataSource(String ds, DataSource in){
		this.inner = in;
		this.DBCONN_IN_CTX = DBCONN_IN_CTX_PREFIX +"#"+ds;
	}
	
	//下面是datasource相关的代理方法，只实现了getConnection一个方法
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return inner.getLogWriter();
	}

	/**
	 * 获取连接，并负责在release时释放连接
	 */
	public Connection getConnection() throws SQLException {
		//如果能在ctx中找到，则返回ctx中的conn
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn != null)
			return conn;
		
		//在ctx中获取不到，则创建一个并放置在ctx中，并设置ReleaseListener确保释放
		conn = inner.getConnection();
		ctx.setAttribute(DBCONN_IN_CTX, conn);
		ctx.addContextListener(new ThreadLocalContextListener() {
			@Override
			public void released(ThreadLocalContext rc) {
				Connection temp = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
				if (temp != null) {
					try {
						if (!temp.isClosed())
							temp.close();
					} catch (SQLException e) {
						net.jplugin.core.log.api.Logger logger = ServiceFactory
								.getService(ILogService.class).getLogger(
										this.getClass().getName());
						logger.error(e);
					}
				}
			}
		});
		//对于新创建的链接，设置新连接的事物属性
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		if (txm.getStatus() != TransactionManager.Status.NOTX){
			conn.setAutoCommit(false);
		}else{
			conn.setAutoCommit(true);
		}
		return conn;
	}
	


	public Connection getConnection(String username, String password)
			throws SQLException {
		throw new RuntimeException("not support");
	}

	public int getLoginTimeout() throws SQLException {
		return inner.getLoginTimeout();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return inner.getParentLogger();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return inner.isWrapperFor(iface);
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		inner.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		inner.setLoginTimeout(seconds);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return inner.unwrap(iface);
	}
	

	//下面实现tx相关方法。目前的实现方法是，连接局限在事物当中。
	//如果begin前有事物，则关闭它;在提交时释放链接。
	
	/**
	 * 获取ctx中的conn，关掉，以便以后获取的连接在事物控制中。
	 */
	@Override
	public void doBegin() {
		//如果已有连接，则加入事物。事物中新获取的连接，会在获取时考虑
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try{conn.close();}catch(Exception e){}
			ctx.setAttribute(DBCONN_IN_CTX,null);
		}
	}
	
	/**
	 * 如果ctx中存在，则rollback
	 */
	@Override
	public void doRollback() {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally{
				try{conn.close();}catch(Exception e){}
				ctx.setAttribute(DBCONN_IN_CTX,null);
			}
		}
	}

	/**
	 * 如果ctx中存在，则commit
	 */
	@Override
	public void doCommit() {
		ThreadLocalContext ctx = ThreadLocalContextManager.instance
				.getContext();
		Connection conn = (Connection) ctx.getAttribute(DBCONN_IN_CTX);
		if (conn!=null){
			try {
				conn.commit();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally{
				try{conn.close();}catch(Exception e){}
				ctx.setAttribute(DBCONN_IN_CTX,null);
			}
		}
	}
}
