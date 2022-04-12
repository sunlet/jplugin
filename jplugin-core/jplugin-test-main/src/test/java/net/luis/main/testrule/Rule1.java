package net.luis.main.testrule;

import java.util.List;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.ctx.api.Rule.TxType;

/**
 * 
 * @author: LiuHang
 * @version 创建时间：2015-3-12 下午06:19:10
 **/

public interface Rule1 {

	@Rule
	public User getUser(long userid);

	@Rule
	public List<User> listUser(String condition);

	@Rule(methodType = TxType.REQUIRED)
	public void update();

	@Rule(methodType = TxType.REQUIRED)
	public void updateEx();

	@Rule(methodType = TxType.REQUIRED)
	public void loopCallOk();
	
	@Rule(methodType = TxType.REQUIRED)
	public void loopCallError();

}
