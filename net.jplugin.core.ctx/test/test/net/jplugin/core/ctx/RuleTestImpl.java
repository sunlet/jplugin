package test.net.jplugin.core.ctx;

import net.jplugin.core.ctx.api.Rule;
import net.jplugin.core.ctx.impl.DefaultRuleAnnoConfig;
import net.jplugin.core.ctx.impl.DefaultRuleInvocationHandler;

public class RuleTestImpl implements IRuleTest {

	public void testNoMeta() {
		System.out.println("testNoMeta");
		
		if (DefaultRuleAnnoConfig.findDefaultRuleAnnotation()!=null){
			StackTraceElement[] ste = new Exception().getStackTrace();
			if (contain(ste,9,DefaultRuleInvocationHandler.class)){
			}else{
				throw new RuntimeException("must contain");
			}
		}else{
			StackTraceElement[] ste = new Exception().getStackTrace();
			if (contain(ste,9,DefaultRuleInvocationHandler.class)){
				throw new RuntimeException("can't contain");
			}
		}
	}

	private boolean contain(StackTraceElement[] ste, int cnt, Class<DefaultRuleInvocationHandler> class1) {
		for (int i=0;i<cnt;i++){
			if (ste[i].getClassName().equals(class1.getName()))
				return true;
		}
		return false;
	}

	public void testNoMeta(String a) {
		System.out.println("testNoMeta  a");
	}

	public void testNoMeta2() {
		System.out.println("testNoMeta2");
	}

	@Rule
	public void testNoMetaWithException() throws Exception {
		throw new Exception("HAHAHA");
	}
	
	
//	@Rule
	void testMetaInDefault(String a){
		
	}
	
//	@Rule
	private void testMetaInPrivate(int b){
		
	}
	
//	@Rule
	public void testMethodNotInimpl(int c){
		
	}

}
