package net.jplugin.ext.dict.api;

import java.util.List;

public interface IDictProvidor {
	/**
	 * 是否使用缓存
	 * @return
	 */
	public boolean dynamic();
	/**
	 * 注意：dynamic==true，才可以传递param
	 * @param param
	 * @return
	 */
	public List<Dictionary> get(String param);

}
