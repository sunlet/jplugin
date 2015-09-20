package net.jplugin.ext.dict.api;

import java.util.List;

public interface IDictionaryService {
	public List<Dictionary> getDictionarys(String name,String param);
	public String getDictionaryLabel(String provider, String param, String value);
}
