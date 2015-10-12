package net.jplugin.core.config.impl;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.kernel.api.IPropertyFilter;

public class PropertyFilter implements IPropertyFilter {

	@Override
	public String filte(String s) {
		if (s!=null && s.startsWith("${") && s.endsWith("}")){
			return s.substring(2, s.length()-1);
		}else{
			return s;
		}
	}
	
	public static void main(String[] args) {
		PropertyFilter f =new PropertyFilter();
		AssertKit.assertEqual(f.filte("${abc}"),"abc");
		AssertKit.assertEqual(f.filte("${}"),"");
		AssertKit.assertEqual(f.filte("${a"),"${a");
	}
}
