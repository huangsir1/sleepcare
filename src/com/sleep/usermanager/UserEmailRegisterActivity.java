package com.sleep.usermanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserNumber;
import com.sleep.overriding_methods.Version;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;
import com.taiir.sleepcare.home.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserEmailRegisterActivity extends Activity {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private EditText memail;
	private Button msubmit;

	private PublicDao pd;
	private UserNumber un;
	public static UserEmailRegisterActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_email);
		ExitActivity.getInstance().addActivity(this);
		sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		instance = this;

		memail = (EditText) findViewById(R.id.email_editor);
		msubmit = (Button) findViewById(R.id.verification_login_btn_email);
		msubmit.setInputType(InputType.TYPE_NULL);
		msubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String eemail = memail.getText().toString().trim();
				String email_verification = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
				// String email_verification =
				// "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
				Pattern p = Pattern.compile(email_verification);
				Matcher m = p.matcher(eemail);
				boolean flag = m.matches();
				if (eemail.length() == 0 && "".equals(eemail)) {
					Toast.makeText(UserEmailRegisterActivity.this,
							UserEmailRegisterActivity.this.getString(R.string.imput_email), Toast.LENGTH_SHORT).show();
				} else if (!flag) {
					Toast.makeText(UserEmailRegisterActivity.this,
							UserEmailRegisterActivity.this.getString(R.string.email_number_input_error),
							Toast.LENGTH_SHORT).show();
				} else {
					pd = new PublicDaoImpl(UserEmailRegisterActivity.this);
					un = new UserNumber();
					String version = Version.getVersionName();
					un.setSleepcareversion(version);
					un.setEmails(eemail);
					pd.addUserLogin(un);
					editor.putInt("removeregister", 1);
					editor.commit();
					Intent intent = new Intent(UserEmailRegisterActivity.this, UserChangeActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		// textView1_email = (TextView) findViewById(R.id.textView1_email);
		msubmit = (Button) findViewById(R.id.verification_login_btn_email);
		String submit_text = msubmit.getText().toString();

		if ("验证并登陆".equals(submit_text)) {
			if (UserRegisterActivity.instance != null) {
				UserRegisterActivity.instance.finish();
			}
			Intent intent = new Intent(UserEmailRegisterActivity.this, UserRegisterActivity.class);
			startActivity(intent);
			finish();
		}
		Log.e("TAG", "UserEmailRegisterActivity.this" + "finish()");
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 判断是否2秒之内退出
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// Object mHelperUtils;
				Toast.makeText(this, UserEmailRegisterActivity.this.getString(R.string.then_click_one_exit_procedure),
						Toast.LENGTH_SHORT).show();
				// 获得当前的时间
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				ExitActivity.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
