package net.luis.main.testrule;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.core.ctx.api.RuleServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-12 下午06:21:00
 **/

public class Rule1Impl implements Rule1 {

	/* (non-Javadoc)
	 * @see net.luis.main.testrule.Rule1#getUser(long)
	 */
	public User getUser(long userid) {
		System.out.println("in getUser");
		return new User();
	}

	/* (non-Javadoc)
	 * @see net.luis.main.testrule.Rule1#listUser(java.lang.String)
	 */
	public List<User> listUser(String condition) {
		System.out.println("in listUser");
		List l = new ArrayList<User>();
		l.add(new User());
		return l;
	}

	/* (non-Javadoc)
	 * @see net.luis.main.testrule.Rule1#update()
	 */
	public void update() {
		System.out.println("in update");
	}
	
	/* (non-Javadoc)
	 * @see net.luis.main.testrule.Rule1#update()
	 */
	public void updateEx() {
		System.out.println("in updateEx");
		throw new RuntimeException();
	}	
	
	/* (non-Javadoc)
	 * @see net.luis.main.testrule.Rule1#update()
	 */
	public void loopCallOk() {
		System.out.println("in loopCallOk");
		RuleServiceFactory.getRuleService(Rule1.class).update();
	}	
	
	public void loopCallError() {
		System.out.println("in loopCallError");
		RuleServiceFactory.getRuleService(Rule1.class).updateEx();
	}	

}
