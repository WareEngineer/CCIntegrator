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
import migration.parsing.entity.Clazz;

public class ClassifierBasedOnClassName extends Classifier {

	public ClassifierBasedOnClassName(List<Product> products, Measure measure, double threshold) {
		super(products, measure, threshold);
	}

	@Override
	public Set<CloneSet> detect(Set<Chain> chains) {
		Map<String, Set<Chain>> map = new HashMap<String, Set<Chain>>();
		for(Chain chain : chains) {
			Clazz clazz = chain.getClazz();
			String name = clazz.getName();
			if(!map.containsKey(name)) map.put(name, new HashSet<Chain>());
			map.get(name).add(chain);
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
		Set<CloneSet> clusters = new HashSet<CloneSet>();
		Set<Chain> chains = new HashSet<Chain>();
		for(CloneSet candidate : candidates) {
			CloneSet cluster = new CloneSet(candidate.getChains(), determine(candidate.getInners(), threshold));
			if(quantify(cluster) < threshold) {
				for(Chain chain : cluster.getChains()) {
					Set<Chain> set = new HashSet<Chain>();
					set.add(chain);
					clusters.add(new CloneSet(set, determine(detectInner(set), threshold)));
					chains.add(chain);
				}
			} else {
				clusters.add(cluster);
				chains.addAll(cluster.getChains());
			}
		}
		
		return clusters;
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
