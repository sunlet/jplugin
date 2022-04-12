package test.net.jplugin.core.das.hib;

import test.net.jplugin.das.hib.DBPojoIdNativeTest;
import test.net.jplugin.das.hib.DBPojoTest;
import test.net.jplugin.das.hib.DasTest;
import test.net.jplugin.das.hib.StatementTest;
import test.net.jplugin.das.hib.mt.MtPojo;
import test.net.jplugin.das.hib.mt.MtServiceTest;
import test.net.jplugin.das.hib.mt.NonMtPojo;
import net.jplugin.core.das.hib.api.ExtensionDasHelper;
import net.jplugin.core.kernel.api.AbstractPluginForTest;
import net.jplugin.core.kernel.api.CoreServicePriority;
import net.jplugin.core.kernel.api.PluginAnnotation;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-5 下午06:13:07
 **/

public class Plugin extends AbstractPluginForTest{

	public Plugin(){
		ExtensionDasHelper.addDataMappingExtension(this, DBHibTest.class);
		
		
		ExtensionDasHelper.addDataMappingExtension(this, DBPojoTest.class);
		ExtensionDasHelper.addDataMappingExtension(this, DBPojoIdNativeTest.class);
		
		ExtensionDasHelper.addDataMappingExtension(this, MtPojo.class);
		ExtensionDasHelper.addDataMappingExtension(this, NonMtPojo.class);
	}
	/* (non-Javadoc)
	 * @see net.luis.plugin.kernel.api.AbstractPluginForTest#test()
	 */
	@Override
	public void test() throws Exception {
		new HibTest().test();
		
//		DasTest.test();
//		StatementTest.test();
//		new MtServiceTest().test();
	}

	/* (non-Javadoc)
	 * @see net.luis.plugin.kernel.api.AbstractPlugin#getPrivority()
	 */
	@Override
	public int getPrivority() {
		return CoreServicePriority.DAS_HIB+1;
	}
	@Override
	public boolean searchClazzForExtension() {
		// TODO Auto-generated method stub
		return false;
	}

}
