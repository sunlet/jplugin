package net.luis.main.pojo;

import java.util.List;

import javax.management.RuntimeErrorException;

import net.jplugin.core.das.api.ExtCond;
import net.jplugin.core.das.api.PageCond;
import net.jplugin.core.das.hib.api.IDataService;
import net.jplugin.core.service.api.ServiceFactory;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-3-18 下午04:31:03
 **/

public class DataTestImpl implements IDataTest {

	public void testNoTx() {
	}

	public void testTx(UserBean a) {
		IDataService das = ServiceFactory.getService(IDataService.class);
		
		das.executeDeleteSql("delete from luisuserbean",null);
		
		UserBean u = null;
		for (int i=0;i<5;i++){
			u = new UserBean();
			u.setName("aa");
			das.insert(u);
		}
		
		List<UserBean> list = das.queryByCondWithPage(UserBean.class, "", null, null);
		if (list.size()!=5){
			throw new RuntimeException("assert");
		}
		
		list = das.queryByCondWithPage(UserBean.class, "order by id desc", null, ExtCond.create(new PageCond(2,1)));
		if (list.size()!=2){
			throw new RuntimeException("assert");
		}

		list = das.queryByCondWithPage(UserBean.class, "", null, ExtCond.create(new PageCond(2,3)));
		if (list.size()!=1){
			throw new RuntimeException("assert");
		}
		
		list = das.queryByCondWithPage(UserBean.class, "", null, ExtCond.create(new PageCond(2,4)));
		if (list.size()!=0){
			throw new RuntimeException("assert");
		}
		
		
		List<Object> result = das.queryBySqlWithPage("select * from luisuserbean ", null, null);
		for (int i=0;i<result.size();i++){
			System.out.println(result.get(i));
		}
		
	}

	public void testPropertyType() {
		IDataService das = ServiceFactory.getService(IDataService.class);
		BeanWithTypes b = new BeanWithTypes();
		
		das.insert(b);
	}
}
