package net.jplugin.core.kernel.api;

import java.util.List;
import java.util.Map;


/**
*
* @author: LiuHang
* @version 创建时间：2015-2-22 上午11:46:53
**/

public interface IPlugin {
	public final static int STAT_INIT = 0;
	public final static int STAT_LOADED=1;
	public final static int STAT_ERROR =-1;
	
	public String getName();
	public int getPrivority();
	public List<ExtensionPoint> getExtensionPoints();
	public List<Extension>  getExtensions();
	public int getStatus();
	public void onCreateServices();
	public void init();
	public void onDestroy();
	public Map<String,String> getConfigures();
}
