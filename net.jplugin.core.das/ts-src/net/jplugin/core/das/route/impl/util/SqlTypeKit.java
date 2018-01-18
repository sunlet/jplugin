package net.jplugin.core.das.route.impl.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 
CHAR 		String
VARCHAR 	String
LONGVARCHAR  String
NUMERIC  	java.math.BigDecimal
DECIMAL      java.math.BigDecimal  
BIT  		boolean
TINYINT  	byte
SMALLINT  	short
INTEGER   	int
BIGINT     long
REAL     	float
FLOAT    	double
DOUBLE    	double
BINARY    	byte[]
VARBINARY  	byte[]
LONGVARBINARY  byte[]
DATE  		java.sql.Date
TIME 		java.sql.Time
TIMESTAMP 	java.sql.Timestamp
 * 
 * </pre>
 * 
 * @author LiuHang
 *
 */
public class SqlTypeKit {
	
	public static Transformer<Object> service = new CommonTransformer();
	
	private static Map<Class, Transformer> transformers = new HashMap<Class, Transformer>();

	public static  <T> T get(Object obj,Class<T> c) throws SQLException{
		Transformer t = transformers.get(obj.getClass());
		if (t == null)
			throw new SQLException("can't find transformer for type:" + obj.getClass());
		if (c==String.class)
			return (T) t.getString(obj);
		if (c==Boolean.class)
			return (T) t.getBoolean(obj);
		if (c==Byte.class || c==byte.class)
			return (T) t.getByte(obj);
		if (c==Short.class || c==short.class)
			return (T) t.getShort(obj);
		if (c==Integer.class || c==int.class)
			return (T)t.getInt(obj);
		if (c==Long.class || c==long.class)
			return (T)t.getLong(obj);
		if (c==Float.class || c==float.class)
			return (T)t.getFloat(obj);
		if (c==Double.class || c==double.class)
			return (T)t.getDouble(obj);
		if (c==BigDecimal.class)
			return (T)t.getBigDecimal(obj);
		if (c==byte[].class)
			return (T)t.getBytes(obj);
		if (c==Date.class)
			return (T)t.getDate(obj);
		if (c==Time.class)
			return (T)t.getTime(obj);
		if (c==Timestamp.class)
			return (T)t.getTimestamp(obj);
		if (c==InputStream.class)
			return (T)t.getAsciiStream(obj);
		throw new SQLException("not supported class:"+c.getName());
	}

	private static class CommonTransformer implements Transformer<Object> {

		Transformer get(Object o) throws SQLException {
			Transformer t = transformers.get(o.getClass());
			if (t == null)
				throw new SQLException("can't find transformer for type:" + o.getClass());
			else
				return t;
		}

		@Override
		public String getString(Object o) throws SQLException {
			return get(o).getString(o);
		}

		@Override
		public Boolean getBoolean(Object o) throws SQLException {
			return get(o).getBoolean(o);
		}

		@Override
		public Byte getByte(Object o) throws SQLException {
			return get(o).getByte(o);
		}

		@Override
		public Short getShort(Object o) throws SQLException {
			return get(o).getShort(o);
		}

		@Override
		public Integer getInt(Object o) throws SQLException {
			return get(o).getInt(o);
		}

		@Override
		public Long getLong(Object o) throws SQLException {
			return get(o).getLong(o);
		}

		@Override
		public Float getFloat(Object o) throws SQLException {
			return get(o).getFloat(o);
		}

		@Override
		public Double getDouble(Object o) throws SQLException {
			return get(o).getDouble(o);
		}

		@Override
		public BigDecimal getBigDecimal(Object o) throws SQLException {
			return get(o).getBigDecimal(o);
		}

		@Override
		public byte[] getBytes(Object o) throws SQLException {
			return get(o).getBytes(o);
		}

		@Override
		public Date getDate(Object o) throws SQLException {
			return get(o).getDate(o);
		}

		@Override
		public Time getTime(Object o) throws SQLException {
			return get(o).getTime(o);
		}

		@Override
		public Timestamp getTimestamp(Object o) throws SQLException {
			return get(o).getTimestamp(o);
		}

		@Override
		public InputStream getAsciiStream(Object o) throws SQLException {
			return get(o).getAsciiStream(o);
		}

		@Override
		public InputStream getUnicodeStream(Object o) throws SQLException {
			return get(o).getUnicodeStream(o);
		}

		@Override
		public InputStream getBinaryStream(Object o) throws SQLException {
			return get(o).getBinaryStream(o);
		}
	}
	

