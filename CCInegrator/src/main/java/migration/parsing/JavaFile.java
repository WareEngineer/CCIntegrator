package migration.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import migration.parsing.entity.Annotation;
import migration.parsing.entity.Clazz;
import migration.parsing.entity.Enum;

public class JavaFile extends VoidVisitorAdapter<Void> {
	public static String DEFAULT_PACKAGE_NAME = "(default)";
	private String path;
	private String packageName = DEFAULT_PACKAGE_NAME;
	private List<String> packageCodes = new LinkedList<String>();
	private List<String> importCodes = new LinkedList<String>();
	private List<Clazz> clazzs = new LinkedList<Clazz>();
	private Set<String> usedType;
	private List<String> importedTypes;
	private boolean hasMainMethod;

	public JavaFile(String file_path) {
		File file = new File(file_path);
		ParserConfiguration parserConfiguration = new ParserConfiguration().setAttributeComments(false);
		StaticJavaParser.setConfiguration(parserConfiguration);
		try {
			this.path = file.getAbsolutePath();
			CompilationUnit cu = StaticJavaParser.parse(file);
			visit(cu, null);
			this.usedType = JavaFileDataMiner.getUsedTypes(cu);
			this.importedTypes = JavaFileDataMiner.getImportedObjects(cu);
			this.hasMainMethod = JavaFileDataMiner.hasMainMethod(cu);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public Set<String> getUsedTypes() {
		return usedType;
	}
	
	public List<String> getImportedTypes() {
		return importedTypes;
	}
	
	public boolean hasMainMethod() {
		return hasMainMethod;
	}
	
	public String getPackageCode() {
		return String.join("\n", packageCodes);
	}
	
	public String getImportCode() {
		return String.join("\n", importCodes);
	}
	
	public List<Clazz> getClazzs() {
		return clazzs;
	}
    
    public void delete() {
    	new File(path).delete();
    }
	
	@Override
	public String toString() {
		List<String> list = new LinkedList<String>();
		list.addAll(packageCodes);
		list.addAll(importCodes);
		for(Clazz clazz : clazzs) {
			list.add(clazz.toString());
		}
		return String.join("\n", list);
	}

	@Override
    public void visit(final PackageDeclaration n, final Void arg) {
		packageName = n.getNameAsString();
		packageCodes.add(n.getTokenRange().get().toString());
	}

    @Override
    public void visit(final ImportDeclaration n, final Void arg) {
		importCodes.add(n.getTokenRange().get().toString());
    }

	@Override
    public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
		clazzs.add(new Clazz(packageName, n));
	}
	
    @Override
    public void visit(final EnumDeclaration n, final Void arg) {
    	clazzs.add(new Enum(packageName, n));
    }
    
    @Override
    public void visit(final AnnotationDeclaration n, final Void arg) {
    	clazzs.add(new Annotation(packageName, n));
    }

	public boolean hasPackage() {
		if(packageName.equals(DEFAULT_PACKAGE_NAME)) return false;
		else return true;
	}
	
}
