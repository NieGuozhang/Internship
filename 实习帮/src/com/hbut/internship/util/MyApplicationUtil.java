package com.hbut.internship.util;

import org.litepal.LitePalApplication;
import android.content.Context;

/**
 * 获取当前的context
 */
public class MyApplicationUtil extends LitePalApplication {

	private static Context context;

	public static Context getContext() {
		return context=LitePalApplication.getContext();
	}
}
