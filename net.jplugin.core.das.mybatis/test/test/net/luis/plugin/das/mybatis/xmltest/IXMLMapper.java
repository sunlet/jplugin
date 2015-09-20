package test.net.luis.plugin.das.mybatis.xmltest;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import test.net.jplugin.core.das.mybatis.MybtestBean;

public interface IXMLMapper {
	List<MybtestBean> select();

	public void add(@Param("f1") String f1, @Param("f2") String f2);
	
	void clear();

}
