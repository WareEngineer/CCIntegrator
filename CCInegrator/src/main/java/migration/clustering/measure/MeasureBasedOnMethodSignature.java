package migration.clustering.measure;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MeasureBasedOnMethodSignature implements Measure {

	@Override
	public double calc(String[] codes) {
		int entire = 0;
		int common = 0;
		String[] commonLines = null;
		for(String code : codes) {
			String[] lines = code.split("\n");
			Arrays.sort(lines);
			entire += lines.length;
			if(commonLines == null) {
				commonLines = lines;
				continue;
			}
			int i=0, j=0;
			List<String> tmp = new LinkedList<String>();
			while(i<lines.length && j<commonLines.length) {
				int cmp = lines[i].compareTo(commonLines[j]);
				if		(cmp < 0) i++;
				else if (cmp > 0) j++;
				else {
					tmp.add(lines[i]);
					i++;
					j++;
				}
			}
			commonLines = new String[tmp.size()];
			tmp.toArray(commonLines);
		}
		common = codes.length * commonLines.length;
		return common / (float)entire;
	}
}
