package migration.merging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.merging.entity.AnnotatedCode;
import migration.merging.merger.CodeMerger;
import migration.merging.merger.SimpleCodeMerger;

public class Asset {
	private static int k = 0;
	private String name;
	private String code;
	private String type;
	private Map<String, String> files;
	private boolean hasTheSameRelativePath;
	private int count;
	
	public Asset(CloneSet cluster, String type) {
		k++;
		
		this.type = type;
		name = String.format("asset%d", k);
		code = getCode(cluster);

		files = new HashMap<String, String>();
		for(Chain chain : cluster.getChains()) {
			String owner = chain.getProduct().getName();
			String relativeFilePath = CodeMerger.extractRelativeFilePath(chain);
			files.put(owner, relativeFilePath);
		}
		
		hasTheSameRelativePath = CodeMerger.hasTheSameRelativePath(cluster);
		count = cluster.getChains().size();
	}
	
	private String getCode(CloneSet cluster) {
		String comment = CodeMerger.makeDescription(cluster);
//		AnnotatedCode annotatedCode = CodeMerger.merge(cluster);
		AnnotatedCode annotatedCode = SimpleCodeMerger.merge(cluster);
		String[] lines = annotatedCode.toString().split("\n");
		LinkedList<String> list = new LinkedList<String>();
		for(String line : lines) {
			if(line.trim().isEmpty()) continue;
			list.add(line);
		}
		return String.format("%s\n%s", comment, String.join("\n", list));
	}

	public String name() {
		return name;
	}
	
	public String code() {
		return code;
	}
	
	public String type() {
		return type;
	}
	
	public boolean hasTheSameRelativePath() {
		return hasTheSameRelativePath;
	}
	
	public int count() {
		return count;
	}
	
	public Map<String, String> files() {
		return files;
	}
}
