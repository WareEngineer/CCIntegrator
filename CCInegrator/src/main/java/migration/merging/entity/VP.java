package migration.merging.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VP {
	private Map<String, List<String>> ids = new HashMap<String, List<String>>();
	private Map<String, String> variants = new HashMap<String, String>();
	
	public void append(String id, String variant) {
		if(variant.equals("")) return;
		if(ids.containsKey(variant) == false) {
			ids.put(variant, new LinkedList<String>());
		}
		ids.get(variant).add(id);
		variants.put(id, variant);
	}

	public Set<String> getIds() {
		return variants.keySet();
	}
	
	public String getVariant(String id) {
		return variants.get(id);
	}
	
	public int size() {
		return variants.size();
	}

	public boolean isEmpty() {
		return variants.isEmpty();
	}
	
	@Override
	public String toString() {
		for(String variant : ids.keySet()) {
			Collections.sort(ids.get(variant));
		}
		StringBuilder buffer = new StringBuilder();
		for(String v : ids.keySet()) {
			if(buffer.length() == 0) buffer.append("//#if ");
			else 					 buffer.append("\n//#elif ");
			buffer.append(String.join(" | ", ids.get(v)));
			buffer.append("\n");
			buffer.append(v);
		}
		buffer.append("\n//#endif");
		return buffer.toString();
	}
}
