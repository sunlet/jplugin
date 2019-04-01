package net.luis.main.pojo;

import java.util.List;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-18 下午04:30:54
 **/

public interface IDataTest {
	@Rule(methodType=TxType.REQUIRED)
	public void testTx(UserBean u);
	
	@Rule
	public void testNoTx();
	
	@Rule(methodType=TxType.REQUIRED)
	public void testPropertyType();
}
