package net.luis.main.webreq;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.http.HttpKit;
import net.jplugin.common.kits.http.HttpStatusException;
import net.jplugin.ext.webasic.api.IController;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-3-14 ����03:56:28
 **/

public class UserRequest{
	public void showAddPage(HttpServletRequest req,HttpServletResponse res) throws IOException{
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><td>aaaa</td><td>bbb</td></tr>");
		sb.append("</table>");
		
		res.getWriter().print(sb.toString());
	}

	public static void test() throws HttpStatusException, IOException {
		String result = HttpKit.post("http://localhost:8080/demo/userreq/showAddPage.do", new HashMap());
		AssertKit.assertEqual(result,"<table><tr><td>aaaa</td><td>bbb</td></tr></table>");
	}

}
