package migration.merging;

import java.util.List;
import java.util.Stack;

import migration.parsing.Product;

public class RepositoryAnalyzer {
	public static void analyze(List<Product> products, SPLrepository repository) {
		List<Asset> list = repository.getAllAssets();
		
		int total = list.size();
		int unique = 0;
		int shared = 0;
		int common = 0;
		int platform = 0;
		int deterministic = 0;
		int non_deterministic = 0;
		int VP = 0;
		int CV = 0;
		int CL = 0;
		int SL = 0;
		int UL = 0;
		
		for(Asset asset : list) {
			asset.count();
			if (asset.count() == products.size()) {
				common++;
				if(asset.hasTheSameRelativePath()) {
					platform++;
					deterministic++;
				} else {
					non_deterministic++;
				}
			} else if (asset.count() > 1) {
				shared++;
				if(asset.hasTheSameRelativePath()) {
					deterministic++;
				} else {
					non_deterministic++;
				}
			} else {
				unique++;
			}
			String[] lines = asset.code().split("\n");
			Stack<Integer> stack = new Stack<Integer>();
			for(int i=0; i<lines.length; i++) {
				String line = lines[i];
				if(line.trim().equals("")) continue;
				if (line.startsWith("//#if")) {
					int n = line.split("\\|").length;
					if(n > products.size()) {
						System.out.println("ERROR");
					} else if(n == products.size()) {
						stack.add(3);
					} else if(n > 1) {
						stack.add(2);
						VP++;
						CV++;
					} else if(n == 1) {
						stack.add(1);
						VP++;
						CV++;
					} else {
						System.out.println("ERROR");
					}
				} else if(line.startsWith("//#elif")) {
					stack.pop();
					int n = line.split("\\|").length;
					if(n > products.size()) {
						System.out.println("ERROR");
					} else if(n == products.size()) {
						stack.add(3);
						System.out.println("Wired");
					} else if(n > 1) {
						stack.add(2);
						CV++;
					} else if(n == 1) {
						stack.add(1);
						CV++;
					} else {
						System.out.println("ERROR");
					}
				} else if(line.startsWith("//#endif")) {
					stack.pop();
				} else {
					if 		(stack.isEmpty()) 	continue;
					if		(stack.peek() == 3) CL++;
					else if (stack.peek() == 2) SL++;
					else if (stack.peek() == 1) UL++;
				}
			}
		}
		
		System.out.println("@ VP (Variation Points)");
		System.out.println("@ CV (Code Variants of variation points)");
		System.out.println("@ CL (Common Lines)");
		System.out.println("@ SL (Shared Lines that is in V related with multi-products)");
		System.out.println("@ UL (Unique Lines that is in V related with only one product)");
		System.out.println(String.format("=> Migrated Products:%d", products.size()));
		System.out.println(String.format("   Assets:%d (Common:%d , Shared:%d , Unique:%d)", total, common, shared, unique));
		System.out.println(String.format("   ReusedAssets:%d (Deterministic:%d , Non-deterministic:%d)", (common+shared), deterministic, non_deterministic));
		System.out.println(String.format("   PlatformAssets:%d", platform));
		System.out.println(String.format("   VP:%d, CV:%d, CL:%d, SL:%d, UL:%d", VP, CV, CL, SL, UL));
	}
}
