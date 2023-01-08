package migration.merging.merger;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.merging.entity.AnnotatedCode;
import migration.parsing.JavaFile;
import migration.parsing.entity.Clazz;
import migration.parsing.entity.Method;

public class CodeMerger {

	public static String makeDescription(CloneSet cluster) {
		List<String> list = new LinkedList<String>();
		cluster.getChains().forEach(chain -> list.add(chain.getJavaFile().getPath().replace('\\', '/')));
		Collections.sort(list);
		String description = String.format("/*\n%s\n*/", String.join("\n", list));
		return description;
	}

	public static boolean isLargerNOCthanNOP(CloneSet cluster) {
		if(cluster.NOP() < cluster.NOC()) {
			System.out.println("In a cluster, the number of classes is larger than the number of product.");
			return true;
		}
		return false;
	}

	public static boolean hasTheSameRelativePath(CloneSet cluster) {
		Set<String> set = new HashSet<String>();
		cluster.getChains().forEach(chain -> {
			String relativePath = extractRelativeFilePath(chain);
			set.add(relativePath);
		});
		return set.size() == 1;
	}

	public static String extractRelativeFilePath(Chain chain) {
		String productPath = chain.getProduct().getRootPath();
		String filePath = chain.getJavaFile().getPath();
		String relativePath = filePath.replace(productPath, "");
		return relativePath;
	}

	public static AnnotatedCode merge(CloneSet cluster) {
		if(cluster.NOP() < cluster.NOC()) {
			System.out.println("It is impossilble to merge files in the cluster.");
			return null;
		}
		
		Set<String> ids = new HashSet<String>();
		Map<String, String> packageCodes = new HashMap<String, String>();
		Map<String, String> importCodes = new HashMap<String, String>();
		for(Chain chain : cluster.getChains()) {
			JavaFile source = chain.getJavaFile();
			String id = chain.getProduct().getName();
			packageCodes.put(id, source.getPackageCode());
			importCodes.put(id, source.getImportCode());
			ids.add(id);
		}
		AnnotatedCode code = new AnnotatedCode(ids);
		code.append(packageCodes);
		code.append(importCodes);
		code.append(annotateClass(ids, cluster));
		return code;
	}
	
	private static AnnotatedCode annotateClass(Set<String> range, CloneSet cluster) {
		Map<String, String> headCodes = new HashMap<String, String>();
		Map<String, String> fieldCodes = new HashMap<String, String>();
		Map<String, Map<String, String>> mapOfMethodCodes = new HashMap<String, Map<String, String>>();
		Map<String, String> classEndBraces = new HashMap<String, String>();
		for(Chain chain : cluster.getChains()) {
			String id = chain.getProduct().getName();
			Clazz clazz = chain.getClazz();
			headCodes.put(id, clazz.getHead());
			fieldCodes.put(id, clazz.getFieldCode());
			List<Method> methods = new LinkedList<Method>();
			clazz.getInitializers().forEach(e -> methods.add(e));
			clazz.getConstructors().forEach(e -> methods.add(e));
			clazz.getMethods().forEach(e -> methods.add(e));
			for(Method method : methods) {
				String signature = method.getSignature();
				if(mapOfMethodCodes.containsKey(signature) == false) {
					mapOfMethodCodes.put(signature, new HashMap<String, String>());
				}
				mapOfMethodCodes.get(signature).put(id, method.toString());
			}
			classEndBraces.put(id, "}");
		}
		
		AnnotatedCode code = new AnnotatedCode();
		code.append(headCodes);
		code.append(fieldCodes);
		
		Map<String, AnnotatedCode> codeMap = new HashMap<String, AnnotatedCode>();

		for(CloneSet inner : cluster.getInners()) {
			Set<String> ids = new HashSet<String>();
			for(Chain chain : inner.getChains()) {
				String id = chain.getProduct().getName();
				ids.add(id);
			}
			AnnotatedCode innerClazz = annotateClass(ids, inner);
			if(ids.size() == range.size()) {
				code.append(innerClazz);
				continue;
			} else {
				String condition = getCondition(ids);
				if(codeMap.containsKey(condition) == false) {
					codeMap.put(condition, new AnnotatedCode(ids));
				}
				codeMap.get(condition).append(innerClazz);
			}
		}
		
		for(String signature : mapOfMethodCodes.keySet()) {
			Map<String, String> methods = mapOfMethodCodes.get(signature);
			Set<String> ids = methods.keySet();
			AnnotatedCode part = new AnnotatedCode();
			part.append(methods);
			if(ids.size() == range.size()) {
				code.append(part);
				continue;
			} else {
				String condition = getCondition(ids);
				if(codeMap.containsKey(condition) == false) {
					codeMap.put(condition, new AnnotatedCode(ids));
				}
				codeMap.get(condition).append(part);
			}
		}
		
		for(AnnotatedCode part : codeMap.values()) {
			code.append(part);
		}
		
		code.append(classEndBraces);
		return code;
	}

	private static String getCondition(Set<String> ids) {
		List<String> list = new LinkedList<String>();
		list.addAll(ids);
		Collections.sort(list);
		return String.join("#", list);
	}
	
}
