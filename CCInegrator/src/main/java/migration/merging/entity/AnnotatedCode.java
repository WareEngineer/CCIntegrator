package migration.merging.entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.merging.merger.LCS;

public class AnnotatedCode {
	private List<String> ids = null;
	private List<Object> segments = new LinkedList<Object>();

	public AnnotatedCode() { }
	
	public AnnotatedCode(Set<String> ids) {
		this.ids = new LinkedList<String>(ids);
		Collections.sort(this.ids);
	}

	public void append(AnnotatedCode other) {
		this.segments.add(other);
	}
	
	public void append(Map<String, String> map) {
		String[] ids = new String[map.keySet().size()];
		String[][] codes = new String[map.keySet().size()][];
		int count = 0;
		for(String id : map.keySet()) {
			String code = map.get(id);
			ids[count] = id;
			codes[count] = code.split("\n");
			count++;
		}
		
		String[] commonLines = LCS.get(codes);
		int[] pos = new int[codes.length];
		for(int i=0; i<commonLines.length; i++) {
			String commonLine = commonLines[i];
			VP vp = new VP();
			for(int j=0; j<ids.length; j++) {
				List<String> variableLines = new LinkedList<String>();
				while(codes[j][pos[j]].equals(commonLine) == false) {
					variableLines.add(codes[j][pos[j]]);
					pos[j]++;
				}
				pos[j]++;
				if(variableLines.isEmpty() == false) {
					vp.append(ids[j], String.join("\n", variableLines));
				}
			}
			if(vp.isEmpty() == false) {
				segments.add(vp);
			}
			segments.add(commonLine);
		}

		VP vp = new VP();
		for(int j=0; j<ids.length; j++) {
			List<String> variableLines = new LinkedList<String>();
			while(pos[j] < codes[j].length) {
				variableLines.add(codes[j][pos[j]]);
				pos[j]++;
			}
			if(variableLines.isEmpty() == false) {
				vp.append(ids[j], String.join("\n", variableLines));
			}
		}
		if(vp.isEmpty() == false) {
			segments.add(vp);
		}
	}
	
	public List<VP> VP() {
		List<VP> list= new LinkedList<VP>();
		segments.forEach(e -> {
			if(e instanceof VP) {
				VP vp = (VP) e;
				list.add(vp);
			} else if(e instanceof AnnotatedCode) {
				AnnotatedCode code = (AnnotatedCode) e;
				List<VP> vps = code.VP();
				list.addAll(vps);
			}
		});
		return list;
	}

	@Override
	public String toString() {
		List<String> list = new LinkedList<String>();
		segments.forEach(e -> list.add(e.toString()));
		if(ids == null) {
			return String.join("\n", list);
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("//#if " + String.join(" | ", ids));
		buffer.append("\n");
		buffer.append(String.join("\n", list));
		buffer.append("\n//#endif");
		return buffer.toString();
	}

}
