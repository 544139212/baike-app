package com.smx.util;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.smx.R;

public class DialogUtils {

	public static Dialog getCustomDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		//检查报错原因
		return dialog;
	}
	
	public static Dialog getWinDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		return dialog;
	}
}
