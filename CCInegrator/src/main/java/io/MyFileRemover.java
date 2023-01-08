package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class MyFileRemover {
	
	public static void remove(String path) {
		if(Files.exists(Paths.get(path)) == false) return;
		try {
			Files.walk(Paths.get(path))
			     .sorted(Comparator.reverseOrder())
			     .forEach(p -> remove(p));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void remove(Path path) {
		try {
			Files.delete(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
