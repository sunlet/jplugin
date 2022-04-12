package test.net.luis.plugin.das.mybatis.txtest;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import test.net.jplugin.core.das.mybatis.MybtestBean;

public interface ITxTestDB1Mapper {
	@Select("SELECT * FROM TxTestDB1")
	List<MybtestBean> select();

	@Insert("INSERT INTO TxTestDB1(f1,f2) values( #{f1},#{f2})")
	public void add(@Param("f1") String f1, @Param("f2") String f2);
	
	@Delete("DELETE FROM TxTestDB1")
	void clear();
}
