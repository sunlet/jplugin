package test.net.jplugin.ext.webasic;

import net.jplugin.ext.webasic.impl.WebDriver;

public class TestPathSearch {

	public void test() {
		WebDriver.INSTANCE.parseControllerMeta("/a/b/c/d/e/f/g.do");
		WebDriver.INSTANCE.parseControllerMeta("/a/b.do");
		WebDriver.INSTANCE.parseControllerMeta("/a.do");
	}

}
