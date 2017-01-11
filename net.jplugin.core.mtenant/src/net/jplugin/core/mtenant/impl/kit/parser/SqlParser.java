package net.jplugin.core.mtenant.impl.kit.parser;

import java.util.List;
import java.util.Map;

public interface SqlParser {

	public String parse(String sourceSql, Map<String, Object> params, List<String> ignoreTables);
}
