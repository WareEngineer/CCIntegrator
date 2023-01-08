package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {
	
	public static void write(String absoluteFilePath, String text) {
		File file = new File(absoluteFilePath);
		if(file.isDirectory()) return;
		
		file.getParentFile().mkdirs();
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(text);
		    writer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
