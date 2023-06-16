import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import analyzer.Analyzer;
import generation.Generator;
import migration.CCIntegrator;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] agrs) {
		List<String> options = new LinkedList<String>();
		options.add("Migrating from CAO based products to SPL repository.");
		options.add("Generating products from migrated SPL repository.");
		options.add("Exit");
		
		int choice;
		do {
			System.out.println("= MENU =================================================");
			System.out.println("1. Migrating from CAO based products to SPL repository.");
			System.out.println("2. Generating products from migrated SPL repository.");
			System.out.println("3. EXIT");
			System.out.println("========================================================");
			do {
				System.out.print("$ ");
				choice = new Scanner(System.in).nextInt();
			} while(choice < 1 || 3 < choice);

			if(choice == 1) {
				CCIntegrator cci = new CCIntegrator();
				cci.init();
				cci.parse();
				cci.deleteDeadCode();			// Step 1.1
				cci.decidePortfolio();			// Step 1.2 & 1.3
				cci.formatCodingStyle(true);	// Step 2.1
				cci.clustering();				// Step 2.2
				cci.merging();					// Step 3.1 & 3.2
				cci.buildingModel();			// Step 4.1
				Analyzer.analyzeAssetRepository();
			} else if(choice == 2) {
				Generator.setConfiguration();
				Generator.generateProduct();
			} else {
				break;
			}
		} while(true);
	}
}
