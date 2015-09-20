package net.jplugin.core.das.hib.mt;

import java.util.Hashtable;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.ReflactKit;
import net.jplugin.core.das.api.BasicMtBean;
import net.jplugin.core.das.api.ExtCond;
import net.jplugin.core.das.api.IResultDisposer;
import net.jplugin.core.das.api.stat.CreateIndexStatement;
import net.jplugin.core.das.api.stat.DeleteStatement;
import net.jplugin.core.das.api.stat.InsertStatement;
import net.jplugin.core.das.api.stat.SelectStatement;
import net.jplugin.core.das.api.stat.UpdateStatement;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.das.hib.api.IMtDataService;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class MtDataService implements IMtDataService{
	public static final Object MT_FIELDNAME = "tenantId";

	IDataService inner;

	Hashtable<Class<?>, Boolean> mtAttributes = new Hashtable<Class<?>, Boolean>();
	private boolean isMt(Class c){
		Boolean b = mtAttributes.get(c);
		if (b==null){
			b = checkMt(c);
			mtAttributes.put(c, b);
			return b;
		}else{
			return b;
		}
	}
	private Boolean checkMt(Class c) {
		return ReflactKit.isTypeOf(c, BasicMtBean.class);
	}
	
	
	
	public void flush() {
		inner.flush();
	}

	public void insert(Object obj) {
		if (!isMt(obj.getClass())) {
			inner.insert(obj);
			return;
		}
		BasicMtBean mtb = (BasicMtBean) obj;
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		AssertKit.assertNull(mtb.getTenantId());
		mtb.setTenantId(currTenantId);
		inner.insert(obj);
	}

	public boolean delete(Object obj) {
		if (!isMt(obj.getClass())) {
			return inner.delete(obj);
		}
		BasicMtBean mtb = (BasicMtBean) obj;
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		AssertKit.assertEqual(currTenantId,mtb.getTenantId());
		return inner.delete(obj);
	}

	public boolean delete(Class<?> cls, Object id) {
		if (!isMt(cls)) {
			return inner.delete(cls,id);
		}

		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);

		Object obj = inner.findById(cls, id);
		if (obj!=null){
			AssertKit.assertEqual(currTenantId, ((BasicMtBean)obj).getTenantId());
		}
		return inner.delete(cls,id);
	}

	public <T> T findById(Class<T> cls, Object id) {
		if (!isMt(cls)) {
			return inner.findById(cls,id);
		}
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		
		Object obj = inner.findById(cls, id);
		if (obj!=null)
			AssertKit.assertEqual(currTenantId, ((BasicMtBean)obj).getTenantId());
		
		return (T) obj;
	}

	public <T> List<T> queryAll(Class<T> clz) {
		if (!isMt(clz)) {
			return inner.queryAll(clz);
		}
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		
		return inner.queryByCond(clz, "tenantId=?", currTenantId);
	}

	public <T> List<T> queryByCondWithPage(Class<T> clz, String hcond,
			Object[] param, ExtCond ec) {
		if (!isMt(clz)) {
			return inner.queryByCondWithPage(clz,hcond,param,ec);
		}
		
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		
		Object[] pnew = new Object[param.length+1];
		copyArray(param,pnew);
		pnew[pnew.length-1] =  currTenantId;
		
		return inner.queryByCondWithPage(clz, "("+hcond+") and tenantId=?", pnew, ec);
	}

	private void copyArray(Object[] from, Object[] to) {
		for (int i=0;i<from.length;i++){
			to[i] = from [i];
		}
	}
	public <T> List<T> queryByCond(Class<T> clz, String hcond, Object... param) {
		if (!isMt(clz)) {
			return inner.queryByCond(clz,hcond,param);
		}
		
		String currTenantId = ThreadLocalContextManager.getRequestInfo().getCurrentTenantId();
		AssertKit.assertStringNotNull(currTenantId);
		
		Object[] pnew = new Object[param.length+1];
		copyArray(param,pnew);
		pnew[pnew.length-1] =  currTenantId;
		
		return inner.queryByCond(clz, "("+hcond+") and tenantId=?", pnew);
	}

	public <T> List<T> queryByHqlWithPage(Class<T> clz, String cond,
			Object[] param, ExtCond ec) {
		throw new RuntimeException("not support yet");
	}

	public <T> List<T> queryByHql(Class<T> clz, String cond, Object... param) {
		//return inner.queryByHql(clz, cond, param);
		throw new RuntimeException("not impl yet");
	}

	public List<Object> queryBySqlWithPage(String sql, Object[] binds,
			ExtCond ec) {
		//return inner.queryBySqlWithPage(sql, binds, pc);
		throw new RuntimeException("not impl yet");
	}

	public List<Object> queryBySql(String sql, Object... binds) {
		//return inner.queryBySql(sql, binds);
		throw new RuntimeException("not impl yet");
	}

	public int executeUpdateSql(UpdateStatement us) {
		//return inner.executeUpdateSql(us);
		throw new RuntimeException("not impl yet");
	}

	public int executeDeleteSql(DeleteStatement ds) {
		//return inner.executeDeleteSql(ds);
		throw new RuntimeException("not impl yet");
	}

	public int executeInsertSql(InsertStatement is) {
		//return inner.executeInsertSql(is);
		throw new RuntimeException("not impl yet");
	}

	public void executeCreateSql(CreateIndexStatement cis) {
		//inner.executeCreateSql(cis);
		throw new RuntimeException("not impl yet");
	}

	public void executeSelect(SelectStatement ss, IResultDisposer rd) {
		//inner.executeSelect(ss, rd);
		throw new RuntimeException("not impl yet");
	}

	public int executeUpdateSql(String sql, Object... param) {
		//return inner.executeUpdateSql(sql, param);
		throw new RuntimeException("not impl yet");
	}

	public int executeDeleteSql(String sql, Object... param) {
		//return inner.executeDeleteSql(sql, param);
		throw new RuntimeException("not impl yet");
	}

	public int executeInsertSql(String sql, Object... param) {
		//return inner.executeInsertSql(sql, param);
		throw new RuntimeException("not impl yet");
	}

	public void executeCreateSql(String sql) {
		//inner.executeCreateSql(sql);
		throw new RuntimeException("not impl yet");
	}

	public void executeSelect(String sql, IResultDisposer rd, Object... param) {
		//inner.executeSelect(sql, rd, param);
		throw new RuntimeException("not impl yet");
	}
	public void initDataService(IDataService ds) {
		this.inner = ds;
	}
}
