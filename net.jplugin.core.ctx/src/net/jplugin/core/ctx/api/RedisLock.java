package net.jplugin.core.ctx.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 例子：
 * 	@Rule (methodType=TxType.ANY,keyIndex=5,logIndexes={1,2,3})
 **/


@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
	public enum Conflict {
		WAIT,FAIL
	}
	public String type();
	public int  paramIndex();
	public Conflict conflict() default Conflict.WAIT;
	public int waitSecondsWhenConflict() default 180;
	public int timeOutSeconds() default 180;
}


