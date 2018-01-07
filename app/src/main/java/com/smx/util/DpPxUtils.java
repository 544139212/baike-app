package com.smx.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DpPxUtils {
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
		 
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	} 
	
	// use dp2px instead
	public static int formatDpToPx(Context context, int dp) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return (int) Math.ceil(dp * dm.density);
	}
		 
	// use px2dp instead
	public static int formatPxToDp(Context context, int px) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return (int) Math.ceil(((px * 160) / dm.densityDpi));
	}
	
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
