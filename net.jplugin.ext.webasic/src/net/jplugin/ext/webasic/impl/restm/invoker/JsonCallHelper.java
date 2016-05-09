package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.List;
import java.util.Map;

import javax.sql.rowset.JoinRowSet;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;

public class JsonCallHelper {

	public static void convertToHttp(CallParam cp) {
		String json = cp.getParamMap().get(CallParam.JSON_KEY);
		if (StringKit.isNull(json))
			throw new RuntimeException("json received is null.");
		
		Map map = JsonKit.json2Map(json);
		for (Object key : map.keySet()){
			if (!(key instanceof String)){
				throw new RuntimeException("the first level key must be String type."+json);
			}
			cp.getParamMap().put((String)key,JsonKit.object2JsonEx(map.get(key)));
		}
	}
	
	public static void main(String[] args) {
		String str = "{\"a\":\"a\",\"b\":{\"a\":\"a\",\"b\":\"b\"},\"c\":[1,2,3]}";
		Map map = JsonKit.json2Map(str);
		
		System.out.println(map.get("a"));
		Map m = (Map) map.get("b");
		List l = (List) map.get("c");
		
		System.out.println(m.get("b"));
		System.out.println(l.size());
	}

}