	static {
		transformers.put(Timestamp.class, new FromTimestampTransformer());
		transformers.put(Date.class, new FromDateTransformer());
		transformers.put(Time.class, new FromTimeTransformer());
		transformers.put(Time.class, new FromTimeTransformer());
		transformers.put(byte[].class, new FromBytesTransformer());
		transformers.put(double.class, new FromDoubleTransformer());
		transformers.put(Double.class, new FromDoubleTransformer());
		transformers.put(Float.class, new FromFloatTransformer());
		transformers.put(float.class, new FromFloatTransformer());
		transformers.put(long.class, new FromLongTransformer());
		transformers.put(Long.class, new FromLongTransformer());
		transformers.put(int.class, new FromIntTransformer());
		transformers.put(Integer.class, new FromIntTransformer());
		transformers.put(short.class, new FromShortTransformer());
		transformers.put(Short.class, new FromShortTransformer());
		transformers.put(byte.class, new FromByteTransformer());
		transformers.put(Byte.class, new FromByteTransformer());
		transformers.put(Boolean.class, new FromBooleanTransformer());
		transformers.put(boolean.class, new FromBooleanTransformer());
		transformers.put(String.class, new FromStringTransformer());
		transformers.put(BigDecimal.class, new FromBigDecimalTransformer());
	}

	private static class FromTimestampTransformer extends BasicTimeTransformer<Timestamp> {
		@Override
		public Date getDate(Timestamp o) throws SQLException {
			return new Date(o.getTime());
		}

		@Override
		public Time getTime(Timestamp o) throws SQLException {
			return new Time(o.getTime());
		}

		@Override
		public Timestamp getTimestamp(Timestamp o) throws SQLException {
			return o;
		}
	}

	private static class FromTimeTransformer extends BasicTimeTransformer<Time> {

		@Override
		public Date getDate(Time o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Time getTime(Time o) throws SQLException {
			return o;
		}

		@Override
		public Timestamp getTimestamp(Time o) throws SQLException {
			throw new RuntimeException("not support");
		}

	}

	private static class FromDateTransformer extends BasicTimeTransformer<Date> {

		@Override
		public Date getDate(Date o) throws SQLException {
			return o;
		}

		@Override
		public Time getTime(Date o) throws SQLException {
			return new Time(o.getTime());
		}

		@Override
		public Timestamp getTimestamp(Date o) throws SQLException {
			return new Timestamp(o.getTime());
		}

	}

	private static class FromBytesTransformer implements Transformer<byte[]> {

		@Override
		public String getString(byte[] o) throws SQLException {
			return new String(o);
		}

