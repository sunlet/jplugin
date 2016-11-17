package net.jplugin.core.das.route.impl.conn;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.conn.mulqry.CombineStatementFactory;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser;
import net.jplugin.core.das.route.impl.util.CallableList;
import net.jplugin.core.das.route.impl.util.MyCallable;

public class RouterPrearedStatement extends RouterStatement  implements PreparedStatement{
	protected CallableList<PreparedStatement> list = new CallableList<PreparedStatement>();
	protected String sql;
	protected SqlParamRecoder recorder = new SqlParamRecoder();

	public static PreparedStatement create(String sql, RouterConnection compondConnection) {
		RouterPrearedStatement s = new RouterPrearedStatement();
		s.sql = sql;
		s.connection = compondConnection;
		return s;
	}

	@Override
	public boolean execute() throws SQLException {
		PreparedStatement stmt = genTargetStatement();
		boolean result = stmt.execute();
		return result;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		PreparedStatement stmt = genTargetStatement();
		ResultSet rs = stmt.executeQuery();
		return rs;
	}

	@Override
	public int executeUpdate() throws SQLException {
		PreparedStatement stmt = genTargetStatement();
		int cnt = stmt.executeUpdate();
		return cnt;

	}

	private PreparedStatement genTargetStatement() throws SQLException {
		if (sql == null)
			throw new TablesplitException("No sql found");
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle(connection, sql, recorder.getList());

		String targetDataSourceName = shr.getTargetDataSourceName();
		PreparedStatement stmt ;
		if (CombinedSqlParser.SPANALL_DATASOURCE.equals(targetDataSourceName)){
			stmt = CombineStatementFactory.createPrepared(this.connection);
		}else{
			DataSource tds = DataSourceFactory.getDataSource(targetDataSourceName);
			if (tds == null)
				throw new TablesplitException("Can't find target datasource." + targetDataSourceName);
			stmt = tds.getConnection().prepareStatement(shr.getResultSql());
		}
		executeResult.set(stmt);
		list.executeWith(stmt);
		return stmt;
	}


	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNull(parameterIndex, sqlType);
			}
		});
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBoolean(parameterIndex, x);
			}
		});
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setByte(parameterIndex, x);
			}
		});
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setShort(parameterIndex, x);
			}
		});

	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setInt(parameterIndex, x);
			}
		});

	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setLong(parameterIndex, x);
			}
		});
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setFloat(parameterIndex, x);
			}
		});
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setDouble(parameterIndex, x);
			}
		});

	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBigDecimal(parameterIndex, x);
			}
		});
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setString(parameterIndex, x);
			}
		});

	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBytes(parameterIndex, x);

			}
		});
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setDate(parameterIndex, x);
			}
		});

	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setTime(parameterIndex, x);
			}
		});
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setTimestamp(parameterIndex, x);
			}
		});

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setAsciiStream(parameterIndex, x, length);
			}
		});
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setUnicodeStream(parameterIndex, x, length);
			}
		});
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBinaryStream(parameterIndex, x, length);
			}
		});
	}

	@Override
	public void clearParameters() throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.clearParameters();
			}
		});
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setObject(parameterIndex, x, targetSqlType);
			}
		});

	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setObject(parameterIndex, x);
			}
		});
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setCharacterStream(parameterIndex, reader, length);
			}
		});
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setRef(parameterIndex, x);

			}
		});
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBlob(parameterIndex, x);

			}
		});
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setClob(parameterIndex, x);
			}
		});
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setArray(parameterIndex, x);
			}
		});
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setDate(parameterIndex, x, cal);
			}
		});
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setTime(parameterIndex, x, cal);
			}
		});
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setTimestamp(parameterIndex, x, cal);
			}
		});
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNull(parameterIndex, sqlType, typeName);
			}
		});

	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setURL(parameterIndex, x);
			}
		});
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		recorder.set(parameterIndex, value);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNString(parameterIndex, value);
			}
		});

	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNCharacterStream(parameterIndex, value, length);
			}
		});

	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNClob(parameterIndex, value);
			}
		});
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setClob(parameterIndex, reader, length);
			}
		});
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBlob(parameterIndex, inputStream, length);
			}
		});
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNClob(parameterIndex, reader, length);
			}
		});
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setSQLXML(parameterIndex, xmlObject);
			}
		});
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
		recorder.set(parameterIndex, x);
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
			}
		});
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setAsciiStream(parameterIndex, x, length);
			}
		});

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBinaryStream(parameterIndex, x, length);
			}
		});

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setCharacterStream(parameterIndex, reader, length);
			}
		});

	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setAsciiStream(parameterIndex, x);
			}
		});

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBinaryStream(parameterIndex, x);
			}
		});
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {
			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setCharacterStream(parameterIndex, reader);
			}
		});
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNCharacterStream(parameterIndex, value);
			}
		});

	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setClob(parameterIndex, reader);

			}
		});
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setBlob(parameterIndex, inputStream);
			}
		});

	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		list.add(new MyCallable<PreparedStatement>() {

			@Override
			public void call(PreparedStatement t) throws SQLException {
				t.setNClob(parameterIndex, reader);
			}
		});
	}

	
	@Override
	public void addBatch() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new RuntimeException("not support");
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new RuntimeException("not support");
	}

}
