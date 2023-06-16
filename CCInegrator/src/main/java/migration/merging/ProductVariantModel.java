package migration.merging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import migration.parsing.Product;

public class ProductVariantModel {
	private final String PATH;
	private Document doc;
	private Element products;
	private Element assets;
	
	public ProductVariantModel() {
		PATH = SPLrepository.ROOT + "\\model.xml";
		try {
	        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Element root = doc.createElement("model");
		products = doc.createElement("products");
		root.appendChild(products);
		assets = doc.createElement("assets");
		root.appendChild(assets);
		doc.appendChild(root);
	}

	public void append(Product unit) {
		Element product = doc.createElement("product");
		product.setAttribute("name", unit.getName());
		product.setAttribute("path", unit.getRootPath());
		products.appendChild(product);
	}

	public void append(Asset unit) {
		Element asset = doc.createElement("asset");
		asset.setAttribute("id", unit.name());
		asset.setAttribute("type", unit.type());
		asset.setAttribute("count", Integer.toString(unit.count()));
		asset.setAttribute("has_the_same_relative_path", unit.hasTheSameRelativePath() ? "true" : "false");
		Map<String, String> files = unit.files();
		List<String> products = new LinkedList<String>(files.keySet());
		Collections.sort(products);
		for(String name : products) {
			Element file = doc.createElement("file");
			file.setAttribute("owner", name);
			file.setAttribute("path", files.get(name));
			asset.appendChild(file);
		}
		assets.appendChild(asset);
	}

	public void create() throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// pretty print
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		try {
			FileOutputStream output = new FileOutputStream(PATH);
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
