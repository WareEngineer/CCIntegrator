package migration.fomatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.JavaToken;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;

import io.MyFileWriter;

public class JavaCodeFormatter {
	private static boolean isRearranged;
	
	public static void format(String path, boolean isRearranged) {
		JavaCodeFormatter.isRearranged = isRearranged;
		File file = new File(path);
		CompilationUnit cu = getCU(file);
		TokenRange range = cu.getTokenRange().get();
		List<String> lines = new LinkedList<String>();
		StringBuffer buffer = new StringBuffer();
		for(JavaToken token : range) {
			if(0==token.getKind() || (2<=token.getKind() && token.getKind()<=4)) {
				String line = getLine(buffer);
				if(line.isEmpty() == false) lines.add(line);
				buffer = new StringBuffer();
			} else if(5<=token.getKind() && token.getKind()<=10) {
				buffer.append(" ");
			} else {
				buffer.append(token.getText());
			}
		}
		String last = getLine(buffer);
		if(last.isEmpty() == false) lines.add(last);
		String code = String.join("\n", lines);
		MyFileWriter.write(path, code);
	}

	private static String getLine(StringBuffer buffer) {
		String trimedLine = buffer.toString().trim();
		if(trimedLine.equals("")) return "";
		if(isRearranged) {
			return trimedLine;
		} else {
			return buffer.toString();
		}
	}

	private static CompilationUnit getCU(File file) {
		CompilationUnit cu = null;
		try {
			if(isRearranged) {
				String rearranged_source_code = StaticJavaParser.parse(file).toString();
				cu = StaticJavaParser.parse(rearranged_source_code);
			} else {
				cu = StaticJavaParser.parse(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return cu;
	}

}
