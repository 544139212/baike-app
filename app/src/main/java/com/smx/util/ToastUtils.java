package com.smx.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smx.R;

/**
 * Toast统一管理类
 * 
 * @author WH
 * 
 */
public class ToastUtils {
	//android default long length
	public static final long LENGTH_LONG = 3500; 
	//android default short length
	public static final long LENGTH_SHORT = 2000;
	
	public static void show(Context context, CharSequence text, int duration) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast, null);
		TextView content = (TextView) view.findViewById(R.id.content);
		content.setText(text);
		
		Toast toast = new Toast(context);
//		toast.setGravity(Gravity.BOTTOM, 0, formatDpToPx(context, 70));
		toast.setGravity(Gravity.BOTTOM, 0, DpPxUtils.dp2px(context, 70));
		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}
	
	public static void show(Context context, int resId, int duration) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.toast, null);
		TextView content = (TextView) view.findViewById(R.id.content);
		content.setText(resId);
		
		Toast toast = new Toast(context);
//		toast.setGravity(Gravity.BOTTOM, 0, formatDpToPx(context, 70));
		toast.setGravity(Gravity.BOTTOM, 0, DpPxUtils.dp2px(context, 70));
		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}
	
	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param resId
	 */
	public static void addShortDurationMessage(Context context, int resId) {
		ToastUtils.show(context, resId, Toast.LENGTH_SHORT);
	}
	
	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void addShortDurationMessage(Context context, CharSequence text) {
		ToastUtils.show(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param resId
	 */
	public static void addLongDurationMessage(Context context, int resId) {
		ToastUtils.show(context, resId, Toast.LENGTH_SHORT);
	}
	
	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param text
	 */
	public static void addLongDurationMessage(Context context, CharSequence text) {
		ToastUtils.show(context, text, Toast.LENGTH_SHORT);
	}

}
