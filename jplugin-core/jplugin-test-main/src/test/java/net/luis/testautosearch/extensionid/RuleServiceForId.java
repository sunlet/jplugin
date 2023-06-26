package net.luis.testautosearch.extensionid;

import net.jplugin.core.ctx.api.BindRuleService;
import net.jplugin.core.kernel.api.SetExtensionId;

@SetExtensionId("IRuleServiceForId")
@BindRuleService()
public class RuleServiceForId implements IRuleServiceForId{

	@Override
	public void a() {
	}

}
