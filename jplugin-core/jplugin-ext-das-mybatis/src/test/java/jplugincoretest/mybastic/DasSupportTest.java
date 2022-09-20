package jplugincoretest.mybastic;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.das.mybatis.api.SqlSessionDaoSupport;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.service.api.BindService;
import net.jplugin.core.service.api.RefService;

@BindService
public class DasSupportTest extends SqlSessionDaoSupport implements Initializable {

    @Override
    public String getDataSourceName() {
        return "database";
    }

    public void test(){
        AssertKit.assertTrue(getSqlSession()!=null);

    }

    @RefService
    DasSupportTest self;
    @Override
    public void initialize() {
        self.test();
    }
}
