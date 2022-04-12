package net.jplugin.common.kits;

import java.util.ArrayList;
import java.util.List;

public class CollectionKit {
	public static List listWith(Object... objs){
		ArrayList l = new ArrayList(objs.length);
		for (Object o:objs){
			l.add(o);
		}
		return l;
	}
}