		@Override
		public Boolean getBoolean(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Byte getByte(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Short getShort(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Integer getInt(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Long getLong(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Float getFloat(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Double getDouble(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public BigDecimal getBigDecimal(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public byte[] getBytes(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Date getDate(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Time getTime(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public Timestamp getTimestamp(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public InputStream getAsciiStream(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public InputStream getUnicodeStream(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

		@Override
		public InputStream getBinaryStream(byte[] o) throws SQLException {
			throw new RuntimeException("not support");
		}

	}

	private static class FromBigDecimalTransformer extends BasicNumicaTransformer<BigDecimal> {
		@Override
		public BigDecimal getBigDecimal(BigDecimal o) throws SQLException {
			return o;
		}
	}

	private static class FromDoubleTransformer extends BasicNumicaTransformer<Double> {

		@Override
		public BigDecimal getBigDecimal(Double o) throws SQLException {
			return new BigDecimal(o);
		}

	}

	private static class FromFloatTransformer extends BasicNumicaTransformer<Float> {

		@Override
		public BigDecimal getBigDecimal(Float o) throws SQLException {
			return new BigDecimal(o);
		}

	}

	private static class FromLongTransformer extends BasicNumicaTransformer<Long> {

		@Override
		public BigDecimal getBigDecimal(Long o) throws SQLException {
			return new BigDecimal(o);
		}

	}

	static class FromIntTransformer extends BasicNumicaTransformer<Integer> {

		@Override
		public BigDecimal getBigDecimal(Integer o) throws SQLException {
			return new BigDecimal(o);
		}

	}

	static class FromByteTransformer extends BasicNumicaTransformer<Byte> {

		@Override
		public BigDecimal getBigDecimal(Byte o) throws SQLException {
			return new BigDecimal(o);
		}

	}

	static class FromShortTransformer extends BasicNumicaTransformer<Byte> {
		@Override
		public BigDecimal getBigDecimal(Byte o) throws SQLException {
			return new BigDecimal(o);
		}
	}

	private static class FromBooleanTransformer implements Transformer<Boolean> {

		@Override
		public String getString(Boolean o) throws SQLException {
			return o.toString();
		}

		@Override
		public Boolean getBoolean(Boolean o) throws SQLException {
			return o;
		}

		@Override
		public Byte getByte(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Short getShort(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Integer getInt(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Long getLong(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Float getFloat(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Double getDouble(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public BigDecimal getBigDecimal(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public byte[] getBytes(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Date getDate(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Time getTime(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public Timestamp getTimestamp(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public InputStream getAsciiStream(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public InputStream getUnicodeStream(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

		@Override
		public InputStream getBinaryStream(Boolean o) throws SQLException {
			throw new SQLException("not support.");
		}

	}

	private static class FromStringTransformer implements Transformer<String> {

		@Override
		public String getString(String o) throws SQLException {
			return o;
		}

		@Override
		public Boolean getBoolean(String o) throws SQLException {
			return "true".equalsIgnoreCase(o);
		}

		@Override
		public Byte getByte(String o) throws SQLException {
			throw new SQLException("not support transformer ." + o);
		}

		@Override
		public Short getShort(String o) throws SQLException {
			return Short.parseShort(o);
		}

		@Override
		public Integer getInt(String o) throws SQLException {
			return Integer.parseInt(o);
		}

		@Override
		public Long getLong(String o) throws SQLException {
			return Long.parseLong(o);
		}

		@Override
		public Float getFloat(String o) throws SQLException {
			return Float.parseFloat(o);
		}

		@Override
		public Double getDouble(String o) throws SQLException {
			return Double.parseDouble(o);
		}

		@Override
		public BigDecimal getBigDecimal(String o) throws SQLException {
			return BigDecimal.valueOf(Double.parseDouble(o));
		}

		@Override
		public byte[] getBytes(String o) throws SQLException {
			return o.getBytes();
		}

		@Override
		public Date getDate(String o) throws SQLException {
			throw new SQLException("not support transformer ." + o);
		}

		@Override
		public Time getTime(String o) throws SQLException {
			throw new SQLException("not support transformer ." + o);
		}

		@Override
		public Timestamp getTimestamp(String o) throws SQLException {
			throw new SQLException("not support transformer ." + o);
		}

		@Override
		public InputStream getAsciiStream(String o) throws SQLException {
			try {
				return new ByteArrayInputStream(o.getBytes("ASCII"));
			} catch (UnsupportedEncodingException e) {
				throw new SQLException(e);
			}
		}

		@Override
		public InputStream getUnicodeStream(String o) throws SQLException {
			try {
				return new ByteArrayInputStream(o.getBytes("UTF-16"));
			} catch (UnsupportedEncodingException e) {
				throw new SQLException(e);
			}
		}

		@Override
		public InputStream getBinaryStream(String o) throws SQLException {
			return new ByteArrayInputStream(o.getBytes());
		}
	}

	private abstract static class BasicTimeTransformer<T> implements Transformer<T> {
		@Override
		public final String getString(T o) throws SQLException {
			return o.toString();
		}

		@Override
		public final Boolean getBoolean(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Byte getByte(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Short getShort(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Integer getInt(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Long getLong(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Float getFloat(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Double getDouble(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final BigDecimal getBigDecimal(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final byte[] getBytes(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getAsciiStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getUnicodeStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getBinaryStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

	}

	private abstract static class BasicNumicaTransformer<T extends Number> implements Transformer<T> {

		@Override
		public final String getString(T o) throws SQLException {
			return o.toString();
		}

		@Override
		public final Boolean getBoolean(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final byte[] getBytes(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Date getDate(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Time getTime(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Timestamp getTimestamp(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getAsciiStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getUnicodeStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final InputStream getBinaryStream(T o) throws SQLException {
			throw new SQLException("not support");
		}

		@Override
		public final Byte getByte(T o) throws SQLException {
			return o.byteValue();
		}

		@Override
		public final Short getShort(T o) throws SQLException {
			return o.shortValue();
		}

		@Override
		public final Integer getInt(T o) throws SQLException {
			return o.intValue();
		}

		@Override
		public final Long getLong(T o) throws SQLException {
			return o.longValue();
		}

		@Override
		public final Float getFloat(T o) throws SQLException {
			return o.floatValue();
		}

		@Override
		public final Double getDouble(T o) throws SQLException {
			return o.doubleValue();
		}
	}
}

interface Transformer<T> {

	public String getString(T o) throws SQLException;

	public Boolean getBoolean(T o) throws SQLException;

	public Byte getByte(T o) throws SQLException;

	public Short getShort(T o) throws SQLException;

	public Integer getInt(T o) throws SQLException;

	public Long getLong(T o) throws SQLException;

	public Float getFloat(T o) throws SQLException;

	public Double getDouble(T o) throws SQLException;

	public BigDecimal getBigDecimal(T o) throws SQLException;

	public byte[] getBytes(T o) throws SQLException;

	public Date getDate(T o) throws SQLException;

	public Time getTime(T o) throws SQLException;

	public Timestamp getTimestamp(T o) throws SQLException;

	public InputStream getAsciiStream(T o) throws SQLException;

	public InputStream getUnicodeStream(T o) throws SQLException;

	public InputStream getBinaryStream(T o) throws SQLException;
}
