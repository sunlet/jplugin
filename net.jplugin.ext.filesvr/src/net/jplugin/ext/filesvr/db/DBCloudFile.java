package net.jplugin.ext.filesvr.db;

import net.jplugin.core.das.hib.api.Entity;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:15:53
 **/
@Entity(idField="fileId")
public class DBCloudFile {
	long fileId;
	String fileName;
	String extName;
	String fileType;
	long fileSize;
	String creator;
	String storePath;
	
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExtName() {
		return extName;
	}
	public void setExtName(String extName) {
		this.extName = extName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getStorePath() {
		return storePath;
	}
	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}
}
