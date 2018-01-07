package com.smx.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

public class ByteUtils {
	public static byte[] getOriginalBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		while ((b = inputStream.read()) != -1) {
			baos.write(b);
		}
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}
	
	public static byte[] getCompressBytes(Bitmap bitmap) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}
	
	public static byte[] getCompressBytes(String path) throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream(path);
		Bitmap bitmap = BitmapUtils.getBitmap(in);
		byte[] bytes = getCompressBytes(bitmap);
		in.close();
		return bytes;
	}
}
