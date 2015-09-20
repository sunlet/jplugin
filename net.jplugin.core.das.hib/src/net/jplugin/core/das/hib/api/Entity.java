package net.jplugin.core.das.hib.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	public String tableName() default "";
	public String idField() default "id";
	public String[] indexes() default "";
	public String idgen() default "native"; 
	public String textFields() default "";
	//native（交给数据库,根据数据库自动确定）|uuid.hex(uuid)|assigned|increment(内存，不支持多实例)|sequence(db2,ora)|identity（自增长，oracle不支持）
}
