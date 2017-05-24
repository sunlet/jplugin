package net.jplugin.ext.dict.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.jplugin.ext.dict.api.Dictionary;
import net.jplugin.ext.dict.api.IDictProvidor;
import net.jplugin.ext.dict.api.IDictionaryService;

public class DictionaryServiceImpl implements IDictionaryService {
	Map<String , IDictProvidor> map=new HashMap<String, IDictProvidor>();
	public void init(Map<String , IDictProvidor> amap){
		map.putAll(amap);
	}

	ConcurrentHashMap<String, Map<String,List<Dictionary>>> dataCache=new ConcurrentHashMap<String, Map<String,List<Dictionary>>>();
	@Override
	public List<Dictionary> getDictionarys(String provider, String param) {
		if (param==null) param="";//以便可以作为key
		
		IDictProvidor dictProd = map.get(provider);
		if (dictProd.dynamic())
			return dictProd.get(param);
		else{
			Map<String, List<Dictionary>> temp = dataCache.get(provider);
			if (temp==null) {
				synchronized (dataCache) {
					temp = dataCache.get(provider);
					if (temp==null){
						temp = new HashMap<String, List<Dictionary>>();
						dataCache.put(provider, temp);
					}
				}
				
			}
			List<Dictionary> ret = temp.get(param);
			if (ret==null) {
				synchronized (temp) {
					ret = temp.get(param);
					if (ret==null){
						ret = dictProd.get(param);
						temp.put(param, ret);
					}
				}
			}
			return ret;
		}
	}
	@Override
	public String getDictionaryLabel(String name, String param, String value) {
		List<Dictionary> list = getDictionarys(name,param);
		for (Dictionary d:list){
			if (value.equals(d.getValue())) return d.getLabel();
		}
		return value;
	}

}
