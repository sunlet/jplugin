package net.jplugin.core.das.mybatis.impl.sess;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
/**
 * <pre>
 * 由于创建Mybatis服务的时候已经对链接不提交、不关闭、所以这里就不要对关注 commit,rollback了。
 * 这里只处理一下 close即可。把session的关闭做成假的。
 * 另外，目前在适当的时候clearCache。
 * </pre>
 * @author Administrator
 *
 */
public class SqlSessionAdaptor implements SqlSession {
	SqlSession inner;
	HashMap<Class,Object> mapperMap=new HashMap<>();

	SqlSessionAdaptor(SqlSession real){
		this.inner = real;
	}
	
	public SqlSession getRealSession(){
		return inner;
	}
	
	/**
	 * 缓存对Mapper的获取
	 */
	public <T> T getMapper(Class<T> cls) {
		Object o = mapperMap.get(cls);
		if (o==null){
			o = inner.getMapper(cls);
			if (o!=null) 
				mapperMap.put(cls, o);
		}
		return (T) o;
	}
	

	public void close() {
		//do nothing
	}
	////////////一下方法未动
	public void clearCache() {
		inner.clearCache();
	}

	public void commit() {
		inner.commit();
	}

	public void commit(boolean arg0) {
		inner.commit(arg0);
	}

	public void rollback() {
		inner.rollback();
	}

	public void rollback(boolean arg0) {
		inner.rollback(arg0);
	}


	public int delete(String arg0, Object arg1) {
		return inner.delete(arg0, arg1);
	}

	public int delete(String arg0) {
		return inner.delete(arg0);
	}

	public List<BatchResult> flushStatements() {
		return inner.flushStatements();
	}

	public Configuration getConfiguration() {
		return inner.getConfiguration();
	}

	public Connection getConnection() {
		return inner.getConnection();
	}

	public int insert(String arg0, Object arg1) {
		return inner.insert(arg0, arg1);
	}

	public int insert(String arg0) {
		return inner.insert(arg0);
	}

	public void select(String arg0, Object arg1, ResultHandler arg2) {
		inner.select(arg0, arg1, arg2);
	}

	public void select(String arg0, Object arg1, RowBounds arg2, ResultHandler arg3) {
		inner.select(arg0, arg1, arg2, arg3);
	}

	public void select(String arg0, ResultHandler arg1) {
		inner.select(arg0, arg1);
	}

	public <E> List<E> selectList(String arg0, Object arg1, RowBounds arg2) {
		return inner.selectList(arg0, arg1, arg2);
	}

	public <E> List<E> selectList(String arg0, Object arg1) {
		return inner.selectList(arg0, arg1);
	}

	public <E> List<E> selectList(String arg0) {
		return inner.selectList(arg0);
	}

	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2, RowBounds arg3) {
		return inner.selectMap(arg0, arg1, arg2, arg3);
	}

	public <K, V> Map<K, V> selectMap(String arg0, Object arg1, String arg2) {
		return inner.selectMap(arg0, arg1, arg2);
	}

	public <K, V> Map<K, V> selectMap(String arg0, String arg1) {
		return inner.selectMap(arg0, arg1);
	}

	public <T> T selectOne(String arg0, Object arg1) {
		return inner.selectOne(arg0, arg1);
	}

	public <T> T selectOne(String arg0) {
		return inner.selectOne(arg0);
	}

	public int update(String arg0, Object arg1) {
		return inner.update(arg0, arg1);
	}

	public int update(String arg0) {
		return inner.update(arg0);
	}
	

}
