package net.jplugin.ext.dict;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.ctx.api.RuleResult;
import net.jplugin.core.service.api.ServiceFactory;
import net.jplugin.ext.dict.api.Dictionary;
import net.jplugin.ext.dict.api.IDictionaryService;
import net.jplugin.ext.webasic.api.AbstractExController;

public class DictController extends AbstractExController {

	public void getDictLabel(){
		String name = getStringAttr("prov");
		String param = getStringAttr("param");//may null
		String value = getStringAttr("value");
		AssertKit.assertStringNotNull(name);
		AssertKit.assertStringNotNull(value);
		IDictionaryService svc = ServiceFactory.getService(IDictionaryService.class);
		String lb = svc.getDictionaryLabel(name, param,value);
		RuleResult rr = RuleResult.create();
		rr.setContent("label", lb);
		super.renderJson(rr);
	}
	
	public void getDicts(){
		String name = getStringAttr("prov");
		String param = getStringAttr("param");//may null
		AssertKit.assertStringNotNull("name");
		IDictionaryService svc = ServiceFactory.getService(IDictionaryService.class);
		List<Dictionary> list = svc.getDictionarys(name, param);
		RuleResult rr = RuleResult.create();
		rr.setContent("list", list);
		super.renderJson(rr);
	}
	
}
