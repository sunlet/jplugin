package test.net.jplugin.core.ctx;

import net.jplugin.core.ctx.api.Rule;

public class RuleTestImpl implements IRuleTest {

	public void testNoMeta() {
		System.out.println("testNoMeta");
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
