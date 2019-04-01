package net.luis.main.event;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-9 下午06:00:22
 **/

public interface ITestEventService {
	@Rule (methodType = TxType.REQUIRED)
	public void test();
}
