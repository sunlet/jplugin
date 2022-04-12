package net.jplugin.core.das.mybatis.impl;

import java.lang.reflect.Field;
import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.mybatis.api.MyBatisServiceFactory;
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
	public Object getValue(Object theObject, Class fieldType,RefMapper anno) {
		String nm = anno.dataSource();
		if (StringKit.isNull(nm)){
			//如果只匹配到一个，则用这个datasource
			//如果匹配到多个，报错
			//如果匹配不到，报错
			List<String> dss = MyBatisServiceFactory.findContainerDataSources(fieldType);
			if (dss.size()==1){
				nm = dss.get(0);
			}else if (dss.size()>1){
				throw new RuntimeException(dss.size() +" datasources found for mapper:"+fieldType.getName()+", You must specify one. class=" +theObject.getClass().getName());
			}else{
				throw new RuntimeException(" No datasource found for mapper:"+fieldType.getName()+", You must specify one. class=" +theObject.getClass().getName());
			}
		}
		
		return MapperProxyFactory.getMapper(nm, fieldType);
	}

}
