package migration.merging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import io.MyFileReader;
import io.MyFileRemover;
import io.MyFileWriter;
import migration.clustering.Chain;
import migration.clustering.CloneSet;
import migration.merging.merger.CodeMerger;
import migration.parsing.Product;

public class SPLrepository {
	public static final String ROOT = ".\\repository";
	public static final String SOURCECODE = ROOT + "\\sourcecode";
	public static final String SUGGESTION = ROOT + "\\suggestion_for_refactoring.txt";
	private List<Asset> assets = new LinkedList<Asset>();
	
	public SPLrepository(List<Product> products) {
		MyFileRemover.remove(SPLrepository.ROOT);
	}

	public void addAsset(Asset asset) {
		assets.add(asset);
		String path = String.format("%s\\%s", SOURCECODE, asset.name());
		MyFileWriter.write(path, asset.code());
	}

	public void log(CloneSet cluster) {
		List<String> list = new LinkedList<String>();
		for(Chain chain : cluster.getChains()) {
			String productName = chain.getProduct().getName();
			String relativePath = CodeMerger.extractRelativeFilePath(chain);
			String record = String.format("=> %s : %s", relativePath, productName);
			list.add(record);
		}
		StringBuffer msg = new StringBuffer();
		msg.append("\n<CLUSTER with different relative paths among the included files>\n");
		msg.append(String.join("\n", list));
		msg.append("\n</CLUSTER>\n");
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(SUGGESTION, true));
			bufferedWriter.append(msg);
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Asset> getAllAssets() {
		return assets;
	}
	
	public static String getAsset(String name) {
		return MyFileReader.read(String.format("%s\\%s", SOURCECODE, name));
	}
	
}
