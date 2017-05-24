package test.net.luis.plugin.das.mybatis.txtest;

import java.sql.Connection;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.TransactionManager;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
import net.jplugin.core.das.mybatis.impl.IMapperHandler;
import net.jplugin.core.das.mybatis.impl.IMybatisService;
import net.jplugin.core.service.api.ServiceFactory;

public class TxTest {
	public void test(){
		initdb();
		
		IMybatisService testdbSvc = MyBatisServiceFactory.getService("testdb");
		IMybatisService defaultSvc = MyBatisServiceFactory.getService("database");
		
		//清理数据
		clearData(testdbSvc,defaultSvc);
		
		testError(testdbSvc,defaultSvc);
		
		//清理数据
		clearData(testdbSvc,defaultSvc);
		testOK(testdbSvc,defaultSvc);
	}
	
	private void clearData(IMybatisService testdbSvc, IMybatisService defaultSvc) {
		testdbSvc.runWithMapper(ITxTestDB1Mapper.class,new IMapperHandler<ITxTestDB1Mapper>(){
			@Override
			public void run(ITxTestDB1Mapper mapper) {
				mapper.clear();
			}} );
		
		defaultSvc.runWithMapper(ITxTestDB2Mapper.class,new IMapperHandler<ITxTestDB2Mapper>(){
			@Override
			public void run(ITxTestDB2Mapper mapper) {
				mapper.clear();
			}} );
	}

	private void testOK(IMybatisService testdbSvc, IMybatisService defaultSvc) {
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		txm.begin();
		try{
			testdbSvc.runWithMapper(ITxTestDB1Mapper.class,new IMapperHandler<ITxTestDB1Mapper>(){
				@Override
				public void run(ITxTestDB1Mapper mapper) {
					mapper.add("f1", "f2");
				}} );
			
			defaultSvc.runWithMapper(ITxTestDB2Mapper.class,new IMapperHandler<ITxTestDB2Mapper>(){
				@Override
				public void run(ITxTestDB2Mapper mapper) {
					mapper.add("f1", "f2");
				}} );
		}finally{
			txm.commit();
		}
		
		testdbSvc.runWithMapper(ITxTestDB1Mapper.class,new IMapperHandler<ITxTestDB1Mapper>(){
			@Override
			public void run(ITxTestDB1Mapper mapper) {
				AssertKit.assertEqual(mapper.select().size(),1);
			}} );
		defaultSvc.runWithMapper(ITxTestDB2Mapper.class,new IMapperHandler<ITxTestDB2Mapper>(){
			@Override
			public void run(ITxTestDB2Mapper mapper) {
				AssertKit.assertEqual(mapper.select().size(),1);
			}} );

	}
	
	private void testError(IMybatisService testdbSvc, IMybatisService defaultSvc) {
		TransactionManager txm = ServiceFactory.getService(TransactionManager.class);
		txm.begin();
		try{
			testdbSvc.runWithMapper(ITxTestDB1Mapper.class,new IMapperHandler<ITxTestDB1Mapper>(){
				@Override
				public void run(ITxTestDB1Mapper mapper) {
					mapper.add("f1", "f2");
				}} );
			
			defaultSvc.runWithMapper(ITxTestDB2Mapper.class,new IMapperHandler<ITxTestDB2Mapper>(){
				@Override
				public void run(ITxTestDB2Mapper mapper) {
					mapper.add("f1", "f2");
				}} );
		}finally{
			txm.rollback();
		}
		
		testdbSvc.runWithMapper(ITxTestDB1Mapper.class,new IMapperHandler<ITxTestDB1Mapper>(){
			@Override
			public void run(ITxTestDB1Mapper mapper) {
				AssertKit.assertEqual(mapper.select().size(),0);
			}} );
		defaultSvc.runWithMapper(ITxTestDB2Mapper.class,new IMapperHandler<ITxTestDB2Mapper>(){
			@Override
			public void run(ITxTestDB2Mapper mapper) {
				AssertKit.assertEqual(mapper.select().size(),0);
			}} );

	}

	private void initdb() {
		Connection conn = ServiceFactory.getService(IMybatisService.class).getConnection();
		try{
		SQLTemplate.executeCreateSql(conn, "create table TxTestDB1(f1 varchar(100),f2 varchar(100))");
		}catch(Exception e){}
		try{
			SQLTemplate.executeCreateSql(conn, "create table TxTestDB2(f1 varchar(100),f2 varchar(100))");
		}catch(Exception e){}
	}
}
