package migration.clustering.classifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.clustering.measure.Measure;
import migration.parsing.Product;

public class ClassifierBasedOnRelativeFilePath extends Classifier {

	public ClassifierBasedOnRelativeFilePath(List<Product> products, Measure measure, double threshold) {
		super(products, measure, threshold);
	}

	@Override
	public Set<CloneSet> detect(Set<Chain> chains) {
		Map<String, Set<Chain>> map = new HashMap<String, Set<Chain>>();
		for(Chain chain : chains) {
			String rootPath = chain.getProduct().getRootPath();
			String absolutePath = chain.getJavaFile().getPath();
			String relativePath = absolutePath.replace(rootPath, "#");
			if(!map.containsKey(relativePath)) map.put(relativePath, new HashSet<Chain>());
			map.get(relativePath).add(chain);
		}
		
		Set<CloneSet> clusters = new HashSet<CloneSet>();
		for(Set<Chain> set : map.values()) {
			CloneSet cluster = new CloneSet(set, detectInner(set));
			clusters.add(cluster);
		}
		
		return clusters;
	}
	
	@Override
	protected Set<CloneSet> determine(Set<CloneSet> candidates, double threshold) {
		return candidates;
	}

//	@Override
//	public double quantify(CloneSet cluster) {
//		Info info = recur(cluster);
//		return (double)info.getCommon() / (double)info.getEntier();
//	}
//
//	private Info recur(CloneSet cluster) {
//		int entire = 0;
//		int common = 0;
//		Set<Chain> chains = cluster.getChains();
//		String[] codes = new String[chains.size()];
//		int i=0;
//		for(Chain chain : chains) {
//			codes[i++] = chain.getClazz().toStringWithoutInnerClassCode();
//		}
//		Info info = measure.calc(codes);
//		entire += info.getEntier();
//		common += info.getCommon();
//		for(CloneSet inner : cluster.getInners()) {
//			Info innerInfo = recur(inner);
//			double w = (double)inner.NOC() / (double)cluster.NOC();
//			entire += innerInfo.getEntier();
//			common += w * innerInfo.getCommon();
//		}
//		return new Info(entire, common);
//	}

}
