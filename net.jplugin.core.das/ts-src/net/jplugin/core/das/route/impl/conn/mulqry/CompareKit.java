package net.jplugin.core.das.route.impl.conn.mulqry;

public class CompareKit {

	public static int compareSupportNull(Comparable o1, Comparable o2) {
		
		if (o1!=null && o2!=null)
			return o1.compareTo(o2);
		else if (o1!=null)
			return 1;
		else if (o2!=null)
			return -1;
		else
			//两个都为null
			return 0;

	}

}
