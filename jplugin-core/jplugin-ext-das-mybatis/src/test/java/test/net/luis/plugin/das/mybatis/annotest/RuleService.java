package test.net.luis.plugin.das.mybatis.annotest;

import java.util.List;

import net.jplugin.common.kits.ObjectRef;
import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.mybatis.impl.IMapperHandler;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.service.api.ServiceFactory;
import test.net.jplugin.core.das.mybatis.MybtestBean;

public class RuleService implements IRule{

	@Override
	public void add(String f1, String f2) {
		IMybatisService svc = ServiceFactory.getService(IMybatisService.class);
		svc.getMapper(IMybtestMapper.class).add("aaa", "bbb");
//		svc.runWithMapper(IMybtestMapper.class,new IMapperHandler<IMybtestMapper>(){
//
//			@Override
//			public void run(IMybtestMapper m) {
//				m.add("aaa", "bbb");
//			}
//		} );
		
	}
	
	@Rule(methodType=TxType.REQUIRED)
	@Override
	public void addAndRollback(String f1, String f2) {
		IMybatisService svc = ServiceFactory.getService(IMybatisService.class);
		svc.runWithMapper(IMybtestMapper.class,new IMapperHandler<IMybtestMapper>(){

			@Override
			public void run(IMybtestMapper m) {
				m.add("aaa", "bbb");
			}
		} );
		
		ServiceFactory.getService(TransactionManager.class).setRollbackOnly();
	}

	@Override
	public void clear() {
		IMybatisService svc = ServiceFactory.getService(IMybatisService.class);
		svc.runWithMapper(IMybtestMapper.class,new IMapperHandler<IMybtestMapper>(){

			@Override
			public void run(IMybtestMapper m) {
				m.clear();
			}
		} );
		
	}

	@Override
	public List<MybtestBean> select() {
		ObjectRef ref = new ObjectRef();
		IMybatisService svc = ServiceFactory.getService(IMybatisService.class);
		svc.runWithMapper(IMybtestMapper.class,new IMapperHandler<IMybtestMapper>(){

			@Override
			public void run(IMybtestMapper m) {
				ref.set(m.select());
			}
		} );
		
		return (List<MybtestBean>) ref.get();
		
	}

}
