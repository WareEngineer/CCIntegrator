package generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import generation.activator.AntennaJavaCodeActivator;
import generation.model.Model;
import io.MyFileCopier;
import io.MyFileRemover;
import io.MyFileWriter;
import migration.merging.SPLrepository;

public class Generator {
	private static String config = null;
	private static Map<String, String> mapAssetToFile = null;
	
	public static void setConfiguration() {
		Model model = new Model(SPLrepository.MODEL);
		List<String> portfolio = model.getProductList();
		
		int choice = -1;
		printConfigurableProducts(portfolio);
		while(choice < 0 || choice > portfolio.size()) {
			System.out.print("$ ");
			choice = new Scanner(System.in).nextInt();
		}
		if(choice == 0) config = null;
		else config = portfolio.get(choice-1);
		mapAssetToFile = model.getAssets(config);
	}
	
	private static void printConfigurableProducts(List<String> portfolio) {
		System.out.println("\n= List of product variants you can generate ============");
		System.out.println("0. RETURN MENU");
		for(int i=0; i<portfolio.size(); i++) {
			String name = portfolio.get(i);
			System.out.println(String.format("%d. %s", (i+1), name));
		}
		System.out.println("========================================================");
	}

	public static void generateProduct() {
		if(config == null) {
			System.out.println("End the product creation process.");
			return;
		}
		System.out.println("\nGenerating a product variant that you choose.");
		String root = ".\\products\\" + config;
		MyFileRemover.remove(root);
		copyNonCodeAssets(root);
		reuseCodeAssets(root);
		System.out.println("It is completed to generate a product variant (" + config + ") in the path ("+ root +").");
		System.out.println();
	}

	private static void copyNonCodeAssets(String dest) {
		String src = SPLrepository.getNonSourceCodeRepository(config);
		MyFileCopier.copyDiretory(src, dest);
	}
	
	private static void reuseCodeAssets(String root) {
		List<Integer> numbers = new ArrayList<Integer>();
		for(String name : mapAssetToFile.keySet()) {
			String n = name.replace("asset", "");
			int number = Integer.parseInt(n);
			numbers.add(number);
		}
		Collections.sort(numbers);
		int cnt = 0;
		for(Integer number : numbers) {
			cnt++;
			String name = "asset" + number;
			String asset = SPLrepository.getAsset(name);
			String code = AntennaJavaCodeActivator.activate(asset, config);
			String path = root + mapAssetToFile.get(name);
			MyFileWriter.write(path, code);
			System.out.println(String.format("%6d: %s => (%s)", cnt, name, path));
		}
	}
	
}
