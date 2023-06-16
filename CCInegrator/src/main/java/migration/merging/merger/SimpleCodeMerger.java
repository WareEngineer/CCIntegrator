package migration.merging.merger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.merging.entity.AnnotatedCode;
import migration.parsing.JavaFile;

public class SimpleCodeMerger {

	public static String makeDescription(CloneSet cluster) {
		List<String> list = new LinkedList<String>();
		cluster.getChains().forEach(chain -> list.add(chain.getJavaFile().getPath().replace('\\', '/')));
		Collections.sort(list);
		String description = String.format("/*\n%s\n*/", String.join("\n", list));
		return description;
	}

	public static boolean isLargerNOCthanNOP(CloneSet cluster) {
		if(cluster.NOP() < cluster.NOC()) {
			System.out.println("In a cluster, the number of classes is larger than the number of product.");
			return true;
		}
		return false;
	}

	public static boolean hasTheSameRelativePath(CloneSet cluster) {
		Set<String> set = new HashSet<String>();
		cluster.getChains().forEach(chain -> {
			String relativePath = extractRelativeFilePath(chain);
			set.add(relativePath);
		});
		return set.size() == 1;
	}

	public static String extractRelativeFilePath(Chain chain) {
		String productPath = chain.getProduct().getRootPath();
		String filePath = chain.getJavaFile().getPath();
		String relativePath = filePath.replace(productPath, "");
		return relativePath;
	}

	public static AnnotatedCode merge(CloneSet cluster) {
		if(cluster.NOP() < cluster.NOC()) {
			System.out.println("It is impossilble to merge files in the cluster.");
			return null;
		}

		Set<String> ids = new HashSet<String>();
		Map<String, String> sources = new HashMap<String, String>();
		for(Chain chain : cluster.getChains()) {
			JavaFile file = chain.getJavaFile();
			String id = chain.getProduct().getName();
			String source = file.toString();
			sources.put(id, source);
			ids.add(id);
		}

		AnnotatedCode code = new AnnotatedCode(ids);
		code.append(sources);
		
		return code;
	}
	
}
