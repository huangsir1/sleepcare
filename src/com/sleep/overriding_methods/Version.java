package com.sleep.overriding_methods;

import com.loopj.android.application.MyApplication;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
/**
 * 获取android版本
 */
public class Version {
	/**
	 * android:versionName——字符串值，代表应用程序的版本信息，需要显示给用户
	 */
	public static String getVersionName() {
		try {
			PackageManager manager = MyApplication.getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
			String version = info.versionName;
			return "android-" + version;
		} catch (Exception e) {
			e.printStackTrace();
			return "android";
		}
	}

	/**
	 * android:versionCode——整数值，代表应用程序代码的相对版本，也就是版本更新过多少次。
	 */
	public static String getVersionCode() {
		try {
			PackageManager manager = MyApplication.getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
			String versionCode = String.valueOf(info.versionCode);
			return "android-" + versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return "android";
		}
	}
	/**
	 * 调用方法
	 * 
	  	String versionName = Version.getVersionName(); String versionCode =
	  	Version.getVersionCode(); Log.d("TAG", "睡眠管家的版本:" + "versionName : " +
	  	versionName + "\n" + "versionCode : " + versionCode);
	 *
	 */
}
