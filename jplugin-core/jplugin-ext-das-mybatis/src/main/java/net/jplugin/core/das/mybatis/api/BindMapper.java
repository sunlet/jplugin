package net.jplugin.core.das.mybatis.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.jplugin.core.das.api.DataSourceFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(BindMapperSet.class)
public @interface BindMapper {
	public String dataSource() default DataSourceFactory.DATABASE_DSKEY;
}