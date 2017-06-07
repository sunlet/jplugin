package net.jplugin.ext.filesvr.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.FileKit;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.kernel.api.ctx.RequesterInfo;
import net.jplugin.core.kernel.api.ctx.ThreadLocalContextManager;
import net.jplugin.ext.filesvr.Plugin;
import net.jplugin.ext.filesvr.api.FileDownloadFilter;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.api.FileUploadFilter;
import net.jplugin.ext.filesvr.api.Size;
import net.jplugin.ext.filesvr.db.DBCloudFile;
import net.jplugin.ext.filesvr.svc.IFileService;
import net.jplugin.ext.filesvr.svc.PicCompressHelper;
import net.jplugin.ext.filesvr.web.UploadHelper.SaveFileResult;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:13:31
 **/

public class FileDownloadServlet {

	private static final String FILE_ID = "fileId";
	
	public void index(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		//处理filter
		FileDownloadFilter[] filters = PluginEnvirement.getInstance().getExtensionObjects(Plugin.EP_DOWNLOADFILTER, FileDownloadFilter.class);
		for (FileDownloadFilter f:filters){
			if (!f.filter(req)){
				return;
			}
		}
		
		String fileid = req.getParameter(FILE_ID);
		AssertKit.assertStringNotNull(fileid, "fileid");
		
		IFileService svc = RuleServiceFactory.getRuleService(IFileService.class);
		DBCloudFile cf = svc.getFile(Long.parseLong(fileid));
		
		if (cf==null) return;
		
		if (FileTypes.FT_IMAGE.equals(cf.getFileType())){
			String scale=req.getParameter("scale");
			if (scale==null) scale=ImgScale.BIG;
			/*预先压缩好放入web目录的方式
			String path = "/upload/" + cf.getStorePath();
			path = ImgScale.maintainImgName(path,scale);
			req.getRequestDispatcher(path).forward(req, res);
			*/
			
			//新方式，使用时压缩，并不放入web当中
			AssertKit.assertNotNull(cf, "the cloud file");
			String origFilePath = Configures.uploadPath+"/"+cf.getStorePath();
			String dirname = new File(origFilePath).getParentFile().getAbsolutePath()+"/";
			String origFileName = origFilePath.substring(dirname.length());
			
			String targetFileName = ImgScale.maintainImgName(origFileName,scale);
			String targetFilePath = dirname +targetFileName;
			
			Size targetSize = ImgScale.getTargetSize(scale);
			if (!FileKit.existsFile(targetFilePath)){
				//压缩文件
				PicCompressHelper.compressPic(dirname, dirname, origFileName, targetFileName, targetSize.width, targetSize.height, true);
			}
			DownloadHelper.downLoad(targetFilePath, cf.getFileName(), res, true);
		}else{
			AssertKit.assertNotNull(cf, "the cloud file");
			String filepath = Configures.uploadPath+"/"+cf.getStorePath();
			DownloadHelper.downLoad(filepath, cf.getFileName(), res, false);
		}
	}
	
}
