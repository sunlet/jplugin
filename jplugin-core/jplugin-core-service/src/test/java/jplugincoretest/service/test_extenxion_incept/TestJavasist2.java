package jplugincoretest.service.test_extenxion_incept;



import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TestJavasist2 {


    public void test() throws InstantiationException, IllegalAccessException {

        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(ServiceForInceptTest.class);

        factory.setHandler(new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                //这里放置重复代码
                Object ret = null;
                try {
                    System.out.println("============================");
                    System.out.println("开启会话");
                    ////
                    //ret = thisMethod.invoke(target, args);//这样使用，调用的是另一个对象，旁路出去了：）
                    System.out.println("calling ... thisMethod:"+thisMethod.getName()+"  procceed:"+proceed.getName());
                    ret = proceed.invoke(self, args);//这样使用，调用自己的父对象
                    //ret = thisMethod.invoke(self, args);//这样使用，则发生递归调用自身，堆栈溢出
                    ///
                    System.out.println("事务提交");
                } catch (Exception e) {
                    System.out.println("事务回滚");
                    e.printStackTrace();
                } finally {
                    System.out.println("关闭会话");
                }
                return ret;
            }

        });

        Class clazz = factory.createClass();
        ServiceForInceptTest proxy = (ServiceForInceptTest) clazz.newInstance();

        System.out.println(proxy.hello("aaa"));

//        proxy.update();
//        proxy.insert();
    }


    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//        new TestJavasist2().test();
        Object obj = new CCC();
        System.out.println(obj.toString());
    }

    public static class CCC{
        @Override
        public String toString() {
            return super.toString();
        }
    }
}