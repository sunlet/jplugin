package test.net.jplugin.core.ctx;

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

	public void testNoMetaWithException() throws Exception {
		throw new Exception("HAHAHA");
	}

}
