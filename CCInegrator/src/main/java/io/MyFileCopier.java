package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MyFileCopier {
	
	public static void copyDiretory(String source, String destination) {
		Path src = Paths.get(source);
		Path dest = Paths.get(destination);
	    try {
	    	Files.walk(src).forEach(path -> copyFile(path, dest.resolve(src.relativize(path))));
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void copyDiretoryExcept(String source, String destination, String extension) {
		Path src = Paths.get(source);
		Path dest = Paths.get(destination);
	    try {
	    	Files.walk(src).forEach(path -> {
	    		boolean isDirectory = Files.isDirectory(path);
	    		boolean isContained = path.toString().endsWith(extension);
	    		if(!isDirectory && !isContained) copyFile(path, dest.resolve(src.relativize(path)));
	    	});
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void copyFile(Path source, Path destination) {
	    try {
	    	if( !Files.exists(destination) ) {
		    	Files.createDirectories(destination);
		        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
	    	}
	    } catch (Exception e) {
	        throw new RuntimeException(e.getMessage(), e);
	    }
	}
	
}
