package migration.parsing;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import migration.parsing.entity.Clazz;

public class JavaFilePackage {
	Map<String, JavaFile> map = new HashMap<String, JavaFile>();

	public void add(JavaFile file) {
		for(Clazz clazz : file.getClazzs()) {
			map.put(clazz.getName(), file);
		}
	}

	public Set<JavaFile> getMatchedFiles(Set<String> usedNames) {
		Set<JavaFile> set = new HashSet<JavaFile>();
		for(String name : map.keySet()) {
			if(usedNames.contains(name)) {
				set.add(map.get(name));
			}
		}
		return set;
	}

	public JavaFile get(String name) {
		return map.get(name);
	}

	public boolean containsKey(String name) {
		return map.containsKey(name);
	}

	public Collection<JavaFile> getAll() {
		return map.values();
	}
}
