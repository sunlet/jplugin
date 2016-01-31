package net.jplugin.core.das.hib.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.api.ExtCond;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.PageCond;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.api.stat.CreateIndexStatement;
import net.jplugin.core.das.api.stat.DeleteStatement;
import net.jplugin.core.das.api.stat.InsertStatement;
import net.jplugin.core.das.api.stat.SelectStatement;
import net.jplugin.core.das.api.stat.UpdateStatement;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.das.hib.api.IPersistObjDefinition;
import net.jplugin.core.das.hib.api.IResultSetDisposer;
import net.jplugin.core.das.hib.api.SinglePoDefine;
import net.jplugin.core.service.api.ServiceFactory;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-9 上午09:02:26
 **/

public class DataService4Hibernate implements IDataService{
	TransactionHandler4Hib txHandler = null;
	public DASHelper dasHelper;
	
	public void init(IPersistObjDefinition[] podefs, SinglePoDefine[] singlePoDefs){
		dasHelper = new DASHelper("sessionFactory",podefs,singlePoDefs);
		txHandler = new TransactionHandler4Hib(dasHelper);
		ServiceFactory.getService(TransactionManager.class).addTransactionHandler(txHandler);
	}
	
	public void insert(Object obj){
		Session sess = dasHelper.getOrCreateSession();
		sess.save(obj);
	}
	
	public void update(Object obj){
		Session sess = dasHelper.getOrCreateSession();
		sess.update(obj);
	}
	
	public boolean delete(Object obj){
		Session sess = dasHelper.getOrCreateSession();
		sess.delete(obj);
		return true;
	}
	
	public boolean delete(Class<?> clazz,Object id){
		Session sess = dasHelper.getOrCreateSession();
	    Object obj = sess.get(clazz, (Serializable) id);
	    if (obj!=null){
	    	sess.delete(obj);
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	public <T> T findById(Class<T> clazz,Object id){
		Session sess = dasHelper.getOrCreateSession();
		return (T) sess.get(clazz, (Serializable) id);
	}
	
	public <T> List<T> queryByCondWithPage(Class<T> clz,String hcond,Object[] param,ExtCond ec){
		Session session = dasHelper.getOrCreateSession();
		String entityname = session.getSessionFactory().getClassMetadata(clz).getEntityName();
		String hql = " from "+entityname +" where 1=1 " ;
		if (StringKit.isNotNull(hcond)){
			hql = hql + " AND "+hcond;
		}
		return queryByHqlWithPage(clz,hql,param,ec);
	}
	
	public <T> List<T> queryByHqlWithPage(Class<T> clz,String hql,Object[] param,ExtCond ec){
		Session session = dasHelper.getOrCreateSession();
		
    	if (ec!=null && ec.getOrderBy()!=null){
    		hql = hql + " "+ec.getOrderBy();
    	}

		Query query = session.createQuery( hql);
    	for( int i=0; param!=null && i<param.length; i++ ){
    		query.setParameter( i, param[i] );
    	}
    	if (ec!=null && ec.getPageCond()!=null){
    		PageCond pc = ec.getPageCond();
    		query.setFirstResult(pc._getFirstRow());
    		query.setMaxResults(pc.getPageSize());
    	}
    	List list = query.list();
    	return list;
	}
	
	public List<Object> queryBySqlWithPage(String sql,Object[] binds,ExtCond ec){
		Session session = dasHelper.getOrCreateSession();
		if (ec!=null && ec.getOrderBy()!=null){
			sql = sql + " "+ec.getOrderBy();
		}
		SQLQuery query = session.createSQLQuery(sql);
		for( int i=0; binds!=null && i<binds.length; i++ ){
    		query.setParameter( i, binds[i] );
    	}
		if (ec!=null && ec.getPageCond()!=null){
			PageCond pc = ec.getPageCond();
			query.setFirstResult(pc._getFirstRow());
    		query.setMaxResults(pc.getPageSize());
		}
		List list = query.list();
		return list;
	}
	
//	public void fetchBySql(String sql,Object[] binds,IResultSetDisposer rsd,PageCond pc){
//		Session session = dasHelper.getOrCreateSession();
//		Connection connection = session.connection();
//		SQLTemplate.fetchBySql(connection,sql,binds,rsd,pc);
//	}
	
	public int executeDeleteSql(String sql, Object... param) {
		Session session = dasHelper.getOrCreateSession();
		Connection connection = session.connection();
		return SQLTemplate.executeDeleteSql(connection,sql,param);
	}

	public int executeInsertSql(String sql, Object... param) {
		Session session = dasHelper.getOrCreateSession();
		Connection connection = session.connection();
		return SQLTemplate.executeInsertSql(connection,sql,param);
	}

	public int executeUpdateSql(String sql, Object... param) {
		Session session = dasHelper.getOrCreateSession();
		Connection connection = session.connection();
		return SQLTemplate.executeUpdateSql(connection,sql,param);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IDataService#executeCreateSql(java.lang.String)
	 */
	public void executeCreateSql(String sql) {
		Session session = dasHelper.getOrCreateSession();
		Connection connection = session.connection();
		SQLTemplate.executeCreateSql(connection,sql);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IDataService#queryAll(java.lang.Class)
	 */
	public  <T> List<T> queryAll(Class<T> clazz) {
		return this.queryByCondWithPage(clazz, null,null,null);
	}

	
	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IDataService#queryByCond(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	public <T> List<T> queryByCond(Class<T> clz, String hcond, Object... param) {
		return queryByCondWithPage(clz,hcond,param,null);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IDataService#queryByHql(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	public <T> List<T> queryByHql(Class<T> clz, String cond, Object... param) {
		return queryByHqlWithPage(clz,cond,param,null);
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.das.api.IDataService#queryBySql(java.lang.String, java.lang.Object[])
	 */
	public List<Object> queryBySql(String sql, Object... binds) {
		return queryBySqlWithPage(sql,binds,null);
	}
	
	public void executeSelect(String sql,IResultDisposer rd,Object... param){
		Session session = dasHelper.getOrCreateSession();
		Connection connection = session.connection();
		SQLTemplate.executeSelect(connection,sql,rd,param);
	}

	public int executeUpdateSql(UpdateStatement us) {
		return executeUpdateSql(us.getSqlClause(),us.getParams().toArray());
	}

	public int executeDeleteSql(DeleteStatement ds) {
		return executeDeleteSql(ds.getSqlClause(),ds.getParams().toArray());
	}

	public int executeInsertSql(InsertStatement is) {
		return executeInsertSql(is.getSqlClause(),is.getParams().toArray());
	}

	public void executeCreateSql(CreateIndexStatement cis) {
		executeCreateSql(cis.getSqlClause());
	}

	public void executeSelect(SelectStatement ss, IResultDisposer rd) {
		executeSelect(ss.getSqlClause(),rd,ss.getParams().toArray());
	}

	public void flush() {
		Session session = dasHelper.getOrCreateSession();
		session.flush();
	}
}
