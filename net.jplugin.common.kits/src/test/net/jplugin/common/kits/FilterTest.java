package test.net.jplugin.common.kits;

import net.jplugin.common.kits.filter.FilterManager;

public class FilterTest {

	public static void main(String[] args) {
		FilterManager<Ctx> fm = new FilterManager<>();
		
		fm.addFilter((fc,ctx)->{System.out.println("in filter1");return fc.next(ctx);});
		fm.addFilter((fc,ctx)->{System.out.println("in filter2");return fc.next(ctx);});

		fm.addFilter((fc,ctx)->{System.out.println("final output"+ ctx.a+" , "+ctx.b); return ctx.a+ctx.b;});
		
		for (int i=0;i<5;i++){
			Object ret = fm.filter(new Ctx(i+"", i+""));
			System.out.println("return = "+ ret);
		}
	}
	
	
	
	public static class Ctx{
		public Ctx(String string, String string2) {
			a = string;
			b = string2;
		}
		String a;
		String b;
	}
}
