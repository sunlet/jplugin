package net.jplugin.ext.filesvr.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.filesvr.db.DBCloudFile;
import net.jplugin.ext.filesvr.svc.IFileService;
import net.jplugin.ext.filesvr.svc.PicCompressHelper;
import net.jplugin.ext.webasic.api.AbstractExController;

public class HeaderMaintain extends AbstractExController {
	
	public void img() throws ServletException, IOException{
		String userId = getStringAttr("userId");
		AssertKit.assertNotNull(userId, "userId");
		
		String uri = "/upload/head/"+userId+".jpg";
		String realpath = getReq().getServletContext().getRealPath(uri);
		File file = new File(realpath);
		if (!file.exists()){
			uri = "/server/icons/def_head.jpg";
		}
		forward(uri);
	}
	
	public void compressHeaderPic(){
		String fileid = getStringAttr("fileId");
		//String userid = getStringAttr("userid");
		String x = getStringAttr("x");
		String y = getStringAttr("y");
		String s = getStringAttr("s");
		String initPicWidth=getStringAttr("initPicWidth");
		
		System.out.println(initPicWidth);
		
		int ix = x==null? 0:Integer.parseInt(x);
		int iy = y==null? 0:Integer.parseInt(y);
		int is = s==null? 60:Integer.parseInt(s);
		int intInitPicWidth = initPicWidth==null ? 0:Integer.parseInt(initPicWidth);
		AssertKit.assertStringNotNull(fileid);
		
		String userid = ThreadLocalContextManager.getRequestInfo().getOperatorId();
		AssertKit.assertStringNotNull(userid);
		
		IFileService svc = RuleServiceFactory.getRuleService(IFileService.class);
		DBCloudFile cf = svc.getFile(Long.parseLong(fileid));
		String headFile = Configures.smallFilePath +"/head/"+userid+".jpg";
		FileKit.makeDirectory(new File(headFile).getParentFile().getAbsolutePath());
		PicCompressHelper.cutAndResizeImage(Configures.uploadPath+"/"+cf.getStorePath(), headFile, ix, iy, is, is,60,60,intInitPicWidth);
		
		RuleResult rr = RuleResult.create();
		renderJson(rr);
	}
}
