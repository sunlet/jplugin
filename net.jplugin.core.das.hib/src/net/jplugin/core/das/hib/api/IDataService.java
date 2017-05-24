package net.jplugin.core.das.hib.api;

import java.util.List;

import net.jplugin.core.das.api.ExtCond;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.stat.CreateIndexStatement;
import net.jplugin.core.das.api.stat.DeleteStatement;
import net.jplugin.core.das.api.stat.InsertStatement;
import net.jplugin.core.das.api.stat.SelectStatement;
import net.jplugin.core.das.api.stat.UpdateStatement;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 上午08:47:47
 **/

public interface IDataService {

	public void flush();
	public void insert(Object obj);

	//去掉UPdate方法，这个方法没什么用处的，因为持久化对象可以自动update,
	//同时，也不怎么需要对非持久对象调用update
	//public void update(Object obj);

	public boolean delete(Object obj);

	public boolean delete(Class<?> obj, Object id);

	public <T> T findById(Class<T> obj, Object id);

	public <T> List<T> queryAll(Class<T> obj);
	
	public <T> List<T> queryByCondWithPage(Class<T> clz, String hcond, Object[] param,ExtCond ec);

	public <T> List<T> queryByCond(Class<T> clz, String hcond, Object... param);
	
	public <T> List<T> queryByHqlWithPage(Class<T> clz, String cond, Object[] param,ExtCond ec);

	public <T> List<T> queryByHql(Class<T> clz, String cond, Object... param);

	public List<Object> queryBySqlWithPage(String sql, Object[] binds,ExtCond ec);

	public List<Object> queryBySql(String sql, Object... binds);
	
	//暂时没有实现fetchBySql方法，使用querybysql方法把resultset以List数组的形式返回，也不错的！
	
	public int executeUpdateSql(UpdateStatement us);
	public int executeDeleteSql(DeleteStatement ds);
	public int executeInsertSql(InsertStatement is);
	public void executeCreateSql(CreateIndexStatement cis);
	public void executeSelect(SelectStatement ss,IResultDisposer rd);
	
	public int executeUpdateSql(String sql,Object...param);
	public int executeDeleteSql(String sql,Object... param);
	public int executeInsertSql(String sql,Object... param);
	public void executeCreateSql(String sql);
	public void executeSelect(String sql,IResultDisposer rd,Object... param);

}
