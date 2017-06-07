package net.jplugin.core.kernel.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.ReflactKit;

public class ExtPlugin {

	public static Collection<String> get() {
		Set<String> ret = new HashSet();
		for (int i=0;i<6;i++){
			Set<String> s = getSet(i);
			if (s!=null){
				for (String pname:s){
					addIfExists(ret,pname);
				}
			}
		}
		return ret;
	}
	
	private static void addIfExists(Set<String> ret,String string) {
		try{
			Class.forName(string);
			ret.add(string);
		}catch(ClassNotFoundException e){
			PluginEnvirement.INSTANCE.getStartLogger().log("IgnoredPlugin : "+string);
		}
		
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
