package io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MyFileReader {
	public static String read(String path) {
		File file = new File(path);
		StringBuffer buffer = new StringBuffer();
		
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			BufferedReader reader = new BufferedReader(isr);
			
			String line;
			while ((line = reader.readLine()) != null) {
			    buffer.append(line);
			    buffer.append("\n");
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
}
