package net.jplugin.ext.filesvr.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.filesvr.Plugin;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.api.FileUploadFilter;
import net.jplugin.ext.filesvr.svc.IFileService;
import net.jplugin.ext.filesvr.web.UploadHelper.SaveFileResult;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:13:31
 **/

public class FileUploadServlet {

	private static final String FILE_ID = "fileId";
	
	public void index(HttpServletRequest req,HttpServletResponse res) throws IOException{
		//处理filter
		FileUploadFilter[] filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_UPLOADFILTER, FileUploadFilter.class);
		for (FileUploadFilter f:filters){
			if (!f.filter(req)){
				return;
			}
		}
		
		RequesterInfo ctx = ThreadLocalContextManager.instance.getContext().getRequesterInfo();
		IFileService svc = RuleServiceFactory.getRuleService(IFileService.class);
		
		String reqFileType = req.getParameter("filetype");

		List<SaveFileResult> files = UploadHelper.saveFiles(req,true,reqFileType);
		if (reqFileType!=null){
			if (!FileTypes.validType(reqFileType)){
				throw new RuntimeException("Error file type:"+reqFileType);
			}
		}
		
		if (files.size() > 1){
			throw new RuntimeException("cound't come here");
		}
		if (files.size() ==0 ){
			return;
		}
		SaveFileResult sfr = files.get(0);
		long fileid = svc.createFile(sfr.getFilename(), sfr.getFileType(), sfr.getSize(), sfr.getStorePath());
		
		//return
		RuleResult rr = RuleResult.create(RuleResult.OK);
		rr.setContent(FILE_ID, fileid);
		res.getWriter().write(rr.getJson());
	}
}
