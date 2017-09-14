package net.jplugin.core.das.mybatis.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.jplugin.core.das.api.DataSourceFactory;

@Retention(RetentionPolicy.RUNTIME)
public @interface BindMapper {
	public String dataSource() default DataSourceFactory.DATABASE_DSKEY;
}