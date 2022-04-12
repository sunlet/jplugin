package test.net.jplugin.core.das.mybatis.ts;

import org.apache.ibatis.annotations.Param;

public interface TbRoute0Mapper {
	public void add(@Param("f1") String f1,@Param("f2")int f2,@Param("f3") String f3);
	
	public TbRoute0 find(@Param("f1") String f1);
	
	public void update(TbRoute0 r);
	
	public void delete(@Param("f1") String f1);
}
