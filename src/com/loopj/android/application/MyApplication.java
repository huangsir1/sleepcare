package com.loopj.android.application;



import com.sleep.overriding_methods.CrashHandler;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	private static Context context;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
//		CrashHandler.getInstance().init(this);
	}

	public static Context getContext() {
		return context;
	}
}
