package migration.parsing.entity;

import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

public class Enum extends Clazz {
	private List<String> constants = new LinkedList<String>();

	public Enum(String pkg, EnumDeclaration n) {
		super(pkg, n.getTokenRange().get().iterator());
		this.pkg = pkg;
		this.name = n.getNameAsString();
		visit(n, null);
		this.fields.add(String.join(",\n", constants) + ";");
	}

    @Override
    public void visit(final EnumConstantDeclaration n, final Void arg) {
    	constants.add(n.getTokenRange().get().toString());
    }

}
