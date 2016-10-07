package com.hbut.internship.util;

import android.app.Application;

import android.content.Context;

/**
 * 获取当前的context
 */
public class MyApplicationUtil extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context = getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}
}
