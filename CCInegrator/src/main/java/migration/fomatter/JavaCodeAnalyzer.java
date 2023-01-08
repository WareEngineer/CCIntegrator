package migration.fomatter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import migration.parsing.JavaFile;
import migration.parsing.Product;

public class JavaCodeAnalyzer {

	public static String classifyLineByType(List<Product> products) {
		int loc = 0;
		Set<String> universe = new HashSet<String>();
		Set<String> common = null;
		for(Product product : products) {
			Set<String> intersection = new HashSet<String>();
			for(JavaFile file : product.getSources()) {
				for(String line : file.toString().split("\n")) {
					if(line.trim().isEmpty()) continue;
					loc++;
					universe.add(line);
					if(common==null || common.contains(line)) {
						intersection.add(line);
					}
				}
			}
			common = intersection;
		}
		int allLines = universe.size();
		int commonLines = (common==null) ? 0 : common.size();
		int variableLines = allLines - commonLines;
		return String.format(
				"   LOC of all products: %d \n   Distinct Lines: %d (Common: %d, Variable: %d)", 
				loc, allLines, commonLines, variableLines
				);
	}

}
