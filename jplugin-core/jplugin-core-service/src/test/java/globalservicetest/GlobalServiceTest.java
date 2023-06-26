package globalservicetest;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.service.api.ServiceFactory;

public class GlobalServiceTest {

    public static void main(String[] args) {
        test();
    }
    public static void test(){
        ServiceFactory._GlobalServiceHolder.addMapping("name", Tuple2.with(new Svc1(),Svc1.class));
        AssertKit.assertFalse(ServiceFactory._GlobalServiceHolder.get("name").getClass().isArray());

        AssertKit.assertTrue(ServiceFactory.getService("name",Svc1.class) instanceof Svc1);


        ServiceFactory._GlobalServiceHolder.addMapping("name",Tuple2.with(new Svc1(),Svc1.class));


        ServiceFactory.getService("name",Svc1.class);
        AssertKit.assertException(()->ServiceFactory.getService("name",Svc1.class));
        AssertKit.assertException(()->ServiceFactory.getService("name1",Svc1.class));

        AssertKit.assertTrue(ServiceFactory._GlobalServiceHolder.get("name").getClass().isArray());
        ServiceFactory._GlobalServiceHolder.addMapping("name",Tuple2.with(new Svc1(),Svc1.class));
        AssertKit.assertEqual(((Object[])ServiceFactory._GlobalServiceHolder.get("name")).length, 3);
        AssertKit.assertTrue(ServiceFactory._GlobalServiceHolder.get("name").getClass().isArray());
        AssertKit.assertTrue(((Object[])ServiceFactory._GlobalServiceHolder.get("name"))[0]!=null);
        AssertKit.assertTrue(((Object[])ServiceFactory._GlobalServiceHolder.get("name"))[1]!=null);
        AssertKit.assertTrue(((Object[])ServiceFactory._GlobalServiceHolder.get("name"))[2]!=null);

        ServiceFactory._GlobalServiceHolder.addMapping(null,Tuple2.with(new Svc1(),Svc1.class));

        AssertKit.assertTrue(ServiceFactory._GlobalServiceHolder.get(ISvc.class.getName())!=null);
        AssertKit.assertTrue(ServiceFactory._GlobalServiceHolder.get(Svc1.class.getName())!=null);

        ServiceFactory._GlobalServiceHolder.addMapping(null,Tuple2.with(new Svc2(),Svc2.class));

        AssertKit.assertTrue(ServiceFactory._GlobalServiceHolder.get(ISvc.class.getName()).getClass().isArray());

        ServiceFactory._GlobalServiceHolder.addMapping(null,Tuple2.with(new Svc3(),Svc3.class));




    }


    public static interface ISvc{

    }

    public static class Svc1 implements  ISvc{

    }

    public static class Svc2 implements  ISvc{

    }

    public static class Svc3 implements  ISvc{

    }

}
