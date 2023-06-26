package jplugincoretest.bind_extensin_incept;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.BindBean;
import net.jplugin.core.kernel.api.Initializable;
import net.jplugin.core.kernel.api.RefExtension;
import net.jplugin.core.kernel.api.RefExtensions;

import java.util.List;

@BindBean(id="o34rp4345")
public class Extension1097Test implements Initializable {

    @RefExtensions
    List<IMyInterface1097> list;
    @Override
    public void initialize() {
        IMyInterface1097 impl1 = list.get(1);
        IMyInterface1097 impl2 = list.get(0);

        AssertKit.assertEqual(2,impl1.hello());
        AssertKit.assertEqual(1,impl2.hello());

        AssertKit.assertEqual(2,impl1.goodMorning());
        AssertKit.assertEqual(0,impl2.goodMorning());

    }
}
