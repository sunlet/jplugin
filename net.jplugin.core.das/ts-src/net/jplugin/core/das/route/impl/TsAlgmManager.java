package net.jplugin.core.das.route.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import net.jplugin.core.das.route.Plugin;
import net.jplugin.core.das.route.api.RouterDataSource;
import net.jplugin.core.das.route.api.RouterException;
import net.jplugin.core.das.route.api.DataSourceInfo;
import net.jplugin.core.das.route.api.ITsAlgorithm;
import net.jplugin.core.das.route.api.ITsAlgorithm.Result;
import net.jplugin.core.das.route.api.ITsAlgorithm.ValueType;
import net.jplugin.core.das.route.api.RouterKeyFilter;
import net.jplugin.core.das.route.api.RouterKeyFilter.Operator;
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

	public static DataSourceInfo[] getMultiResults(RouterDataSource dataSource, String tableName,RouterKeyFilter kva) {
		TableConfig tc = dataSource.getConfig().findTableConfig(tableName);
		ITsAlgorithm algm = algmMap.get(tc.getSplitAlgm());
		
		
		//必要时转换类型，同时各值的类型一致性
		//注意，当kva没有数据，或者数据为null时，返回的type为null
		ValueType type = checkAndConvertKeyValue(kva);
		
		
		if (algm==null)
			throw new TablesplitException("error algm:"+tc.getSplitAlgm()+" for table:"+tableName);
		return algm.getMultiResults(dataSource,tableName,type,kva);
	}

	private static ValueType checkAndConvertKeyValue(RouterKeyFilter kva) {
		Object[] vals = kva.getConstValue();
		ValueType type = null;
		if (vals!=null){
			for (int i=0;i<vals.length;i++){
				Object o = vals[i];
				//null值不做判断和处理
				if (o!=null){
					KeyTypeValueRespect r = convertValueRespect(o);
					vals[i] = r.value;
					if (type!=null){
						if (!type.equals(r.valueType))
							throw new RouterException("All the values in the sql must be same type. "+type+" "+r.valueType);
						else
							//这是正常的
							;
					}else{
						type = r.valueType;
					}
				}
			}
		}
		return type;
	}

	public static DataSourceInfo[] getDataSourceInfos(RouterDataSource dataSource, String tableName) {
		return getMultiResults(dataSource,tableName,new RouterKeyFilter(net.jplugin.core.das.route.api.RouterKeyFilter.Operator.ALL ,null));
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
