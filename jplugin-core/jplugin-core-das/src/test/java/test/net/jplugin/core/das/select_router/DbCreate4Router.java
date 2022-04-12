package test.net.jplugin.core.das.select_router;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;


public class DbCreate4Router {

	static String[] drops = { "drop table db_a.the_tb1", 
			"drop table db_b.the_tb2", 
			"drop table db_c.the_tb3"
//			,
//			"drop database db_a",
//			"drop database db_b",
//			"drop database db_c"
			};
//	
//	static String[] createDb= { "create database db_a", 
//			"create database db_b", 
//			"create database db_c"
//			};

	static String[] creates = { 
			"create table db_a.the_tb1 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table db_b.the_tb2 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table db_c.the_tb3 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )"
			};

	public static void create() throws SQLException {
		DataSource ds = DataSourceFactory.getDataSource("database");
		Connection conn = ds.getConnection();
		drop();
//		createDb();
		for ( String s:creates){
			SQLTemplate.executeCreateSql(conn, s);
		}
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
	}
	
//	public static void createDb() throws SQLException{
//		DataSource ds = DataSourceFactory.getDataSource("database");
//		Connection conn = ds.getConnection();
//		for ( String s:createDb){
//			try{
//			SQLTemplate.executeCreateSql(conn, s);
//			}catch(Exception e){
//				System.out.println("database exists!");
//			}
//		}
//	}

}
