package net.jplugin.core.ctx.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 例子：
 * 	@Rule (methodType=TxType.ANY,keyIndex=5,logIndexes={1,2,3})
 × 
 * @author: LiuHang
 * @version 创建时间：2015-2-11 上午08:42:35
 **/


@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
	public static enum TxType{REQUIRED,ANY};

	public TxType methodType() default TxType.ANY;
	
	public int keyIndex() default -1;
	
	public int[] logIndexes()  default {};

	public String actionDesc() default "";
}

