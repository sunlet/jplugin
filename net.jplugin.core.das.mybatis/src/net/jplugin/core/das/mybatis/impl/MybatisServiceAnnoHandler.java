package net.jplugin.core.das.mybatis.impl;

import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
import net.jplugin.core.das.mybatis.api.RefMybatisService;
import net.jplugin.core.kernel.api.IAnnoForAttrHandler;

public class MybatisServiceAnnoHandler implements IAnnoForAttrHandler<RefMybatisService> {

	@Override
	public Class<IMybatisService> getAttrClass() {
		return IMybatisService.class;
	}

	@Override
	public Class getAnnoClass() {
		return RefMybatisService.class;
	}

	@Override
	public Object getValue(Object obj,Class fieldType, RefMybatisService anno) {
		return MyBatisServiceFactory.getService(anno.dataSource());
	}

}
