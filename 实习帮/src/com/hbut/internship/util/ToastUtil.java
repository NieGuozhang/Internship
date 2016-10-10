package com.hbut.internship.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 用于提醒用户的Toast封装，避免多次点击出现多次提醒。
 */
public class ToastUtil {

	private static Toast toast;

	public static void showToast(Context context, String content) {

		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	public static void showtoast(String content) {
		if (toast == null) {
			toast = Toast.makeText(MyApplicationUtil.getContext(), content,
					Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}
}
