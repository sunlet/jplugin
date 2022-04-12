package net.jplugin.mtenant.impl.kit.parse;

import java.util.List;
import java.util.Map;

public interface SqlParser {
	public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables);

}
