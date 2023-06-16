package generation.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model {
	private Document doc;
	
	public Model(String path) {
		try {
			File file = new File(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getProductList() {
		List<String> list = new LinkedList<String>();
		for(Element e : getElementsByTagName("product")) {
			String name = e.getAttribute("name");
			list.add(name);
		}
		return list;
	}

	public Map<String, String> getAssets(String product) {
		Map<String, String> map = new HashMap<String, String>();
		for(Element asset : getElementsByTagName("asset")) {
			String name = asset.getAttribute("id");
			for(Element file : getElementsByTagName(asset, "file")) {
				String owner = file.getAttribute("owner");
				if(owner.equals(product) == false) {
					continue;
				}
				String path = file.getAttribute("path");
				map.put(name, path);
			}
		}
		return map;
	}
	
	public List<Element> getElementsByTagName(Element from, String tag) {
		List<Element> elements = new LinkedList<Element>();
		NodeList nodes = from.getElementsByTagName(tag);
		for(int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				elements.add(e);
			}
		}
		return elements;
	}

	public List<Element> getElementsByTagName(String tag) {
		Element from = doc.getDocumentElement();
		return getElementsByTagName(from, tag);
	}
	
}
