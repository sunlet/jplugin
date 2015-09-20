package test.net.luis.plugin.das.mybatis.annotest;

import java.util.List;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.api.Rule.TxType;
import test.net.jplugin.core.das.mybatis.MybtestBean;

public interface IRule {
	
	@Rule(methodType=TxType.REQUIRED)
	public void add(String f1,String f2);
	
	@Rule(methodType=TxType.REQUIRED)
	public void addAndRollback(String f1,String f2);
	
	@Rule(methodType=TxType.REQUIRED)
	public void clear();
	
	@Rule()
	public List<MybtestBean> select();
}	
