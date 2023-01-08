package preprocess.decomposing;

import java.util.Map;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;

public class ClassVisitor extends ModifierVisitor<Map<String, String>> {

	@Override
    public ClassOrInterfaceDeclaration visit(final ClassOrInterfaceDeclaration n, final Map<String, String> arg) {
		arg.put(n.getNameAsString(), n.getTokenRange().get().toString());
		return null;
	}
	
    @Override
    public EnumDeclaration visit(final EnumDeclaration n, final Map<String, String> arg) {
		arg.put(n.getNameAsString(), n.getTokenRange().get().toString());
		return null;
    }
    
    @Override
    public AnnotationDeclaration visit(final AnnotationDeclaration n, final Map<String, String> arg) {
		arg.put(n.getNameAsString(), n.getTokenRange().get().toString());
		return null;
    }
	
}
