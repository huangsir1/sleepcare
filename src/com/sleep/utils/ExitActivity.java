package com.sleep.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**退出所有activity的方法*/
public class ExitActivity extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ExitActivity instance;

	private ExitActivity() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static ExitActivity getInstance() {
		if (null == instance) {
			instance = new ExitActivity();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	/*
	 * 在每一个Activity中的onCreate方法里添加该Activity到MyApplication对象实例容器中
	 * 
	 * MyApplication.getInstance().addActivity(this);
	 * 
	 * 在需要结束所有Activity的时候调用exit方法
	 * 
	 * MyApplication.getInstance().exit();
	 */
	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);

	}
}
