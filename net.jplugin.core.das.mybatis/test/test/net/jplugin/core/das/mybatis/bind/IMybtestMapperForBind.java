package test.net.jplugin.core.das.mybatis.bind;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import net.jplugin.core.das.mybatis.api.BindMapper;
import test.net.jplugin.core.das.mybatis.MybtestBean;

@BindMapper
public interface IMybtestMapperForBind {
	@Select("SELECT * FROM mybtest")
	List<MybtestBean> select();

	@Insert("INSERT INTO mybtest(f1,f2) values( #{f1},#{f2})")
	public void add(@Param("f1") String f1, @Param("f2") String f2);
	
	@Delete("DELETE FROM mybtest")
	void clear();

}
