package net.luis.testautosearch;

import net.jplugin.core.service.api.BindService;
import net.jplugin.core.service.api.BindServiceSet;

public class Test {

	public static void main(String[] args) {
		BindServiceSet sss = ServiceTest.class.getAnnotation(BindServiceSet.class);
		ServiceTest.class.getAnnotationsByType(BindService.class);
		BindService[] values = sss.value();
		for (int i=0;i<values.length;i++) {
			System.out.println(values[i]);
		}
	}
}
