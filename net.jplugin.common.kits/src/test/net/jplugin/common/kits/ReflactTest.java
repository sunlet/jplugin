package test.net.jplugin.common.kits;

import java.lang.reflect.Field;
import java.util.List;

import net.jplugin.common.kits.ReflactKit;

public class ReflactTest {
public static void main(String[] args) {
	Clazz c = new Clazz();
	List<Field> flist = ReflactKit.getAllFields(c);
	
	for (Field f:flist){
		System.out.println(f.getName());
		if (f.getType()==String.class)
			ReflactKit.setFieldValueForce(f,c,"a");
	}
	
	c.print();
}



}

