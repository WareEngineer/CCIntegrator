package preprocess;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import io.FileExplorer;
import io.MyFileCopier;
import io.MyFileReader;
import io.MyFileRemover;
import preprocess.decomposing.JavaCodeDecomposer;

public class Preprocessor {
	private List<String> roots = new LinkedList<String>();
	private final String example 
	    = "\n===<file name>=================================="
		+ "\nproducts.txt"
		+ "\n===<file text>=================================="
		+ "\nC:\\[your_directory_path]\\[root_of_1th_product]"
		+ "\nC:\\[your_directory_path]\\[root_of_2th_product]"
		+ "\n..."
		+ "\nC:\\[your_directory_path]\\[root_of_nth_product]"
		+ "\n================================================"
		+ "\nEnter the file-path: C:\\...\\products.txt"
		+ "\n"
		+ "\nTo apply CCIntegrator to your own product variants developed with Clone-and-Own approach,"
		+ "\n   you create a text file recording paths of products and enter the path of the text file as above."
		+ "\nWe offer two simple examples referring to the Antenna-based FeatureIDE examples."
		+ "\n   1) .\\examples\\elevator.txt"
		+ "\n   2) .\\examples\\hello_world.txt";
	
	@SuppressWarnings("resource")
	public void loadProducts() {
		System.out.println(example);
		System.out.println();
		System.out.println("Enter the file-path that is recored the products' root-path like the avobe example.");
		String path;
		while(true) {
			System.out.print("$ ");
			path = new Scanner(System.in).next();
			if(new File(path).exists()) break;
		}
		String text = MyFileReader.read(path);
		System.out.println("Loading products' files ...");
		int total = 0;
		for(String productPath : text.split("\n")) {
			productPath = productPath.trim();
			if(productPath.isEmpty() == false) {
				total++;
				File file = new File(productPath);
				roots.add(file.getAbsolutePath());
				System.out.println("   " + file.getAbsolutePath());
			}
		}
		System.out.println("   Total Products: " + total);
	}

	public void copyProductsToRepogitory() {
		System.out.println("Copying all products to a temporary directory ...");
		String repository = ".\\temp";
		MyFileRemover.remove(repository);
		List<String> list = new LinkedList<String>();
		for(String from : roots) {
			int pos = from.lastIndexOf('\\');
			String productName = from.substring(pos+1);
			String path = repository + "\\" + productName;
			String to = new File(path).getAbsolutePath();
			MyFileCopier.copyDiretory(from, to);
			list.add(to);
		}
		roots = list;
	}

	public void deleteClassFile() {
		System.out.println("Deleting files with the class extension(.class) ...");
		for(String root : roots) {
			for(String path : FileExplorer.collectClassFiles(root)) {
				File file = new File(path);
				file.delete();
			}
		}
	}
	
	public void decomposeJavaFileWithMultipleTopLevelClasses() {
		System.out.println("Decomposing java files that have multiple top-level classes in one file ...");
		for(String root : roots) {
			for(String path : FileExplorer.collectJavaFiles(root)) {
				JavaCodeDecomposer.decompose(path);
			}
		}
	}

	public List<String> getProductRootPaths() {
		return roots;
	}
}
