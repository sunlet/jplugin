package net.jplugin.ext.webasic.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-3 上午11:34:19
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Para {
	public String name();
	public boolean required() default false;
}
