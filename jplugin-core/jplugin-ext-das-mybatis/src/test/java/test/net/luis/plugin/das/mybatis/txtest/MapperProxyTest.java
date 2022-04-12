package test.net.luis.plugin.das.mybatis.txtest;

import net.jplugin.core.das.mybatis.impl.MapperProxyFactory;

public class MapperProxyTest {
	public void test(){
		ITxTestDB1Mapper mapper = (ITxTestDB1Mapper) MapperProxyFactory.getMapper("testdb", ITxTestDB1Mapper.class);
		mapper.add("a1", "b1");
	}
}
