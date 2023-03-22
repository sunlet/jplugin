package jplugincoretest.mybastic;

import net.jplugin.core.das.mybatis.api.BindMapper;

//有了这一句才能让getSqlSession（）不为空。
@BindMapper(dataSource = "database")
public interface TestMapper {
}
