package migration.clustering;

import migration.parsing.JavaFile;
import migration.parsing.Product;
import migration.parsing.entity.Clazz;

public class Chain {
	private final Product p;
	private final JavaFile f;
	private final Clazz c;
	
	public Chain(Product p, JavaFile f, Clazz c) {
		this.p = p;
		this.f = f;
		this.c = c;
	}
	
	public Product getProduct() {
		return p;
	}
	
	public JavaFile getJavaFile() {
		return f;
	}
	
	public Clazz getClazz() {
		return c;
	}

}
