package net.jplugin.core.das.mybatis.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.jplugin.core.das.api.DataSourceFactory;

@Retention(RetentionPolicy.RUNTIME)
public @interface RefMybatisService {
	public String dataSource() default DataSourceFactory.DATABASE_DSKEY;
}
