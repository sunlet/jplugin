package net.jplugin.core.das.dds.kits;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

public abstract class EmptyQueryableResultSet implements ResultSet {

	@Override
	public final <T> T unwrap(Class<T> iface) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new RuntimeException("not impl");
	}


	@Override
	public final boolean rowUpdated() throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final boolean rowInserted() throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final boolean rowDeleted() throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateNull(int columnIndex) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBoolean(int columnIndex, boolean x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateByte(int columnIndex, byte x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateShort(int columnIndex, short x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateInt(int columnIndex, int x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateLong(int columnIndex, long x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateFloat(int columnIndex, float x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateDouble(int columnIndex, double x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateString(int columnIndex, String x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBytes(int columnIndex, byte[] x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateDate(int columnIndex, Date x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateTime(int columnIndex, Time x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateObject(int columnIndex, Object x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateNull(String columnLabel) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBoolean(String columnLabel, boolean x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateByte(String columnLabel, byte x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateShort(String columnLabel, short x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateInt(String columnLabel, int x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateLong(String columnLabel, long x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateFloat(String columnLabel, float x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateDouble(String columnLabel, double x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateString(String columnLabel, String x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateBytes(String columnLabel, byte[] x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateDate(String columnLabel, Date x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateTime(String columnLabel, Time x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateObject(String columnLabel, Object x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void insertRow() throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateRow() throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void deleteRow() throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void cancelRowUpdates() throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void moveToInsertRow() throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void moveToCurrentRow() throws SQLException {
		throw new RuntimeException("not impl");
	}



	@Override
	public final void updateRef(int columnIndex, Ref x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateRef(String columnLabel, Ref x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(int columnIndex, Blob x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(String columnLabel, Blob x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(int columnIndex, Clob x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(String columnLabel, Clob x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateArray(int columnIndex, Array x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateArray(String columnLabel, Array x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateRowId(int columnIndex, RowId x) throws SQLException {
		throw new RuntimeException("not impl");
	}

	@Override
	public final void updateRowId(String columnLabel, RowId x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNString(int columnIndex, String nString) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNString(String columnLabel, String nString) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		throw new RuntimeException("not impl");

	}

	

	@Override
	public final void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		throw new RuntimeException("not impl");

	}


	@Override
	public final void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(int columnIndex, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

	@Override
	public final void updateNClob(String columnLabel, Reader reader) throws SQLException {
		throw new RuntimeException("not impl");

	}

}
