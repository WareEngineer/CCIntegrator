package migration.clustering.measure;

import java.util.HashSet;
import java.util.Set;

public class MeasureBasedOnLineSet implements Measure {

	@Override
	public double calc(String[] codes) {
		Set<String> universal = new HashSet<String>();
		Set<String> commonSet = null;
		for(String code : codes) {
			String[] lines = code.split("\n");
			if(commonSet == null) {
				commonSet = new HashSet<String>();
				for(String line : lines) {
					universal.add(line);
					commonSet.add(line);
				}
			} else {
				Set<String> tmp = new HashSet<String>();
				for(String line : lines) {
					universal.add(line);
					if(commonSet.contains(line)) tmp.add(line); 
				}
				commonSet = tmp;
			}
		}
		
		return commonSet.size() / (float)universal.size();
	}
}
