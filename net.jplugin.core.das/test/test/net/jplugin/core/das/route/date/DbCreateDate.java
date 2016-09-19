package test.net.jplugin.core.das.route.date;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class DbCreateDate {

	static String[] drops = { "drop table tb_date_160101", 
			"drop table tb_date_160102", 
			"drop table tb_tisp_1601",
			"drop table tb_tisp_1602"};

	static String[] creates = { "create table tb_date_160101 (f1 varchar(20) primary key,f2 date)",
			"create table tb_date_160102 (f1 varchar(20) primary key,f2 date)",
			"create table tb_tisp_1601 (f1 varchar(20) primary key,f2 timestamp)",
			"create table tb_tisp_1602 (f1 varchar(20) primary key,f2 timestamp)"};

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
	}

}
