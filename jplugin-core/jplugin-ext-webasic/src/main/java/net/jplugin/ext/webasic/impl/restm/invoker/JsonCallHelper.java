package net.jplugin.ext.webasic.impl.restm.invoker;

import java.util.List;
import java.util.Map;

import javax.sql.rowset.JoinRowSet;

import net.jplugin.common.kits.JsonKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;

public class JsonCallHelper {

	public static void convertToHttp(CallParam cp) {
		//因为Value已经Merge到ParamContent当中，所以这里直接从ParamContent获取即可。
		Map<String, String> map = ThreadLocalContextManager.getRequestInfo().getContent().getParamContent();
		cp.getParamMap().clear();
		cp.getParamMap().putAll(map);

		
//		String json = cp.getParamMap().get(CallParam.JSON_KEY);
//		if (StringKit.isNull(json))
//			return;//不做任何转换，认为没参数
//		
//		Map map = JsonKit.json2Map(json);
//		for (Object key : map.keySet()){
//			if (!(key instanceof String)){
//				throw new RuntimeException("the first level key must be String type."+json);
//			}
//			cp.getParamMap().put((String)key,JsonKit.object2JsonEx(map.get(key)));
//		}
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
