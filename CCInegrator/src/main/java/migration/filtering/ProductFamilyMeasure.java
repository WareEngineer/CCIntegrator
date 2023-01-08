package migration.filtering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import migration.parsing.JavaFile;
import migration.parsing.Product;
import migration.parsing.entity.Clazz;
import migration.parsing.entity.Method;

public class ProductFamilyMeasure {
	private Map<String, Set<Product>> relevantProducts = new HashMap<String, Set<Product>>();
	private Map<Product, Set<String>> relevantSignatures = new HashMap<Product, Set<String>>();
	private int NOP;
	private int CMS;
	private int VMS;

	public ProductFamilyMeasure(List<Product> products) {
		NOP = products.size();
		for(Product product : products) {
			Set<String> signatures = new HashSet<String>();
			for(JavaFile source : product.getSources()) {
				for(Clazz clazz : source.getClazzs()) {
					Stack<Clazz> stack = new Stack<Clazz>();
					stack.add(clazz);
					while(!stack.isEmpty()) {
						Clazz c = stack.pop();
						for(Clazz inner : c.getInners()) {
							stack.add(inner);
						}
						for(Method method : clazz.getMethods()) {
							String signature = method.getSignature();
							signatures.add(signature);
							if(relevantProducts.containsKey(signature) == false) {
								relevantProducts.put(signature, new HashSet<Product>());
							}
							relevantProducts.get(signature).add(product);
						}
					}
				}
			}
			relevantSignatures.put(product, signatures);
		}
		int[] counts = new int[NOP+1];
		for(Set<Product> set : relevantProducts.values()) {
			int size = set.size();
			counts[size]++;
		}
		for(int i=1; i<=NOP; i++) {
			if(i<NOP) VMS += counts[i];
			else 	  CMS += counts[i];
		}
	}
	
	public int calculateProductSimilarity(Product p) {
		int similarity = 0;
		for(String ms : relevantSignatures.get(p)) {
			int n = relevantProducts.get(ms).size();
			if(n>1) similarity += (n-1)*(n-1);
			else similarity--;
		}
		return similarity;
	}
	
	public int NOP() {
		return NOP;
	}
	
	public int CMS() {
		return CMS;
	}
	
	public int VMS() {
		return VMS;
	}
}
