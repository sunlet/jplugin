package net.jplugin.core.kernel.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.ReflactKit;

public class ExtPlugin {

	public static Collection<? extends Object> get() {
		Set<Object> ret = new HashSet();
		for (int i=0;i<6;i++){
			Set<String> s = getSet(i);
			if (s!=null)
				ret.addAll(s);
		}
		return ret;
	}

	private static Set<String> getSet(int i) {
		String cName = "net.jplugin.PluginProvider_"+i;
		try{
			Class c = Class.forName(cName);
			Object ret = ReflactKit.invoke(c.newInstance(), "get", new Object[]{});
			return (Set<String>)ret;
		}catch(Exception e){}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(ExtPlugin.get());
	}
}
