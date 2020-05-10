package test.net.jplugin.core.das.route.stringint;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;
import net.jplugin.core.das.route.impl.TableAutoCreation;

public class DbCreateStringInt {

	static String[] drops = { "drop table tb_route0_1", 
			"drop table tb_route0_2", 
			"drop table tb_route1_1",
			"drop table tb_route1_2", 
			"drop table tb_route1_3", 
			"drop table tb_route1_4",
			"drop table tb_route1_5"};

	static String[] creates = { 
//			"create table tb_route0_1 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
//			"create table tb_route0_2 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route1_1 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route1_2 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route1_3 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route1_4 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route1_5 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )", };

	public static void create() throws SQLException {
		DataSource ds = DataSourceFactory.getDataSource("database");
		Connection conn = ds.getConnection();
		for ( String s:drops){
			try{
			SQLTemplate.executeDropSql(conn, s);
			}catch(Exception e){
				System.out.println("table not exists.");
			}
		}
		for ( String s:creates){
			SQLTemplate.executeCreateSql(conn, s);
		}
		//清理对已存在表的缓存
		TableAutoCreation.clearCache();
	}
	/**
	 * 只删除，不创建
	 * @throws SQLException
	 */
	public static void drop() throws SQLException{
		DataSource ds = DataSourceFactory.getDataSource("database");
		Connection conn = ds.getConnection();
		for ( String s:drops){
			try{
			SQLTemplate.executeDropSql(conn, s);
			}catch(Exception e){
				System.out.println("table not exists.");
			}
		}
		//清理对已存在表的缓存
		TableAutoCreation.clearCache();
	}

}
