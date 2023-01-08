package migration.parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileReferenceGraph {
	private Map<JavaFile, Set<JavaFile>> map = new HashMap<JavaFile, Set<JavaFile>>();

	public FileReferenceGraph(Map<String, JavaFile> sources, Map<String, JavaFilePackage> packages) {
		for(JavaFile file : sources.values()) {
			Set<JavaFile> references = new HashSet<JavaFile>();
			for(JavaFile refferedFile : getExplicitlyImportedJavaFiles(file, packages)) {
				references.add(refferedFile);
			}
			for(JavaFile refferedFile : getDirectlyOrImplicitlyImportedJavaFiles(file, packages)) {
				references.add(refferedFile);
			}
			map.put(file, references);
		}
	}

	private Set<JavaFile> getDirectlyOrImplicitlyImportedJavaFiles(JavaFile file, Map<String, JavaFilePackage> packages) {
		Set<JavaFile> set = new HashSet<JavaFile>();
		String packageName = file.getPackageName();
		for(String type : file.getUsedTypes()) {
			List<JavaFile> result = find(file, packages, type);
			if(result.isEmpty()) {
				String implicit = String.format("%s.%s", packageName, type);
				result = find(file, packages, implicit);
			}
			set.addAll(result);
		}
		return set;
	}

	private List<JavaFile> getExplicitlyImportedJavaFiles(JavaFile file, Map<String, JavaFilePackage> packages) {
		List<JavaFile> list = new LinkedList<JavaFile>();
		for(String importedType : file.getImportedTypes()) {
			list.addAll( find(file, packages, importedType) );
		}
		return list;
	}

	private List<JavaFile> find(JavaFile file, Map<String, JavaFilePackage> packages, String s) {
		List<JavaFile> list = new LinkedList<JavaFile>();
		int pos = s.lastIndexOf('.');
		if(pos == -1) return list;
		
		String pkg = s.substring(0, pos);
		String cls = s.substring(pos+1);
		if(packages.containsKey(pkg) == false) return list;
		
		JavaFilePackage filePackage = packages.get(pkg);
		if(cls.equals("*")) {
			Set<JavaFile> matchedFiles = filePackage.getMatchedFiles(file.getUsedTypes());
			list.addAll(matchedFiles);
		} else if(filePackage.containsKey(cls)) {
			JavaFile refferedFile = filePackage.get(cls);
			list.add(refferedFile);
		}
		return list;
	}

	public Set<JavaFile> getRefferedFiles(JavaFile file) {
		if(map.containsKey(file)) return map.get(file);
		else return new HashSet<JavaFile>();
	}
	
}
