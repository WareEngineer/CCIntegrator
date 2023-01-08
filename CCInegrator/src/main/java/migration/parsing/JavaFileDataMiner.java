package migration.parsing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class JavaFileDataMiner {
	public static String getPackageName(CompilationUnit cu) {
		String package_name = "(default)";
		if(cu.getPackageDeclaration().isPresent()) {
			PackageDeclaration pd = cu.getPackageDeclaration().get();
			package_name = pd.getNameAsString();
		}
		return package_name;
	}
	
	public static List<String> getImportedObjects(CompilationUnit cu) {
		List<String> names = new LinkedList<String>();
		for(ImportDeclaration id : cu.getImports()) {
			String name = id.getNameAsString();
			if(id.isAsterisk()) {
				name += ".*";
			}
			names.add(name);
		}
		return names;
	}
	
	public static List<String> getUserDefinedObjectNames(CompilationUnit cu) {
		List<String> names = new LinkedList<String>();
		String name;
		for(ClassOrInterfaceDeclaration coid : cu.findAll(ClassOrInterfaceDeclaration.class)) {
			name = coid.getNameAsString();
			names.add(name);
		}
		for(EnumDeclaration ed : cu.findAll(EnumDeclaration.class)) {
			name = ed.getNameAsString();
			names.add(name);
		}
		for(AnnotationDeclaration ad : cu.findAll(AnnotationDeclaration.class)) {
			name = ad.getNameAsString();
			names.add(name);
		}
		return names;
	}
	
	public static Set<String> getUsedTypes(CompilationUnit cu) {
		Set<String> usedTypes = new HashSet<String>();
		for(MethodCallExpr mc : cu.findAll(MethodCallExpr.class)) {
			String temp = mc.toString();
			int pos = temp.lastIndexOf(String.format(".%s", mc.getNameAsString()));
			if(pos == -1) continue;
			String type = temp.substring(0, pos);
			usedTypes.add(type);
		}
		for(FieldAccessExpr ne : cu.findAll(FieldAccessExpr.class)) {
			String temp = ne.toString();
			int pos = temp.lastIndexOf(String.format(".%s", ne.getNameAsString()));
			if(pos == -1) continue;
			String type = temp.substring(0, pos);
			usedTypes.add(type);
		}
		for(ClassOrInterfaceType coit : cu.findAll(ClassOrInterfaceType.class)) {
			usedTypes.add(coit.toString());
			usedTypes.add(coit.getNameAsString());
		}
		for(StringLiteralExpr sle : cu.findAll(StringLiteralExpr.class)) {
			usedTypes.add(sle.asString());
		}
		return usedTypes;
	}

	public static boolean hasMainMethod(CompilationUnit cu) {
		for(MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
			if(md.getSignature().toString().equals("main(String[])")) {
				return true;
			}
		}
		return false;
	}
	
}
