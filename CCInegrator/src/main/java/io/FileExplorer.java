package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileExplorer {
	
	public static List<String> collectJavaFiles(String path) {
		String extension = ".java";
		List<String> javaSourceFiles = new ArrayList<String>();
		try {
			javaSourceFiles = Files.walk(Paths.get(path))
							   	   .filter(Files::isRegularFile)
							   	   .map(file -> file.toAbsolutePath().toString())
							   	   .filter(name -> name.endsWith(extension))
							   	   .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return javaSourceFiles;
	}

	public static List<String> collectClassFiles(String path) {
		String extension = ".class";
		List<String> javaSourceFiles = new ArrayList<String>();
		try {
			javaSourceFiles = Files.walk(Paths.get(path))
							   	   .filter(Files::isRegularFile)
							   	   .map(file -> file.toAbsolutePath().toString())
							   	   .filter(name -> name.endsWith(extension))
							   	   .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return javaSourceFiles;
	}
	
	public static List<String> collectFilesExcept(String path, List<String> extensions) {
		List<String> files = new ArrayList<String>();
		try {
			files = Files.walk(Paths.get(path))
							   	   .filter(Files::isRegularFile)
							   	   .map(file -> file.toAbsolutePath().toString())
							   	   .filter(name -> !isEndWith(name, extensions))
							   	   .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

	private static boolean isEndWith(String name, List<String> extensions) {
		for(String extension : extensions) {
			if(name.endsWith(extension)) return true;
		}
		return false;
	}
	
}
