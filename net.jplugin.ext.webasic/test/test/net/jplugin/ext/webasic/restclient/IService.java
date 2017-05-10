package test.net.jplugin.ext.webasic.restclient;

import java.util.List;
import java.util.Map;

import net.jplugin.ext.webasic.api.Para;
import test.net.jplugin.ext.webasic.restclient.ServiceBean.Bean;

public interface IService {

	int add(int a, int b);

	/**
	 * 2017-5-10 增加MIX参数模式，不需要annotation了
	 */
	String addString(String a,  String b);
	
	String addStringDefault(String a, @Para(name="b",required=false) String b);

	List<Bean> getBeanList();

	Map<String, Bean> getBeanMap();
	
	public void ex();
	
	public  void remoteEx();
	
	public void indirectEx();

}