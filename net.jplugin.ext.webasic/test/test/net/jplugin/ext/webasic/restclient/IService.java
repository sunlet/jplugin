package test.net.jplugin.ext.webasic.restclient;

import java.util.List;
import java.util.Map;

import net.jplugin.ext.webasic.api.Para;
import test.net.jplugin.ext.webasic.restclient.RestServiceBean.Bean;

public interface IService {

	int add(int a, int b);

	String addString(String a, @Para(name="b") String b);

	List<Bean> getBeanList();

	Map<String, Bean> getBeanMap();

}