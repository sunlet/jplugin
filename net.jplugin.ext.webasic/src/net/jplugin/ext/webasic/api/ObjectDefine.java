package net.jplugin.ext.webasic.api;

import java.util.List;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.RuleServiceFactory;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.Extension.Property;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 下午02:16:58
 **/

public class ObjectDefine {
	public static final String OBJ_JAVAOBJECT="javaObject";
	public static final String OBJ_BIZLOGIC="bizLogic";

	
	String objType;
	String blName;
	Class  objClass;
//	String methodName;
	
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getBlName() {
		return blName;
	}
	public void setBlName(String blName) {
		this.blName = blName;
	}
	public Class getObjClass() {
		return objClass;
	}
	public void setObjClass(Class objClass) {
		this.objClass = objClass;
	}
//	public String getMethodName() {
//		return methodName;
//	}
//	public void setMethodName(String methodName) {
//		this.methodName = methodName;
//	}

//	public void setExtensionProperty(List<Extension.Property> list) throws ClassNotFoundException{
//		for (Property p:list){
//			if(p.getKey().equals("objType")){
//				this.setObjType(p.getValue());
//			}else if (p.getKey().equals("blName")){
//				this.setBlName(p.getValue());
//			}else if (p.getKey().equals("objClass")){
//				this.setObjClass(Class.forName(p.getValue()));
//			}else if (p.getKey().equals("methodName")){
//				this.setMethodName(p.getValue());
//			}else{
//				throw new RuntimeException("unknown property:"+p.getKey());
//			}
//		}
//	}
}
