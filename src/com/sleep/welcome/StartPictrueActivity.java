package com.sleep.welcome;

import com.sleep.usermanager.UserChangeActivity;
import com.sleep.usermanager.UserEmailRegisterActivity;
import com.sleep.usermanager.UserRegisterActivity;
import com.sleep.utils.ExitActivity;
import com.taiir.sleepcare.home.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class StartPictrueActivity extends Activity {
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private static final int LOAD_DISPLAY_TIME = 1000;
	public static StartPictrueActivity instance = null;
	private int removeregister;
	private String welcometext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_picture_activity);
		ExitActivity.getInstance().addActivity(this);
		mSharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		Intent intent = getIntent();
		welcometext = intent.getStringExtra("enterBtn");
		removeregister = mSharedPreferences.getInt("removeregister", 0);
		instance = this;
		// 登录状态
		getInfo();
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (removeregister == 1) {
					Intent intent = new Intent(StartPictrueActivity.this, UserChangeActivity.class);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.fade, R.anim.hold);
				} else {
					if ("立即体验".equals(welcometext)) {
						Intent intent = new Intent(StartPictrueActivity.this, UserRegisterActivity.class);
						startActivity(intent);
						StartPictrueActivity.this.finish();
						overridePendingTransition(R.anim.fade, R.anim.hold);
					} else {
						Intent intent = new Intent(StartPictrueActivity.this, UserEmailRegisterActivity.class);
						startActivity(intent);
						StartPictrueActivity.this.finish();
						overridePendingTransition(R.anim.fade, R.anim.hold);
					}
				}

			}
		}, LOAD_DISPLAY_TIME);
	}
	// 登录状态及引导页
	private void getInfo() {
		mEditor.putInt("startactivity", 1);
		mEditor.commit();
	}
}
