package migration.parsing;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.JavaToken;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import io.FileExplorer;

public class Product {
	private String root;
	private String name;
	private FileReferenceGraph graph;
	private Map<String, JavaFile> sources = new HashMap<String, JavaFile>();
	private Map<String, JavaFilePackage> packages = new HashMap<String, JavaFilePackage>();
	
	public Product(String root) {
		root = new File(root).getAbsolutePath();
		this.root = root;
		this.name = root.substring(root.lastIndexOf('\\')+1);
		for(String file_path : FileExplorer.collectJavaFiles(root)) {
			JavaFile file = new JavaFile(file_path);
			sources.put(file.getPath(), file);
			String package_name = file.getPackageName();
			if(packages.containsKey(package_name) == false) {
				packages.put(package_name, new JavaFilePackage());
			}
			packages.get(package_name).add(file);
		}
		graph = new FileReferenceGraph(sources, packages);
	}
	
	public String getRootPath() {
		return root;
	}
	
	public String getName() {
		return name;
	}

	public FileReferenceGraph getRefeerenceGraph() {
		return graph;
	}
	
	public Iterable<String> getPaths() {
		return sources.keySet();
	}
	
	public JavaFile getSource(String path) {
		return sources.get(path);
	}

	public Iterable<JavaFile> getSources() {
		return sources.values();
	}

	public int sizeOfSourceFiles() {
		return sources.size();
	}
	
	public List<JavaFile> getSourceFilesWithMainMethod() {
		List<JavaFile> list = new LinkedList<JavaFile>();
		for(JavaFile file : sources.values()) {
			if(file.hasMainMethod()) {
				list.add(file);
			}
		}
		return list;
	}

	@SuppressWarnings("resource")
	public void deleteAllFilesExcept(Set<JavaFile> active_files) {
		Map<String, JavaFile> newSources = new HashMap<String, JavaFile>();
		for(JavaFile file : sources.values()) {
			if(active_files.contains(file)) {
				newSources.put(file.getPath(), file);
			} else {
				System.out.println(String.format("   Delete \"%s\"", file.getPath()));
				file.delete();
			}
		}
		renew(newSources);
	}

	public void update(Map<JavaFile, JavaFile> map) {
		for(JavaFile before : map.keySet()) {
			JavaFile after = map.get(before);
			sources.remove(before.getPath());
			sources.put(after.getPath(), after);
		}
		renew(sources);
	}
	
	private void renew(Map<String, JavaFile> newSources) {
		sources = newSources;
		packages = new HashMap<String, JavaFilePackage>();
		for(JavaFile file : newSources.values()) {
			String package_name = file.getPackageName();
			if(packages.containsKey(package_name) == false) {
				packages.put(package_name, new JavaFilePackage());
			}
			packages.get(package_name).add(file);
		}
		graph = new FileReferenceGraph(sources, packages);
	}

	public void checkParsingError(Map<String, CompilationUnit> origins) throws Exception {
		for(String path : sources.keySet()) {
			JavaFile source = sources.get(path);
			CompilationUnit cu = StaticJavaParser.parse(source.toString());
			List<String> tokens = new LinkedList<String>();
			for(JavaToken token : cu.getTokenRange().get()) {
				int kind = token.getKind();
				if(kind < 10) continue;	// EOF, SPACE, COMMENT
				tokens.add(token.getText());
			}
			Collections.sort(tokens);
			List<String> originTokens = new LinkedList<String>();
			for(JavaToken token : origins.get(path).getTokenRange().get()) {
				int kind = token.getKind();
				if(kind < 10) continue;	// EOF, SPACE, COMMENT
				originTokens.add(token.getText());
			}
			Collections.sort(originTokens);
			int i=0, j=0;
			while(i<tokens.size() && j<originTokens.size()) {
				int cmp = tokens.get(i).compareTo(originTokens.get(j));
				if(cmp < 0) {
					throw new Exception("Parsing is incomplete.");
				} else if (cmp > 0) {
					if(originTokens.get(j++).equals(";")) continue;	// in case that there are consecutive semicolons. ex);;
					throw new Exception("Parsing is incomplete.");
				} else {
					i++;
					j++;
				}
			}
		}
		System.out.println("Parsing is complete.");
	}

	public int LOC() {
		int loc = 0;
		for(JavaFile code : sources.values()) {
			loc += code.toString().split("\n").length;
		}
		return loc;
	}
	
}
