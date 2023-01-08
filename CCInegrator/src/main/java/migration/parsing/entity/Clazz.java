package migration.parsing.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.github.javaparser.JavaToken;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Clazz extends VoidVisitorAdapter<Void> {
	protected String pkg = "";
	protected String name = "";
	protected String head = "";
	protected List<String> fields = new LinkedList<String>();
	private List<Method> initializers = new LinkedList<Method>();
	private List<Method> constructors = new LinkedList<Method>();
	private List<Method> methods = new LinkedList<Method>();
	private List<Clazz> inners = new LinkedList<Clazz>();
	
	protected Clazz(String pkg, Iterator<JavaToken> iter) {
		StringBuffer buffer = new StringBuffer();
		if(iter.hasNext()) {
			JavaToken token = iter.next();
			Stack<String> parentheses = new Stack<String>();
			while(iter.hasNext()) {
				String txt = token.getText();
				if(txt.equals("(")) parentheses.add(txt);
				if(parentheses.isEmpty() && txt.equals("{")) break;
				if(txt.equals(")")) parentheses.pop();
				buffer.append(token.getText());
				token = iter.next();
			}
		}
		head = buffer.toString() + "{";
	}

	public Clazz(String pkg, ClassOrInterfaceDeclaration n) {
		this(pkg, n.getTokenRange().get().iterator());
		this.pkg = pkg;
		this.name = n.getNameAsString();
//		n.getFullyQualifiedName()
		visit(n, null);
	}
	
	public String getName() {
		return name;
	}

	public String getFullName() {
		return pkg+"."+name;
	}
	
	public String getHead() {
		return head;
	}

	public Iterable<Method> getInitializers() {
		return initializers;
	}

	public Iterable<Method> getConstructors() {
		return constructors;
	}
	
	public Iterable<Method> getMethods() {
		return methods;
	}

	public Iterable<Clazz> getInners() {
		return inners;
	}

	public String getFieldCode() {
		return String.join("\n", fields);
	}

	public String toStringWithoutInnerClassCode() {
		List<String> list = new LinkedList<String>();
		list.add(head);
		list.addAll(fields);
		for(Method initializer : initializers) {
			list.add(initializer.toString());
		}
		for(Method constructor : constructors) {
			list.add(constructor.toString());
		}
		for(Method method : methods) {
			list.add(method.toString());
		}
		list.add("}");
		return String.join("\n", list);
	}

	@Override
	public String toString() {
		List<String> list = new LinkedList<String>();
		list.add(head);
		list.addAll(fields);
		for(Method initializer : initializers) {
			list.add(initializer.toString());
		}
		for(Method constructor : constructors) {
			list.add(constructor.toString());
		}
		for(Method method : methods) {
			list.add(method.toString());
		}
		for(Clazz clazz : inners) {
			list.add(clazz.toString());
		}
		list.add("}");
		return String.join("\n", list);
	}

	@Override
    public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
		if(n.getNameAsString().equals(name)) {
			super.visit(n, null);
		} else {
			inners.add(new Clazz(pkg+"."+name, n));
		}
    }
	
    @Override
    public void visit(final EnumDeclaration n, final Void arg) {
		if(n.getNameAsString().equals(name)) {
			super.visit(n, null);
		} else {
			inners.add(new Enum(pkg+"."+name, n));
		}
    }

    @Override
    public void visit(final AnnotationDeclaration n, final Void arg) {
		if(n.getNameAsString().equals(name)) {
			super.visit(n, null);
		} else {
			inners.add(new Annotation(pkg+"."+name, n));
		}
    }
	
    @Override
    public void visit(FieldDeclaration n, Void arg) {
    	fields.add(n.getTokenRange().get().toString());
    }
    
    @Override
    public void visit(InitializerDeclaration n, Void v) {
    	fields.add(n.getTokenRange().get().toString());
//    	initializers.add(new Method(n));
    }

    @Override
    public void visit(final ConstructorDeclaration n, final Void arg) {
    	constructors.add(new Method(n));
    }
    
    @Override
    public void visit(MethodDeclaration n, Void v) {
    	methods.add(new Method(n));
    }

}
