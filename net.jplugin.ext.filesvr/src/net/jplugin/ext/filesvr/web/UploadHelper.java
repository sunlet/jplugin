package net.jplugin.ext.filesvr.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.FileKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.ConfigHelper;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.core.log.api.ILogService;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.filesvr.Plugin;
import net.jplugin.ext.filesvr.api.FileTypes;
import net.jplugin.ext.filesvr.api.IStorePathGenerator;
import net.jplugin.org.apache.log.Logger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-16 上午10:37:10
 **/

public class UploadHelper {

	/**
	 * @author Luis
	 *
	 */
	public static class SaveFileResult {
		private String filename;
		private long size;
		private String storePath;
		private String fileType;
		public String getFilename() {
			return filename;
		}
		public long getSize() {
			return size;
		}
		public String getStorePath() {
			return storePath;
		}
		public String getFileType() {
			return fileType;
		}

		
	}
	


	/**
	 * 本方法，如果失败都会正常返回，只是不返回这个文件
	 * @param request
	 * @param reqFileType 
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public static List<SaveFileResult> saveFiles(HttpServletRequest request,boolean onlyOne, String reqFileType) {
		//请求可以直接传入filetype
		
		List<SaveFileResult> retList = new ArrayList<SaveFileResult>(2);
		try {
			DiskFileItemFactory fu = new DiskFileItemFactory();

			// 设置缓冲区大小，这里是4kb
			fu.setSizeThreshold(Configures.uploadBufferSize);
			// 设置临时目录：
			fu.setRepository(new File(Configures.tempPath));

			ServletFileUpload upload = new ServletFileUpload(fu);



			// 得到所有的文件：
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			// 依次处理每一个文件：
			
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				// 获得文件名，这个文件名包括路径：
				String fileName = fi.getName();
				
				System.out.println("field name="+fi.getName());

				if (StringKit.isNull(fileName)){
					//不是文件项
					continue;
				}
				
				long fileSize = fi.getSize();
				if (fileSize==0){
					continue;
				}
				
				String fileType = reqFileType!=null? reqFileType:FileTypes.getFileType(FileKit.getFileExt(fileName));
				if (fileType.equals(FileTypes.FT_IMAGE)){
					// 设置最大文件尺寸，这里是4MB
					upload.setSizeMax(Configures.maxPicSize);
					if (fileSize>Configures.maxPicSize){
						continue;
					}
				}else{
					upload.setSizeMax(Configures.maxFileSize);
					if (fileSize>Configures.maxFileSize){
						continue;
					}
				}

				String storePath = IStorePathGenerator.instance.generateStorePath(fileName);
				File file = new File(Configures.uploadPath + "/"+storePath);
				FileKit.makeDirectory(file.getParentFile().getAbsolutePath());
				fi.write(file);
				
				SaveFileResult res = new SaveFileResult();
				res.filename = fileName;
				res.size = fileSize;
				res.fileType = fileType;
				res.storePath = storePath;
				retList.add(res);
				
				if (onlyOne){
					break;
				}
			}
			return retList;
		} catch (Exception e) {
			ILogService svc = ServiceFactory.getService(ILogService.class);
			svc.getLogger(UploadHelper.class.getName()).error("file upload error",e);
			return retList;
		}
	}


}
