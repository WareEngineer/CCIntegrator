package migration.clustering.classifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.clustering.measure.Measure;
import migration.clustering.measure.MeasureBasedOnSortedLine;
import migration.parsing.JavaFile;
import migration.parsing.Product;
import migration.parsing.entity.Clazz;

@FunctionalInterface
interface Supplier {
    String get(Clazz clazz);
}

public abstract class Classifier {
	protected Measure measure;
	protected double threshold;
	protected List<Product> products;
	protected Set<CloneSet> clusters;
	
	public Classifier(List<Product> products, Measure measure, double threshold) {
		this.measure = measure;
		this.products = products;
		this.threshold = threshold;
		this.apply();
	}
	public abstract Set<CloneSet> detect(Set<Chain> chains);
	protected abstract Set<CloneSet> determine(Set<CloneSet> candidates, double threshold);

	public Set<CloneSet> getClusters() {
		return clusters;
	}
	
	final protected void apply() {
		Set<Chain> chains = new HashSet<Chain>();
		for(Product product : products) {
			for(JavaFile source : product.getSources()) {
				for(Clazz clazz : source.getClazzs()) {
					Chain chain = new Chain(product, source, clazz);
					chains.add(chain);
				}
			}
		}
		Set<CloneSet> candidates = detect(chains);
		clusters = determine(candidates, threshold);
	}
	
	final protected Set<CloneSet> detectInner(Set<Chain> chains) {
		Set<Chain> innerChains = new HashSet<Chain>();
		for(Chain chain : chains) {
			for(Clazz innerClazz : chain.getClazz().getInners()) {
				Chain innerChain = new Chain(chain.getProduct(), chain.getJavaFile(), innerClazz);
				innerChains.add(innerChain);
			}
		}
		return detect(innerChains);
	}

	final public double quantify(CloneSet cluster) {
		Set<Chain> chains = cluster.getChains();
		String[] codes = new String[chains.size()];
		int i=0;
		for(Chain chain : chains) {
			codes[i++] = chain.getClazz().toString();
		}
		return measure.calc(codes);
	}
	
	final public String getInfo() {
		String sourceCodeBase = getPrecisionAndRecall((c)->c.toString());
		String methodSignatureBase = getPrecisionAndRecall((c)-> {
			List<String> list = new LinkedList<String>();
			c.getMethods().forEach(m -> list.add(m.getSignature()));
			return String.join("\n", list);
		});
		StringBuffer buffer = new StringBuffer();
		buffer.append("SourceCodeBase: " + sourceCodeBase);
		buffer.append("  ");
		buffer.append("MethodSignatureBase: " + methodSignatureBase);
		buffer.append("  ");
		buffer.append(getSimilarityInfo());
		return buffer.toString();
	}
	
	final public String getSimilarityInfo() {
		double acc = 0.0;
		double min = 1.0;
		int count = 0;
		for(CloneSet sc : clusters) {
			if(sc.NOC() < 2) continue;
			String[] codes = new String[sc.NOC()];
			int k = 0;
			for(Chain chain : sc.getChains()) {
				codes[k] = chain.getClazz().toString();
				k++;
			}
			double sim = measure.calc(codes);
			if(sim < min) {
				min = sim;
			}
			acc += sim;
			count++;
		}
		double average = (acc/count);
		return String.format("ClusterSimilarity: [Avg.: %.3f, Min.: %.3f]", average, min);
	}
	
	final private String getPrecisionAndRecall(Supplier supplier) {
		Map<Clazz, Set<Clazz>> map = new HashMap<Clazz, Set<Clazz>>();
		Set<Chain> chains = new HashSet<Chain>();
		for(Product product : products) {
			for(JavaFile source : product.getSources()) {
				for(Clazz clazz : source.getClazzs()) {
					chains.add(new Chain(product, source, clazz));
					map.put(clazz, new HashSet<Clazz>());
				}
			}
		}
		
		double actualP = 0;
//		double actualN = 0;
		Measure calculator = new MeasureBasedOnSortedLine();
		Set<Chain> marked = new HashSet<Chain>();
		for(Chain from : chains) {
			marked.add(from);
			String[] codes = new String[2];
			codes[0] = supplier.get(from.getClazz());
			if(codes[0].isEmpty()) continue;
			for(Chain to : chains) {
				if(marked.contains(to) || from.getProduct()==to.getProduct()) {
					continue;
				}
				codes[1] = supplier.get(to.getClazz());
				if(codes[1].isEmpty()) continue;
				double similarity = calculator.calc(codes);
				if(similarity < threshold) {
//					actualN++;
				} else {
					map.get(from.getClazz()).add(to.getClazz());
					map.get(to.getClazz()).add(from.getClazz());
					actualP++;	
				}
			}
		}

		double TP = 0;
		double FP = 0;
		for(CloneSet cluster : clusters) {
			marked = new HashSet<Chain>();
			for(Chain from : cluster.getChains()) {
				marked.add(from);
				for(Chain to : cluster.getChains()) {
					if(marked.contains(to) || from.getProduct()==to.getProduct()) {
						continue;
					}
					Set<Clazz> relatedClazzs = map.get(from.getClazz());
					Clazz target = to.getClazz();
					if(relatedClazzs.contains(target)) {
						TP++;
					} else {
						FP++;
					}
				}
			}
		}
		
		double FN = actualP - TP;
		double precision = TP / (TP+FP);
		double recall = TP / (TP+FN);
		
//		return String.format("[ActualPositive:%4.0f, ActualNegative:%4.0f, TP:%4.0f, FP:%4.0f, FN:%4.0f, Precision:%3.2f, Recall:%3.2f]", actualP, actualN, TP, FP, FN, precision, recall);
		return String.format("[Precision:%3.2f, Recall:%3.2f]", precision, recall);
	}
	
}
