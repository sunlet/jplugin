package net.jplugin.ext.filesvr;

import net.jplugin.core.ctx.api.RuleServiceDefinition;
import net.jplugin.core.event.api.EventAliasDefine;
import net.jplugin.core.event.api.Channel.ChannelType;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.ext.filesvr.api.FileCreatedEvent;
import net.jplugin.ext.filesvr.api.FileDownloadFilter;
import net.jplugin.ext.filesvr.api.FileUploadFilter;
import net.jplugin.ext.filesvr.db.DBDefine;
import net.jplugin.ext.filesvr.svc.ComressImageConsumer;
import net.jplugin.ext.filesvr.svc.CreateImageEventFilter;
import net.jplugin.ext.filesvr.svc.FileService;
import net.jplugin.ext.filesvr.svc.IFileService;
import net.jplugin.ext.filesvr.web.FileDownloadServlet;
import net.jplugin.ext.filesvr.web.FileUploadServlet;
import net.jplugin.ext.filesvr.web.HeaderMaintain;
import net.jplugin.ext.webasic.ExtensionWebHelper;
import net.jplugin.ext.webasic.api.ObjectDefine;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-15 下午03:09:15
 **/

public class Plugin extends AbstractPlugin{

	public static final String CFG_MAXPICSIZE = "max-picsize";
	public static final String CFG_MAXFILESIZE = "max-filesize";
	public static final String CFG_UPLOAD_PATH = "file-upload-path";
	public static final String CFG_TEMP_PATH = "file-temp-path";
	public static final String CFG_UPLOAD_BUF_SIZE = "upload-buf-size";
	public static final String EP_UPLOADFILTER = "EP_UPLOADFILTER";
	public static final String EP_DOWNLOADFILTER = "EP_DOWNLOADFILTER";
	public static final String CFG_SMALLPIC_PATH = "small-pic-path";

	public Plugin(){
		addConfigure(CFG_MAXFILESIZE, "20000000");
		addConfigure(CFG_MAXPICSIZE,"6000000");
		
		addExtensionPoint(ExtensionPoint.create(EP_UPLOADFILTER,FileUploadFilter.class));
		addExtensionPoint(ExtensionPoint.create(EP_DOWNLOADFILTER,FileDownloadFilter.class));
		addExtension(Extension.create(net.jplugin.core.das.hib.Plugin.EP_DATAMAPPING, DBDefine.class));

		addExtension(Extension.create(net.jplugin.core.ctx.Plugin.EP_RULE_SERVICE, IFileService.class.getName(),RuleServiceDefinition.class,new String[][]{{"interf",IFileService.class.getName()},{"impl",FileService.class.getName()}}));
		
//		addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, "/upload",ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",net.jplugin.ext.filesvr.web.FileUploadServlet.class.getName()},{"methodName","uploadFile"}}));
//		addExtension(Extension.create(net.jplugin.ext.webasic.Plugin.EP_WEBCONTROLLER, "/download",ObjectDefine.class,new String[][]{{"objType","javaObject"},{"objClass",net.jplugin.ext.filesvr.web.FileUploadServlet.class.getName()},{"methodName","downloadFile"}}));

		ExtensionWebHelper.addWebControllerExtension(this, "/upload", FileUploadServlet.class);
		ExtensionWebHelper.addWebControllerExtension(this, "/download", FileDownloadServlet.class);
		
		addExtension(Extension.createStringExtension(net.jplugin.core.event.Plugin.EP_EVENT_TYPES, FileCreatedEvent.ET_FILE_CREATED));
		addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_TYPE_ALIAS, "",EventAliasDefine.class,new String[][]{{"eventType",FileCreatedEvent.ET_FILE_CREATED},{"typeAlias",FileCreatedEvent.IMAGE_EVENT_ALIAS},{"filterClass",CreateImageEventFilter.class.getName()}} ));
		addExtension(Extension.create(net.jplugin.core.event.Plugin.EP_EVENT_CONSUMER, "",ComressImageConsumer.class,new String[][]{{"targetType",FileCreatedEvent.IMAGE_EVENT_ALIAS},{"channelType",ChannelType.POST_MEMORY.toString()}} ));

		ExtensionWebHelper.addWebExControllerExtension(this, "/head", HeaderMaintain.class);
	}
	
	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.FILE_UPLOAD;
	}

	/* (non-Javadoc)
	 * @see net.luis.common.kernel.api.IPlugin#init()
	 */
	public void onCreateServices() {
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

}
