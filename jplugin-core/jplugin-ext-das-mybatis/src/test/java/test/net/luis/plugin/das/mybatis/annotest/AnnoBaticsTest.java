package test.net.luis.plugin.das.mybatis.annotest;

import java.sql.Connection;
import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.impl.IMapperHandler;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.service.api.ServiceFactory;
import test.net.jplugin.core.das.mybatis.MybtestBean;

public class AnnoBaticsTest {

	public void test() {
		initdb();
			
		IRule rule = RuleServiceFactory.getRuleService(IRule.class);
		rule.clear();
		
		rule.add("aa", "aaa");
		
		try{
			rule.addAndRollback("bb", "bbb");
		}catch(Exception e){}
		
		rule.add("aa", "bbb");
		List<MybtestBean> list = rule.select();
		
		for (MybtestBean o:list){
			System.out.println(o);
		}
		
		AssertKit.assertEqual(2, list.size());
	}

	private void initdb() {
		Connection conn = ServiceFactory.getService(IMybatisService.class).getConnection();
		try{
		SQLTemplate.executeCreateSql(conn, "create table mybtest(f1 varchar(100),f2 varchar(100))");
		}catch(Exception e){}
	}

}
