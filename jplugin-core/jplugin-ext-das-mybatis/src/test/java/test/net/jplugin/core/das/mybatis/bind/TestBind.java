package test.net.jplugin.core.das.mybatis.bind;

import java.sql.Connection;

import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.api.RefMapper;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.kernel.api.RefAnnotationSupport;
import net.jplugin.core.service.api.ServiceFactory;

public class TestBind extends  RefAnnotationSupport{

	@RefMapper(dataSource = "db_2")
	IMybtestMapperForBind ref1;
	
	@RefMapper(dataSource = "database")
	IMybtestMapperForBind ref2;
	
	public void test() {
		initdb();
		ref1.select();
		ref2.select();
	}

	private void initdb() {
		Connection conn = ServiceFactory.getService(IMybatisService.class).getConnection();
		try{
		SQLTemplate.executeCreateSql(conn, "create table mybtest(f1 varchar(100),f2 varchar(100))");
		}catch(Exception e){}
	}

}
