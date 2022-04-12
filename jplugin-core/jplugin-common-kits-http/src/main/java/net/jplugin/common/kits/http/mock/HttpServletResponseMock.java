package net.jplugin.common.kits.http.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.jplugin.common.kits.JsonKit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-4 上午11:53:15
 **/

public class HttpServletResponseMock extends HttpServletResponseEmpty{
	PrintWriter printWriter;
	StringWriter strWriter;
	
	public HttpServletResponseMock(){
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return printWriter;
	}
	
	public String getResult(){
		return strWriter.toString();
	}
	
}
