package generation.activator;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class AntennaJavaCodeActivator {

	public static String activate(String text, String name) {
		String[] lines = text.split("\n");
		String[] activatedLines = activate(lines, name);
		String code = String.join("\n", activatedLines);
		return code;
	}

	private static String[] activate(String[] lines, String name) {
		String[] result = new String[lines.length];
		Stack<Boolean> stack = new Stack<Boolean>();
		stack.add(true);
		for(int i=0; i<lines.length; i++) {
			String line = lines[i];
			if(line.startsWith("//#if")) {
				result[i] = line;
				String condition = line.replaceFirst("//#if", "");
				stack.add(scan(condition).contains(name));
			} else if(line.startsWith("//#elif")) {
				result[i] = line;
				String condition = line.replaceFirst("//#elif", "");
				stack.pop();
				stack.add(scan(condition).contains(name));
			} else if(line.startsWith("//#endif")) {
				result[i] = line;
				stack.pop();
			} else {
				if(stack.peek()) result[i] = activateLine(line);
				else result[i] = deactivateLine(line);
			}
		}
		return result;
	}

	private static Set<String> scan(String condition) {
		Set<String> elements = new HashSet<String>();
		for(String var : condition.split("\\|")) {
			elements.add(var.trim());
		}
		return elements;
	}

	private static String activateLine(String line) {
		if(line.startsWith("//")) return line.replaceFirst("//", "");
		else return line;
	}

	private static String deactivateLine(String line) {
		if(line.startsWith("//")) return line;
		else return "//" + line;
	}
	
}
