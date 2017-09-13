package net.jplugin.core.das.mybatis.impl;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.api.RefMapper;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;

public class MybatisMapperAnnoHandler implements IAnnoForAttrHandler<RefMapper> {

	@Override
	public Class<RefMapper> getAnnoClass() {
		return RefMapper.class;
	}

	@Override
	public Class getAttrClass() {
		return Object.class;
	}

	@Override
	public Object getValue(Object theObject, Class fieldType, RefMapper anno) {
		String nm = anno.dataSource();
		if (nm == null)
			nm = DataSourceFactory.DATABASE_DSKEY;
		return MapperProxyFactory.getMapper(nm, fieldType);
	}

}
