package net.jplugin.core.das.route.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.das.route.Plugin;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.api.RouterDataSourceConfig.TableConfig;
import net.jplugin.core.kernel.api.PluginEnvirement;

public class TsAlgmManager {
	private static Map<String,ITsAlgorithm> algmMap=new HashMap<String,ITsAlgorithm>();
	
	private static ThreadLocal<KeyTypeValueRespect> valueRespect = new ThreadLocal<KeyTypeValueRespect>() {
		protected KeyTypeValueRespect initialValue() {
			return new KeyTypeValueRespect();
		};
	};
	
	public static DataSourceInfo[] getDataSourceInfos(RouterDataSource dataSource, String tableName) {
		TableConfig tc = dataSource.getConfig().findTableConfig(tableName);
		ITsAlgorithm algm = algmMap.get(tc.getSplitAlgm());
		if (algm==null)
			throw new TablesplitException("error algm:"+tc.getSplitAlgm()+" for table:"+tableName);
		return algm.getDataSourceInfos(dataSource,tableName);
	}
	
	public static ITsAlgorithm.Result getResult(RouterDataSource compondDataSource,String tbBaseName,Object key){
		TableConfig tc = compondDataSource.getConfig().findTableConfig(tbBaseName);
		ITsAlgorithm algm = algmMap.get(tc.getSplitAlgm());
		if (algm==null)
			throw new TablesplitException("error algm:"+tc.getSplitAlgm()+" for table:"+tbBaseName);
		KeyTypeValueRespect vr = convertValueRespect(key);
		Result result = algm.getResult(compondDataSource,tbBaseName,vr.getValueType(),vr.getValue());
		TableAutoCreation.tryCreate(tc,result,tbBaseName);
		return result;
	}
	
	private static KeyTypeValueRespect convertValueRespect(Object key) {
		KeyTypeValueRespect o = valueRespect.get();
		if (key instanceof String){
			o.setValue(key);
			o.setValueType(ValueType.STRING);
		}else if (key instanceof Integer){
			o.setValue(Long.valueOf(((Integer)key)));
			o.setValueType(ValueType.LONG);
		}else if (key instanceof Long){
			o.setValue((Long)key);
			o.setValueType(ValueType.LONG);
		}else if (key instanceof BigDecimal){
			o.setValue(((BigDecimal)key).longValue());
			o.setValueType(ValueType.LONG);
		}else if (key instanceof BigInteger){
			o.setValue(((BigInteger)key).longValue());
			o.setValueType(ValueType.LONG);
		}else if (key instanceof java.sql.Date){
			o.setValue(key);
			o.setValueType(ValueType.DATE);
		}else if (key instanceof java.sql.Timestamp){
			o.setValue(key);
			o.setValueType(ValueType.TIMESTAMP);
		}else throw new TablesplitException("not supported key value type:"+key+" "+key.getClass().getName());
		return o;
	}

	public static void init(){
		Map<String, Object> m = PluginEnvirement.getInstance().getExtensionMap(Plugin.EP_TS_ALGM);
		algmMap.putAll((Map<? extends String, ? extends ITsAlgorithm>) m);
	}

	public static boolean exists(String algm) {
		return algmMap.containsKey(algm);
	}
	
	static class KeyTypeValueRespect{
		ValueType valueType;
		Object value;
		public ValueType getValueType() {
			return valueType;
		}
		public void setValueType(ValueType valueType) {
			this.valueType = valueType;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}


}
