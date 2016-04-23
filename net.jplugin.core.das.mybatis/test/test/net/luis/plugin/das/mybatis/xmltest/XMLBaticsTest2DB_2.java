package test.net.luis.plugin.das.mybatis.xmltest;

import java.sql.Connection;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
import net.jplugin.core.das.mybatis.impl.IMapperHandler;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.service.api.ServiceFactory;
import test.net.jplugin.core.das.mybatis.MybtestBean;

public class XMLBaticsTest2DB_2 {
	
	private IMybatisService get(){
		return MyBatisServiceFactory.getService("testdb");
	}
	
	public void test() {
		initdb();
		System.out.println("\nnow test xml ibatis mapper====");
		
		IMybatisService svc = get();
		svc.runWithMapper(IXMLMapper.class,new IMapperHandler<IXMLMapper>(){

			@Override
			public void run(IXMLMapper m) {
				m.clear();
				m.add("aaa", "bbb");
				//System.out.println("\nadd result="+ret);
				
				List<MybtestBean> result = m.select();
				for (MybtestBean o:result){
					System.out.println(o);
				}
			}
		} );
		
	}

	private void initdb() {
		Connection conn = ServiceFactory.getService(IMybatisService.class).getConnection();
		try{
		SQLTemplate.executeCreateSql(conn, "create table mybtest(f1 varchar(100),f2 varchar(100))");
		}catch(Exception e){}
	}

}
