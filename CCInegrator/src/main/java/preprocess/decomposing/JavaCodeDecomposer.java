package preprocess.decomposing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import io.MyFileWriter;

public class JavaCodeDecomposer {

	public static void decompose(String path) {
		File file = new File(path);
		CompilationUnit cu;
		try {
			cu = StaticJavaParser.parse(file);
			Map<String, String> map = new HashMap<String, String>();
			ModifierVisitor<Map<String, String>> visitor = new ClassVisitor();
			visitor.visit(cu, map);
			
			if(map.size() == 1) return;
			System.out.println("#DECOMPOSING TARGET: " + path);
			String prefixCode = cu.toString();
			String prefixPath = file.getParentFile().toPath().toString();
			file.delete();
			for(String name : map.keySet()) {
				String new_path = String.format("%s\\%s.java", prefixPath, name);
				String code = String.format("%s\n%s", prefixCode, map.get(name));
				MyFileWriter.write(new_path, code);
				System.out.println("=> " + new_path);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
