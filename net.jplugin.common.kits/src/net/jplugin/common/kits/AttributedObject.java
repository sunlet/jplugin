package net.jplugin.common.kits;

import java.util.HashMap;

public class AttributedObject {
	private HashMap<String, Object> attributes = null;

	public Object getAttribute(String key) {
		return attributes == null ? null : attributes.get(key);
	}

	public void setAttribute(String key, Object val) {
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
		}
		attributes.put(key, val);
	}
}
