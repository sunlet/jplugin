package net.jplugin.core.ctx.api;

import java.util.List;

import net.jplugin.core.kernel.api.Extension;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-10 上午09:15:30
 **/

public class RuleServiceDefinition {
	
	private Class<?>  interf;
	private Class<?>  impl;
	
	public RuleServiceDefinition(){
	}

	public Class<?> getInterf() {
		return interf;
	}

	public void setInterf(Class<?> interf) {
		this.interf = interf;
	}

	public Class<?> getImpl() {
		return impl;
	}

	public void setImpl(Class<?> impl) {
		this.impl = impl;
	}
	
//	public void setExtensionProperty(List<Extension.Property> property){
//		if (property.size()!=1){
//			throw new RuleRuntimeException(" the property must has one key/value");
//		}
//		
//		
//			
//		this.ruleInterface = interf;
//		this.ruleImplementation = impl;
//	}
//	
//	public Class<?> getRuleInterface() {
//		return interf;
//	}
//	public void setRuleInterface(Class<?> ruleInterface) {
//		this.interf = ruleInterface;
//	}
//	public Class<?> getRuleImplementation() {
//		return impl;
//	}
//	public void setRuleImplementation(Class<?> ruleImplementation) {
//		this.impl = ruleImplementation;
//	}
	
	

	/**
	 * 
	 */
	public void valid() {
		Class aInterf = this.interf;
		Class aImpl = this.impl;

		
		if (!aInterf.isInterface())
			throw new RuleRuntimeException(aInterf.getName()+" is not a interface");
		
		boolean isimpl = false;
		for (Class i:aImpl.getInterfaces()){
			if (i==aInterf){
				isimpl = true;
				break;
			}
		}
		if (!isimpl){
			throw new RuleRuntimeException(aInterf.getName()+" is not impl by "+aImpl.getName());
		}
	}

}
