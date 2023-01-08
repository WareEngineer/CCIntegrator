package migration.parsing.entity;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;

public class Annotation extends Clazz {

	public Annotation(String pkg, AnnotationDeclaration n) {
		super(pkg, n.getTokenRange().get().iterator());
		this.pkg = pkg;
		this.name = n.getNameAsString();
		visit(n, null);
	}

    @Override
    public void visit(final AnnotationMemberDeclaration n, final Void arg) {
    	fields.add(n.getTokenRange().get().toString());
    }
}
