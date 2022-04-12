package test.net.jplugin.core.das.mybatis.ts;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class DbCreate {

	static String[] drops = { "drop table tb_route0_1","drop table tb_route0_2"};

	static String[] creates = { "create table tb_route0_1 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )",
			"create table tb_route0_2 (f1 varchar(20) primary key,f2 int,f3 varchar(20) )" };

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
