package test.net.jplugin.ext.webasic.bind;

import net.jplugin.ext.webasic.api.BindServiceExport;

@BindServiceExport(path = "/bindserviceexport")
public class BindServiceExportTest {
	public String  test(String a,String b){
		return a+b;
	}
}
