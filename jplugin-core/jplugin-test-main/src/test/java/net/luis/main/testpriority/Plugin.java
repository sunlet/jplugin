package net.luis.main.testpriority;

import java.util.List;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.AbstractPlugin;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.ExtensionPoint;
import net.jplugin.core.kernel.api.PluginAnnotation;

@PluginAnnotation
public class Plugin extends AbstractPlugin {
	
	
	public Plugin() {
		testPointList();
		testPointListPriority();
		
		testPointMap();
		testPointMapPriority();
		
		testPointUnique();
	}
	@Override
	public void init() {
		

	}

	private void testPointUnique() {
			//验证后面的优先数大
			ExtensionPoint p1 = ExtensionPoint.createUnique("test", null);
			add(p1,Impl1.class,"name1",0);
			
			AssertKit.assertException(()->{
				add(p1,Impl2.class,"name2",0);	
			});
			
			List<Extension> list = p1.__debugGetExtensions();
			
			AssertKit.assertEqual(list.size(), 1);
			AssertKit.assertEqual(list.get(0).getClazz(), Impl1.class);
			
			
			//
			ExtensionPoint p = ExtensionPoint.createUniqueWithPriority("test", null);
			add(p,Impl1.class,"name1",0);
			add(p,Impl2.class,"name2",0);
			
			list = p.__debugGetExtensions();
			
			AssertKit.assertEqual(list.size(), 1);
			AssertKit.assertEqual(list.get(0).getName(), "name1");

			//
			p = ExtensionPoint.createUniqueWithPriority("test", null);
			add(p,Impl1.class,"name1",0);
			add(p,Impl2.class,"name2",-1);
			
			list = p.__debugGetExtensions();
			
			AssertKit.assertEqual(list.size(), 1);
			AssertKit.assertEqual(list.get(0).getName(), "name2");
			
			//
			p = ExtensionPoint.createUniqueWithPriority("test", null);
			add(p,Impl1.class,"name1",0);
			add(p,Impl2.class,"name2",2);
			
			list = p.__debugGetExtensions();
			
			AssertKit.assertEqual(list.size(), 1);
			AssertKit.assertEqual(list.get(0).getName(), "name1");	
			
	}

	private void testPointMapPriority() {
		//验证后面的优先数大
		ExtensionPoint p = ExtensionPoint.createNamedWithPriority("test", null);
		add(p,Impl1.class,"name1",0);
		add(p,Impl2.class,"name2",0);
		add(p,Impl3.class,"name1",1);	
		
		List<Extension> list = p.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 2);
		AssertKit.assertEqual(list.get(0).getClazz(), Impl1.class);
		AssertKit.assertEqual(list.get(1).getClazz(), Impl2.class);
		
		
		//验证后面的优先数小
		p = ExtensionPoint.createNamedWithPriority("test", null);
		add(p,Impl1.class,"name1",0);
		add(p,Impl2.class,"name2",0);
		add(p,Impl3.class,"name1",-1);	
		
		list = p.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 2);
		AssertKit.assertEqual(list.get(0).getClazz(), Impl3.class);
		AssertKit.assertEqual(list.get(1).getClazz(), Impl2.class);

		//验证优先级相等
		p = ExtensionPoint.createNamedWithPriority("test", null);
		add(p,Impl1.class,"name1",0);
		add(p,Impl2.class,"name2",0);
		add(p,Impl3.class,"name1",0);	
		
		list = p.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 2);
		AssertKit.assertEqual(list.get(0).getClazz(), Impl1.class);
		AssertKit.assertEqual(list.get(1).getClazz(), Impl2.class);
	}

	private void testPointMap() {
		final ExtensionPoint p = ExtensionPoint.createNamed("test", null);
		
		add(p,Impl1.class,"name1",0);
		add(p,Impl1.class,"name2",0);
		AssertKit.assertException(()->{
			add(p,Impl2.class,"name1",0);	
		});
		
		List<Extension> list = p.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 2);
		AssertKit.assertEqual(list.get(0).getName(), "name1");
		AssertKit.assertEqual(list.get(1).getName(), "name2");

	}

	private void testPointListPriority() {
		
		ExtensionPoint p2 = ExtensionPoint.createListWithPriority("test", null);
		add(p2,Impl1.class,"name1",0);
		add(p2,Impl1.class,"name2",0);
		add(p2,Impl2.class,"name3",-1);
		List<Extension> list = p2.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 3);
		AssertKit.assertEqual(list.get(0).getName(), "name3");
		AssertKit.assertEqual(list.get(1).getName(), "name1");
		AssertKit.assertEqual(list.get(2).getName(), "name2");
	}

	private void testPointList() {
		final ExtensionPoint p = ExtensionPoint.createList("test", null);
		AssertKit.assertException(()->add(p,Impl1.class,"name1",1));
		
		ExtensionPoint p2 = ExtensionPoint.createList("test", null);
		add(p2,Impl1.class,"name1",0);
		add(p2,Impl1.class,"name1",0);
		add(p2,Impl2.class,"name1",0);
		List<Extension> list = p2.__debugGetExtensions();
		
		AssertKit.assertEqual(list.size(), 3);
		AssertKit.assertEqual(list.get(0).getClazz(), Impl1.class);
		AssertKit.assertEqual(list.get(1).getClazz(), Impl1.class);
		AssertKit.assertEqual(list.get(2).getClazz(), Impl2.class);
	}
	
	void add(ExtensionPoint p, Class clz,String name,int priority) {
		Extension e = Extension.create("test",name,clz );
		e.setPriority((short) priority);
		String ret = p.__debugValidAndAdd(e);
		if (StringKit.isNotNull(ret))
			throw new RuntimeException(ret);
	}

	@Override
	public int getPrivority() {
		return 0;
	}

	public static class Impl1 implements IServvv{
		@Override
		public void a() {
		}
	}
	
	public static class Impl2 implements IServvv{
		@Override
		public void a() {
		}
	}

	public static class Impl3 implements IServvv{
		@Override
		public void a() {
		}
	}

	public static class Impl4 implements IServvv{
		@Override
		public void a() {
		}
	}

	
	public static interface IServvv{
		void a();
	}
}
