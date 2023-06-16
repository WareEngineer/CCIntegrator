package migration.optimizing;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import migration.parsing.FileReferenceGraph;
import migration.parsing.JavaFile;
import migration.parsing.Product;

public class ActiveFileFinder {
	
	public static Set<JavaFile> find(Product p) {
		List<JavaFile> main_classes = p.getSourceFilesWithMainMethod();
		int choice = 0;
		if(main_classes.isEmpty()) {
			System.out.println("The product hasn't been optimized because it don't have a main class.");
			return null;
		}
		if(main_classes.size() > 1) {
			System.out.println("There are multiple main classes.");
			for(int i=0; i<main_classes.size(); i++) {
				System.out.println(i+1 + ") " + main_classes.get(i).getPath());
			}
			System.out.println("Please choose the number to be a start point.");
			System.out.print("$ ");
			while(choice<1 || choice>main_classes.size()) {
				choice = new Scanner(System.in).nextInt();
			}
			choice--;
		}

		FileReferenceGraph graph = p.getRefeerenceGraph();
		JavaFile s = main_classes.get(choice);
		Set<JavaFile> marked = new HashSet<JavaFile>();
		marked.add(s);
		dfs(graph, s, marked);
		
		return marked;
	}

	private static void dfs(FileReferenceGraph graph, JavaFile from, Set<JavaFile> marked) {
		for(JavaFile to : graph.getRefferedFiles(from)) {
			if(marked.contains(to)) continue;
			marked.add(to);
			dfs(graph, to, marked);
		}
	}

}
