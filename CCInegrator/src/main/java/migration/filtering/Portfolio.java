package migration.filtering;

import java.util.LinkedList;
import java.util.List;

import migration.parsing.Product;

public class Portfolio {
	private ProductFamilyMeasure measure;
	private List<Product> list;
	private Product minProduct;
	
	public Portfolio(List<Product> products) {
		list = new LinkedList<Product>(products);
		minProduct = list.get(0);
		measure = new ProductFamilyMeasure(list);
		long min = measure.calculateProductSimilarity(minProduct);
		for(Product p : list) {
			long similarity = measure.calculateProductSimilarity(p);
			if(similarity < min) {
				minProduct = p;
				min = similarity;
			}
		}
	}
	
	public double estimateExpectedPlatfomReusability() {
		double CMS = measure.CMS();
		double VMS = measure.VMS();
		double MVMS = VMS / measure.NOP();
		return (CMS) / (CMS+MVMS);
	}
	
	public Product getProductWithMinSimilarity() {
		return minProduct;
	}
	
	@Override
	public String toString() {
		double EPR = this.estimateExpectedPlatfomReusability();
		return String.format("(VMS:%4d, CMS:%4d, EPR:%.2f)", measure.VMS(), measure.CMS(), EPR);
	}
}
