package test.net.jplugin.core.das.mybatis.cachetest;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.jplugin.core.das.api.PageCond;

public interface ICacheMapper {

	public void add(@Param("id") int  id,@Param("name") String name);
	
	public List<TestCacheBean> queryWithPage(@Param("page") PageCond pc);
}
