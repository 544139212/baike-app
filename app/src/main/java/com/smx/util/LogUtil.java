package com.smx.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LogUtil {
	static List<String> messages = new ArrayList<String>();
	static String tag;
	static String path;
	
	public static void i() {
		path = PathUtils.getLogPath(System.currentTimeMillis() + ".txt");
	}
	
	public static void v(String text) {
		messages.add(text);
	}
	
	public static void w() { 
		File file = new File(path);       
		if (file.exists()) {               
			file.delete();           
		}  
		BufferedWriter bw = null;           
		try{              
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"UTF-8"));   
			if (!messages.isEmpty()) {
				bw.write(TimeUtil.getMinTime(System.currentTimeMillis()));
				bw.newLine();
				bw.newLine();
				for (String msg : messages) {
					if (StringUtil.isNotEmpty(msg)) {
						bw.write(msg);  
						bw.newLine();
					}
				}
			}
			bw.flush();      
		} catch (UnsupportedEncodingException e) {   
			e.printStackTrace();
		} catch (FileNotFoundException e) {           
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close(); 
				messages.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}     	
	}
	
}
