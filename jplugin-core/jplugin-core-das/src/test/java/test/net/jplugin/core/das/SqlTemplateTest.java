package test.net.jplugin.core.das;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;



import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.CalenderKit;
import net.jplugin.common.kits.JsonKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.api.SQLTemplate;

public class SqlTemplateTest {

	
	public void test() throws SQLException {
		initData();
		
		testNormal();
		
		testSpecial();
	}

	private void testSpecial() throws SQLException {
		Connection conn = DataSourceFactory.getDataSource("database").getConnection();
		
		String sql = "select fchar from stt_test";
		List<Bean> list = SQLTemplate.selectForBeans(conn,sql ,  Bean.class,null);
		AssertKit.assertEqual(2, list.size());
		AssertKit.assertEqual("charvalue", list.get(0).getFchar());
		AssertKit.assertEqual("charvalue", list.get(1).getFchar());
		
		AssertKit.assertException(()->{
			SQLTemplate.selectForBeans(conn,"select fchar as fff from stt_test",Bean.class,null);
		});
		
		sql = "select * from stt_test";
		list = SQLTemplate.selectForBeans(conn,sql , Bean.class, null);
		AssertKit.assertEqual(2, list.size());
		AssertKit.assertEqual("charvalue", list.get(0).getFchar());
		AssertKit.assertEqual(123, list.get(0).getFint());
		System.out.println(JsonKit.object2Json(list));
		
		sql = "select ftimestamp ftimestampLong,ftimestamp ftimestampString  from stt_test";
		list = SQLTemplate.selectForBeans(conn,sql , Bean.class, null);
		AssertKit.assertEqual(2, list.size());
		System.out.println(JsonKit.object2Json(list));
		
		sql = "select ftime ftimeDate,ftime ftimeString,ftimestamp ftimestampLong,ftimestamp ftimestampString,fdate fdateString,fdate fdateLong  from stt_test";
		list = SQLTemplate.selectForBeans(conn,sql , Bean.class, null);
		AssertKit.assertEqual(2, list.size());
		System.out.println(JsonKit.object2Json(list));
		
		
	}

	private void testNormal() {
		// TODO Auto-generated method stub
		
	}

	private void initData() throws SQLException {
		Connection conn = DataSourceFactory.getDataSource("database").getConnection();
		
		try {
			SQLTemplate.executeDropSql(conn, "drop table stt_test");
		}catch(Exception e) {}
		
		String sql = "create table stt_test("
				+ " fvarchar varchar(20), "
				+ " fchar char(20), "
				+ " fint int,"
				+ " fdouble double,"
				+ " fdecimal decimal,"
				+ " fdate date,"
				+ " ftimestamp timestamp,"
				+ " ftime time)";
		System.out.println(sql);
		SQLTemplate.executeCreateSql(conn, sql);
		
		SQLTemplate.executeInsertSql(conn, "insert into stt_test values(?,?,?,?,?,?,?,?)", new Object[] {
				"varcharvalue",
				"charvalue",
				123,
				Double.valueOf("123.123"),
				Double.valueOf("123.123"),
				CalenderKit.getDateFromString("20220202"),
				new java.sql.Timestamp(CalenderKit.getTimeFromString("20220202101010").getTime()),
				"11:11:11"
		});
		
		SQLTemplate.executeInsertSql(conn, "insert into stt_test values(?,?,?,?,?,?,?,?)", new Object[] {
				"varcharvalue",
				"charvalue",
				123,
				Double.valueOf("123.123"),
				Double.valueOf("123.123"),
				CalenderKit.getDateFromString("20220202"),
				new java.sql.Timestamp(CalenderKit.getTimeFromString("20220202101010").getTime()),
				"11:11:11"
		});
		
		List<Map<String, String>> result = SQLTemplate.executeSelect(conn, "select * from stt_test", null);
		
		System.out.println(JsonKit.object2Json(result));
	}
	
	public static class Bean{
		String fvarchar;
		String fchar;
		int fint;
		double fdouble;
		float fdecimal;
		java.util.Date fdate;
		String fdateString;
		Long fdateLong;
		java.util.Date ftimestamp;
		String ftimestampString;
		long ftimestampLong;
		long ftime;
		String ftimeString;
		java.util.Date ftimeDate;
		public String getFvarchar() {
			return fvarchar;
		}
		public void setFvarchar(String fvarchar) {
			this.fvarchar = fvarchar;
		}
		public String getFchar() {
			return fchar;
		}
		public void setFchar(String fchar) {
			this.fchar = fchar;
		}
		public int getFint() {
			return fint;
		}
		public void setFint(int fint) {
			this.fint = fint;
		}
		public double getFdouble() {
			return fdouble;
		}
		public void setFdouble(double fdouble) {
			this.fdouble = fdouble;
		}
		public float getFdecimal() {
			return fdecimal;
		}
		public void setFdecimal(float fdecimal) {
			this.fdecimal = fdecimal;
		}
		public java.util.Date getFdate() {
			return fdate;
		}
		public void setFdate(java.util.Date fdate) {
			this.fdate = fdate;
		}
		public String getFdateString() {
			return fdateString;
		}
		public void setFdateString(String fdateString) {
			this.fdateString = fdateString;
		}
		public Long getFdateLong() {
			return fdateLong;
		}
		public void setFdateLong(Long fdateLong) {
			this.fdateLong = fdateLong;
		}
		public java.util.Date getFtimestamp() {
			return ftimestamp;
		}
		public void setFtimestamp(java.util.Date ftimestamp) {
			this.ftimestamp = ftimestamp;
		}
		public String getFtimestampString() {
			return ftimestampString;
		}
		public void setFtimestampString(String ftimestampString) {
			this.ftimestampString = ftimestampString;
		}
		public long getFtimestampLong() {
			return ftimestampLong;
		}
		public void setFtimestampLong(long ftimestampLong) {
			this.ftimestampLong = ftimestampLong;
		}
		public long getFtime() {
			return ftime;
		}
		public void setFtime(long ftime) {
			this.ftime = ftime;
		}
		public String getFtimeString() {
			return ftimeString;
		}
		public void setFtimeString(String ftimeString) {
			this.ftimeString = ftimeString;
		}
		public java.util.Date getFtimeDate() {
			return ftimeDate;
		}
		public void setFtimeDate(java.util.Date ftimeDate) {
			this.ftimeDate = ftimeDate;
		}
		
		
	}
}
