package migration.clustering.classifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.clustering.HPQitem;
import migration.clustering.HierarchicalPriorityQueue;
import migration.clustering.measure.Measure;
import migration.parsing.Product;
import migration.parsing.entity.Clazz;
import migration.parsing.entity.Method;

public class ClassifierBasedOnMethodSignature extends Classifier {

	public ClassifierBasedOnMethodSignature(List<Product> products, Measure measure, double threshold) {
		super(products, measure, threshold);
	}

	@Override
	public Set<CloneSet> detect(Set<Chain> chains) {
		Map<String, Set<Chain>> map = new HashMap<String, Set<Chain>>();
		Set<Chain> processed = new HashSet<Chain>();
		for(Chain chain : chains) {
			Clazz clazz = chain.getClazz();
			if(clazz.getMethods().iterator().hasNext()) processed.add(chain);
			
			String name = clazz.getName();
			if(!map.containsKey(name)) map.put(name, new HashSet<Chain>());
			map.get(name).add(chain);
			
			String fullName = clazz.getFullName();
			if(!map.containsKey(fullName)) map.put(fullName, new HashSet<Chain>());
			map.get(fullName).add(chain);
			
			for(Method method : clazz.getMethods()) {
				String signature = method.getSignature();
				if(!map.containsKey(signature)) map.put(signature, new HashSet<Chain>());
				map.get(signature).add(chain);
			}
			
			for(Method constructor : clazz.getConstructors()) {
				String signature = constructor.getSignature();
				if(!map.containsKey(signature)) map.put(signature, new HashSet<Chain>());
				map.get(signature).add(chain);
			}
		}
		
		Set<CloneSet> clusters = new HashSet<CloneSet>();
		for(Set<Chain> set : map.values()) {
			CloneSet cluster = new CloneSet(set, detectInner(set));
			clusters.add(cluster);
		}
		
		for(Chain e : chains) {
			if(!processed.contains(e)) {
				Set<Chain> set = new HashSet<Chain>();
				set.add(e);
				clusters.add(new CloneSet(set, detectInner(set)));
			}
		}
		
		return clusters;
	}

	@Override
	protected Set<CloneSet> determine(Set<CloneSet> candidates, double threshold) {
		int max = 0;
		Set<String> set = new HashSet<String>();
		Set<Chain> chains = new HashSet<Chain>();
		for(CloneSet candidate : candidates) {
			String config = candidate.ID();
			if(set.contains(config)) continue;
			if(candidate.NOP() > max) max = candidate.NOP();
			for(Chain chain : candidate.getChains()) {
				chains.add(chain);
				set.add(config);
			}
		}

		HierarchicalPriorityQueue<HPQitem<CloneSet>> hpq = new HierarchicalPriorityQueue<HPQitem<CloneSet>>(max);
		for(CloneSet candidate : candidates) {
			if(candidate.NOC() > candidate.NOP() || candidate.NOC() < 2) continue;
			CloneSet cluster = new CloneSet(candidate.getChains(), determine(candidate.getInners(), threshold));
			HPQitem<CloneSet> item = new HPQitem<CloneSet>(cluster, cluster.NOP(), quantify(cluster));
			if(item.value() < (threshold*0.8)) continue;
			hpq.put(item);
		}
		Set<CloneSet> clusters = new HashSet<CloneSet>();
		Set<Chain> processed = new HashSet<Chain>();
		while(!hpq.isEmpty()) {
			HPQitem<CloneSet> item = hpq.poll();
			CloneSet candidate = item.content();
			Set<Chain> diff = new HashSet<Chain>();
			for(Chain chain : candidate.getChains()) {
				if(processed.contains(chain) == false) {
					diff.add(chain);
				}
			}
			
			if(diff.size() < 2) {
				continue;
			} else if(diff.size() == candidate.getChains().size()) {
				if(item.value() >= threshold) {
					clusters.add(candidate);
					processed.addAll(candidate.getChains());
				}
			} else {
				CloneSet cluster = new CloneSet(diff, determine(detectInner(diff), threshold));
				String config = cluster.ID();
				if(set.contains(config)) continue;
				set.add(config);
				HPQitem<CloneSet> newItem = new HPQitem<CloneSet>(cluster, cluster.NOP(), quantify(cluster));
				hpq.put(newItem);
			}
		}
		
		for(Chain chain : chains) {
			if(processed.contains(chain) == false) {
				clusters.add(new CloneSet(chain));
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
//		Map<String, Set<Method>> map = new HashMap<String, Set<Method>>();
//		for(Chain chain : cluster.getChains()) {
//			Clazz clazz = chain.getClazz();
//			for(Method method : clazz.getMethods()) {
//				String signature = method.getSignature();
//				if(!map.containsKey(signature)) map.put(signature, new HashSet<Method>());
//				map.get(signature).add(method);
//			}
//		}
//		
//		for(String signature : map.keySet()) {
//			Set<Method> methods = map.get(signature);
//			String[] codes = new String[methods.size()];
//			int i=0;
//			for(Method method : methods) {
//				codes[i++] = method.toString();
//			}
//			double w = (double)methods.size() / (double)cluster.NOC();
//			Info info = measure.calc(codes);
//			entire += info.getEntier();
//			common += w * info.getCommon();
//		}
//		
//		for(CloneSet inner : cluster.getInners()) {
//			Info innerInfo = recur(inner);
//			double w = (double)inner.NOC() / (double)cluster.NOC();
//			entire += innerInfo.getEntier();
//			common += w * innerInfo.getCommon();
//		}
//		return new Info(entire, common);
//	}

}
