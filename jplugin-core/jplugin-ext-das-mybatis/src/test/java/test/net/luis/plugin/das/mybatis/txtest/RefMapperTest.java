package test.net.luis.plugin.das.mybatis.txtest;

import net.jplugin.core.das.mybatis.api.RefMapper;
import net.jplugin.core.kernel.api.RefAnnotationSupport;

public class RefMapperTest extends RefAnnotationSupport{
	
	@RefMapper(dataSource="testdb")
	ITxTestDB1Mapper mapper;
	
	public void test(){
		mapper.add("a1", "b1");
	}
}
