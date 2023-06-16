package migration.merging.merger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LCS {

	public static String[] get(String[][] codes) {
		String[] common = getCommonLines(codes);
		String[][] simples = excludeUncommonLines(codes, common);
		Arrays.sort(simples, (a, b)-> a.length - b.length);
		String[] base = simples[0];
		for(int i=1; i<simples.length; i++) {
			base = lcs(base, simples[i]);
		}
		return base;
	}

	private static String[] getCommonLines(String[][] codes) {
		String[] common = Arrays.copyOf(codes[0], codes[0].length);
		Arrays.sort(common);
		for(int i=1; i<codes.length; i++) {
			List<String> temp = new LinkedList<String>();
			String[] lines = Arrays.copyOf(codes[i], codes[i].length);
			Arrays.sort(lines);
			int j=0, k=0;
			while(j<common.length && k<lines.length) {
				int cmp = common[j].compareTo(lines[k]);
				if(cmp < 0) j++;
				else if(cmp > 0) k++;
				else {
					temp.add(common[j]);
					j++;
					k++;
				}
			}
			common = new String[temp.size()];
			temp.toArray(common);
		}
		return common;
	}

	private static String[][] excludeUncommonLines(String[][] codes, String[] common) {
		Set<String> set = new HashSet<String>();
		for(String line : common) set.add(line);
		String[][] simples = new String[codes.length][];
		for(int i=0; i<codes.length; i++) {
			List<String> temp = new LinkedList<String>();
			for(int j=0; j<codes[i].length; j++) {
				String line = codes[i][j];
				if(set.contains(line)) {
					temp.add(line);
				}
			}
			simples[i] = new String[temp.size()];
			temp.toArray(simples[i]);
		}
		return simples;
	}

	private static String[] lcs(String[] array1, String[] array2) {
		int n = array1.length;
		int m = array2.length;
		int[][] table = new int[n+1][m+1];
		for(int i=0; i<=n; i++) {
			for(int j=0; j<=m; j++) {
				if(i==0 || j==0) {
					table[i][j] = 0;
				} else if (array1[i-1].equals(array2[j-1])) {
					table[i][j] = table[i-1][j-1] + 1;
				} else {
					table[i][j] = Math.max(table[i][j-1], table[i-1][j]);
				}
			}
		}
		int i = n;
		int j = m;
		List<String> lcs = new LinkedList<String>();
	    while (i > 0 && j > 0) {
	    	if (array1[i-1].equals(array2[j-1])) {
	    		lcs.add(array1[i-1]);
	    		i--;
	    		j--;
	    	} else if (table[i-1][j] > table[i][j-1]) {
	    		i--;
	    	} else {
	    		j--;
	    	}
	    }
	    Collections.reverse(lcs);
	    String[] array = new String[lcs.size()];
	    lcs.toArray(array);
		return array;
	}
}