package com.sleep.overriding_methods;

import java.lang.Thread.UncaughtExceptionHandler;

import com.sleep.activity.MainActivity;
import com.sleep.utils.ExitActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * <h3>全局捕获异常</h3> <br>
 * 当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误日志
 * 
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private static CrashHandler instance = new CrashHandler();
	private Context mContext;
	
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			if (MainActivity.instance != null) {
				MainActivity.instance.finish();
			}
			ExitActivity.getInstance().exit();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息; 否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		try {
			// 重启应用（按需要添加是否重启应用）
			Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
//			Intent intent = new Intent(mContext, UserChangeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			SystemClock.sleep(600);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
