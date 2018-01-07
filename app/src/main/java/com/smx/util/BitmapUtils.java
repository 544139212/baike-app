package com.smx.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapUtils {
	public static Bitmap getBitmap(InputStream inputStream) {
		return BitmapFactory.decodeStream(inputStream);
	}
	
	// http://www.cnblogs.com/fighter/archive/2012/02/20/android-bitmap-drawable.html
	public static Bitmap getBitmap(byte[] buffer) {
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}
	
	public static Bitmap getBlankBitmap(int width, int height) {
		return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	}
	
	// http://blog.sina.com.cn/s/blog_66edd39d01013p1f.html
	public static Bitmap getScaleBitmap(Bitmap bitmap, int w, int h) {
		return Bitmap.createScaledBitmap(bitmap, w, h, true); 
	}
	
	// http://www.cnblogs.com/Soprano/articles/2577152.html
	public static Bitmap getScaleBitmap(Context context, Uri uri, int dw, int dh) throws FileNotFoundException {
        BitmapFactory.Options op = new BitmapFactory.Options(); 
        // set to true to get image width and height
        op.inJustDecodeBounds = true;  
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, op);
        
        int wRatio = (int) Math.ceil(op.outWidth / (float) dw);
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh);
        
        // �̶�����s��, inSampleSize�ǿs�ű���
        if (wRatio > 1 && hRatio > 1) {  
            if (wRatio > hRatio) {  
                op.inSampleSize = wRatio;  
            } else {  
                op.inSampleSize = hRatio;  
            }  
        } else if (wRatio > 1) {
        	op.inSampleSize = wRatio;  
        } else if (hRatio > 1) {
        	op.inSampleSize = hRatio;  
        }
        op.inSampleSize = 8;  
        // set to false to get image
        op.inJustDecodeBounds = false; 
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, op);  
	}
	
	public static Bitmap getScaleBitmap(Context context, byte[] buffer, int dw, int dh) {
		if (buffer == null) return null;
        BitmapFactory.Options op = new BitmapFactory.Options(); 
        // set to true to get image width and height
        op.inJustDecodeBounds = true;  
        BitmapFactory.decodeByteArray(buffer, 0, buffer.length, op);
        
        int wRatio = (int) Math.ceil(op.outWidth / (float) dw);
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh);
        
        // �̶�����s��, inSampleSize�ǿs�ű���
        if (wRatio > 1 && hRatio > 1) {  
            if (wRatio > hRatio) {  
                op.inSampleSize = wRatio;  
            } else {  
                op.inSampleSize = hRatio;  
            }  
        } else if (wRatio > 1) {
        	op.inSampleSize = wRatio;  
        } else if (hRatio > 1) {
        	op.inSampleSize = hRatio;  
        }
        op.inSampleSize = 8;  
        // set to false to get image
        op.inJustDecodeBounds = false; 
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length, op);  
	}
}
