package net.jplugin.ext.filesvr.sample;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * 从网上复制
 **/

public class UploadSample {

	private String uploadPath = "C:\\upload\\"; // 上传文件的目录
	private String tempPath = "C:\\upload\\tmp\\"; // 临时文件目录

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			DiskFileItemFactory fu = new DiskFileItemFactory();

			// 设置缓冲区大小，这里是4kb
			fu.setSizeThreshold(4096);
			// 设置临时目录：
			fu.setRepository(new File(tempPath));

			ServletFileUpload upload = new ServletFileUpload(fu);

			// 设置最大文件尺寸，这里是4MB
			upload.setSizeMax(4194304);

			// 得到所有的文件：
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			// 依次处理每一个文件：
			while (i.hasNext()) {
				FileItem fi = (FileItem) i.next();
				// 获得文件名，这个文件名包括路径：
				String fileName = fi.getName();
				long fileSize = fi.getSize();
				// 在这里可以记录用户和文件信息
				// ...
				// 写入文件，暂定文件名为a.txt，可以从fileName中提取文件名：
				fi.write(new File(uploadPath + "a.txt"));
			}
		} catch (Exception e) {
			// 可以跳转出错页面
		}
	}

}
