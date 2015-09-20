package net.jplugin.ext.dict.test;

import java.util.ArrayList;
import java.util.List;

import net.jplugin.ext.dict.api.Dictionary;
import net.jplugin.ext.dict.api.IDictProvidor;

public class TestDictProvider implements IDictProvidor {

	@Override
	public boolean dynamic() {
		return true;
	}

	@Override
	public List<Dictionary> get(String param) {
		ArrayList al = new ArrayList();
		al.add(new Dictionary("v1", "l1"));
		al.add(new Dictionary("v2", "l2"));
		al.add(new Dictionary("v3", "l3"));
		return al;
	}

}
