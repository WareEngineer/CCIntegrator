package migration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import io.FileExplorer;
import migration.clustering.CloneSet;
import migration.clustering.classifier.Classifier;
import migration.clustering.classifier.ClassifierBasedOnMethodSignature;
import migration.clustering.measure.Measure;
import migration.clustering.measure.MeasureBasedOnSortedLine;
import migration.filtering.Portfolio;
import migration.fomatter.JavaCodeAnalyzer;
import migration.fomatter.JavaCodeFormatter;
import migration.merging.Asset;
import migration.merging.RepositoryAnalyzer;
import migration.merging.SPLrepository;
import migration.merging.merger.CodeMerger;
import migration.optimizing.ActiveFileFinder;
import migration.parsing.JavaFile;
import migration.parsing.Product;
import preprocess.Preprocessor;

public class CCIntegrator {
	private List<String> roots;
	private List<Product> products;
	private List<CloneSet> clusters;
	private SPLrepository repository;
	
	public void init() {
		Preprocessor preprocessor = new Preprocessor();
		preprocessor.loadProducts();
		preprocessor.copyProductsToRepogitory();
		preprocessor.deleteClassFile();
		preprocessor.decomposeJavaFileWithMultipleTopLevelClasses();
		roots = preprocessor.getProductRootPaths();
	}
	
	public void parse() {
		System.out.println("Parsing Java Source Codes ...");
		int total = 0;
		products = new LinkedList<Product>();
		for(String root : roots) {
			System.out.println("   TARGET PRODUCT: " + root);
			Product product = new Product(root);
			products.add(product);
			total += product.sizeOfSourceFiles();
		}
		System.out.println("   Total Files: " + total);
	}
	
	public void deleteDeadCode() {
		System.out.println("Deleting Dead Codes...");
		int total = 0;
		int remainder = 0;
		int deletion = 0;
		for(Product p : products) {
			System.out.println("TARGET PRODUCT: " + p.getRootPath());
			Set<JavaFile> activeFiles = ActiveFileFinder.find(p);
			int sizeOfDeadFiles = p.sizeOfSourceFiles() - activeFiles.size();
			System.out.println("=> All Files: " + p.sizeOfSourceFiles());
			System.out.println("=> Dead Files: " + sizeOfDeadFiles);
			System.out.println("=> Active Files: " + activeFiles.size());
			if(sizeOfDeadFiles == 0) continue;
			System.out.println("Do you want to delete the UNREACHABLE_CLASSES? [y/n]");
			String answer = "";
			while(!answer.equals("y") && !answer.equals("n")) {
				answer = new Scanner(System.in).nextLine();
			}
			total += p.sizeOfSourceFiles();
			if(answer.equals("n")) {
				remainder += p.sizeOfSourceFiles();
				continue;
			}
			remainder += activeFiles.size();
			p.deleteAllFilesExcept(activeFiles);
			deletion += sizeOfDeadFiles;
		}
		System.out.println(String.format("=> Out of a total of %d files that make up the product family,", total));
		System.out.println(String.format("   %d files were deleted because they were dead files.", deletion));
		System.out.println(String.format("=> Total active files: %d", remainder));
	}
	
	public void decidePortfolio() {
		System.out.println("Deciding the scope of the migration...");
		System.out.print("Please enter the threshold for clustering: ");
		double threshold = -1;
		while(threshold<0 || threshold>1.0) {
			threshold = new Scanner(System.in).nextDouble();
		}
		int step = 1;
		double EPR;
		System.out.println("@ VMS (Variable Method Signature)");
		System.out.println("@ CMS (Common Method Signature)");
		System.out.println("@ EPR (Expected Platform Reusability)");
		while(!products.isEmpty()) {
			Portfolio portfolio = new Portfolio(products);
			EPR = portfolio.estimateExpectedPlatfomReusability();
			String message = "";
			if(threshold <= EPR) {
				message = "EPR Become above the required threshold.";
				System.out.println(String.format("=> Step%d. %s %s", step, portfolio.toString(), message));
				break;
			} else {
				Product p = portfolio.getProductWithMinSimilarity();
				products.remove(p);
				message = String.format("Exclude a product with Min_similarity. -%s", p.getName());
				System.out.println(String.format("=> Step%d. %s %s", step, portfolio.toString(), message));
			}
			step++;
		}
		System.out.println("=> List of products to be migrated");
		for(int i=0; i<products.size(); i++) {
			Product p = products.get(i);
			System.out.println(String.format("   %d. %s", (i+1), p.getName()));
		}
	}
	
	
	public void formatCodingStyle(boolean isRearranged) {
		System.out.println("Formmating code styles in all java files ...");
		List<Product> formattedProducts = new LinkedList<Product>();
		System.out.println("=> BEFORE: The original codes");
		System.out.println(JavaCodeAnalyzer.classifyLineByType(products));
		System.out.println("=> Apply single coding style to products.");
		for(Product p : products) {
			System.out.println(String.format("   TARGET PRODUCT: %s", p.getName()));
			String root = p.getRootPath();
			for(String path : FileExplorer.collectJavaFiles(root)) {
				JavaCodeFormatter.format(path, isRearranged);
			}
			Product product = new Product(root);
			formattedProducts.add(product);
		}
		System.out.println("=> AFTER: The formatted codes");
		System.out.println(JavaCodeAnalyzer.classifyLineByType(formattedProducts));
		products = formattedProducts;
	}
	
	public void clustering() {
		System.out.println("Clustering Code...");
		System.out.print("Please enter the threshold for clustering: ");
		double threshold = -1;
		while(threshold<0 || threshold>1.0) {
			threshold = new Scanner(System.in).nextDouble();
		}
		Measure measure = new MeasureBasedOnSortedLine();
		Classifier classifier = new ClassifierBasedOnMethodSignature(products, measure, threshold);
		Set<CloneSet> set = classifier.getClusters();
		clusters = new LinkedList<CloneSet>(set);
		int[] counts = new int[products.size()+1];
		for(CloneSet cs : clusters) {
			int size = cs.NOC();
			counts[size]++;
		}
		System.out.println("@ NOC (the Number Of Clusters)");
		System.out.println("=> The number of products: " + products.size());
		System.out.println("=> Total Clusters: " + clusters.size());
		for(int i=1; i<=products.size(); i++) {
			System.out.println(String.format("   NOC consisting of %d cloned files: %d", i, counts[i]));
		}
	}
	
	public void merging() {
		System.out.println("Merging Code...");
		repository = new SPLrepository(products);
		Collections.sort(clusters, (a,b) -> b.NOC()-a.NOC());
		Queue<CloneSet> queue = new LinkedList<CloneSet>();
		queue.addAll(clusters);
		String type;
		while( !queue.isEmpty() ) {
			CloneSet cluster = queue.poll();
			
			if(CodeMerger.isLargerNOCthanNOP(cluster)) {
				System.out.println("In a cluster, the number of Classes is larger than the number of Products. So the cluster is decomposed into clusters for each class.");
				cluster.getChains().forEach(chain -> {
					CloneSet singleCluster = new CloneSet(chain);
					queue.add(singleCluster);
				});
				continue;
			}
			
			if (cluster.getChains().size() == products.size()) {
				type = "common";
			} else if (cluster.getChains().size() > 1) {
				type = "variable";
			} else {
				type = "unique";
			}
			
			Asset asset = new Asset(cluster, type);
			repository.addAsset(asset);
			
			if(asset.hasTheSameRelativePath() == false) {
				repository.log(cluster);
			}
		}
		RepositoryAnalyzer.analyze(products, repository);
	}

}
