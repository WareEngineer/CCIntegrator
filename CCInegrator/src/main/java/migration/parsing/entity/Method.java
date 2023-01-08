package migration.parsing.entity;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Method {
	private String signature;
	private String body;
	private String code;
	
	public Method(MethodDeclaration n) {
		code = n.getTokenRange().get().toString();
		boolean isAbstract = !n.getBody().isPresent();
		if(isAbstract) {
//			signature = n.getSignature().toString();
			signature = n.getSignature().toString() + ";";
//			signature = n.getTypeAsString() + " " + n.getSignature().toString() + ";";
			body = "";
		} else {
//			signature = n.getSignature().toString();
			signature = n.getSignature().toString() + "{}";
//			signature = n.getTypeAsString() + " " + n.getSignature().toString() + "{}";
			body = n.getBody().get().getTokenRange().get().toString();
		}
	}

	public Method(ConstructorDeclaration n) {
		signature = n.getSignature().toString();
		body = n.getBody().getTokenRange().get().toString();
		code = n.getTokenRange().get().toString();
	}
	
	public Method(InitializerDeclaration n) {
		signature = "static";
		body = n.getBody().getTokenRange().get().toString();
		code = n.getTokenRange().get().toString();
	}

	public String getSignature() {
		return signature;
	}
	
	public String getBody() {
		return body;
	}
	
	@Override
	public String toString() {
		return code;
	}
}
