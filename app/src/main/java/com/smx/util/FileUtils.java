package com.smx.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUtils {
	public static String getFilename(String suffix) {
		return UUID.randomUUID().toString() + suffix;
	}
	
	public static boolean isExists(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	public static void writePhoto(String path, byte[] buffer) throws FileNotFoundException, IOException {
		FileOutputStream outputStream = new FileOutputStream(path);
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
	}
	
	public static void deletePhoto(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}
