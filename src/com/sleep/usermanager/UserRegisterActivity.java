package com.sleep.usermanager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.loopj.android.application.MyApplication;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.overriding_methods.Version;
import com.sleep.sleepservice.WebToLocal;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.NetworkProber;
import com.taiir.sleepcare.home.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.SMSValidate;
import taiyi.web.model.dto.UserEssAndDHDto;

public class UserRegisterActivity extends Activity {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private EditText inputMobileET, inputsuncodeET;
	private Button obtainCodeTV;

	private int i = 60;
	private SmsObserver smsObserver;
	private Button loginBtn;
	private PublicDao pd;
	private UserNumber un;
	public static UserRegisterActivity instance = null;
	private String phones;
	private String codes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register_layout);
		ExitActivity.getInstance().addActivity(this);
		sharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		instance = this;
		inputMobileET = (EditText) findViewById(R.id.phone_editor);
		inputsuncodeET = (EditText) findViewById(R.id.phone_codes);
		loginBtn = (Button) findViewById(R.id.verification_login_btn);
		smsObserver = new SmsObserver(this, smsHandler);
		getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {
					phones = inputMobileET.getText().toString().trim();
					codes = inputsuncodeET.getText().toString().trim();
					String phone_verification = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
					Pattern p = Pattern.compile(phone_verification);
					Matcher m = p.matcher(phones);
					boolean flag = m.matches();
					if (phones.length() == 0 && "".equals(phones)) {
						Toast.makeText(UserRegisterActivity.this,
								UserRegisterActivity.this.getString(R.string.input_phone), Toast.LENGTH_SHORT).show();
					} else if (!flag) {
						Toast.makeText(UserRegisterActivity.this,
								UserRegisterActivity.this.getString(R.string.phone_number_input_error),
								Toast.LENGTH_SHORT).show();
					} else if (codes.length() == 0 && "".equals(codes)) {
						Toast.makeText(UserRegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								SMSValidate sms = new WebAPI().checkSMS(phones, codes);
								pd = new PublicDaoImpl(UserRegisterActivity.this);
								if (sms.isSuccess()) {
									// 清空
									pd.truncateUserLogin();
									pd.truncateUserManager();
									pd.truncateDiabetesHy();
									pd.truncateEpworth();
									pd.truncateHospitals();
									pd.truncateUserHistory();

									// if (1 == 1) {
									un = new UserNumber();
									String version = Version.getVersionName();
									un.setSleepcareversion(version);
									un.setUsertel(phones);
									// String testtoken =
									// "f3ca44c1b1c944b9a7ea1547fb570dc6";
									// String testtoken = "123";
									un.setToken(sms.getMessage());
									// un.setToken(testtoken);
									pd.addUserLogin(un);
									// String token =
									// sharedPreferences.getString("token",
									// null);
									// Log.d("TAG", "token = " + token);

									UserNumber findTelorEmail = pd.findTelorEmail();
									try {
										if (findTelorEmail != null && findTelorEmail.getToken() != null) {
											String token = findTelorEmail.getToken();
											Log.d("TAG", "token = " + token);
											List<UserEssAndDHDto> userByTokens = new WebAPI().getUserByToken(token);

											for (UserEssAndDHDto userByToken : userByTokens) {
												UserManager userManager = WebToLocal.webtousermanager(userByToken);
												Epworth epworth = WebToLocal.webtoEpworth(userByToken);
												DiabetesHy webtouserMedicalhistory = WebToLocal
														.webtouserMedicalhistory(userByToken);
												epworth.setUpload(1);
												webtouserMedicalhistory.setUpload(1);
												userManager.setUpload(1);
												pd.addUser(userManager);
												pd.addEpworth(epworth);
												pd.addDiabetesHy(webtouserMedicalhistory);
											}
											mSendHandler.sendEmptyMessage(2);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									mSendHandler.sendEmptyMessage(1);
								}
							}
						}).start();
					}
				} else {
					Toast.makeText(UserRegisterActivity.this,
							UserRegisterActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		initPhone();
		obtainCodeTV = (Button) findViewById(R.id.obtain_codes_btn);
		// 验证码监听
		obtainCodeTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String phoneNumbers = inputMobileET.getText().toString();
				String phone_verification = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
				Pattern p = Pattern.compile(phone_verification);
				Matcher m = p.matcher(phoneNumbers);
				boolean flag = m.matches();
				if (phoneNumbers.length() == 0 && "".equals(phoneNumbers)) {
					Toast.makeText(UserRegisterActivity.this, UserRegisterActivity.this.getString(R.string.input_phone),
							Toast.LENGTH_SHORT).show();
				} else if (!flag) {
					Toast.makeText(UserRegisterActivity.this,
							UserRegisterActivity.this.getString(R.string.phone_number_input_error), Toast.LENGTH_SHORT)
							.show();
				} else {
					Log.i("TAG", "此号码可以注册，请点击获取验证码！");
					SMSSDK.getVerificationCode("86", phoneNumbers);
					// 变为不可点击
					obtainCodeTV.setClickable(false);
					// // 按钮变灰
					// obtainCodeTV.setEnabled(false);
					obtainCodeTV.setText("Resend:" + i--);
					new Thread(new Runnable() {

						@Override
						public void run() {
							for (int i = 60; i > 0; i--) {
								handler.sendEmptyMessage(-9);
								if (i < 0)
									break;
								try {
									Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							handler.sendEmptyMessage(-8);
						}
					}).start();
				}
			}
		});
		//
		// // 短信
		// content = new SmsContent(new Handler());
		// // 注册短信变化监听
		// this.getContentResolver().registerContentObserver(
		// Uri.parse("content://sms/"), true, content);
	}

	private void initPhone() {
		// 启动短信验证sdk
		// SMSSDK.initSDK(this, "169eb0c6ab56c",
		// "1a6117952dd97af869de01930a4fc447");
		SMSSDK.initSDK(this, "162a091330664", "543340121b92e1c5a8ad9fe1908c2dd3");

		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {

				obtainCodeTV.setText(UserRegisterActivity.this.getString(R.string.re_send) + "(" + i-- + ")");
				// obtainCodeTV.setTextColor(Color.GRAY);
			} else if (msg.what == -8) {
				obtainCodeTV.setText("获取验证码");
				obtainCodeTV.setClickable(true);
				i = 60;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.i("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					//  短信注册成功后，返回MainActivity,然后提示新好友
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//  提交验证码成功 
						Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
						Log.i("TAG", "提交验证码成功");
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Toast.makeText(getApplicationContext(), "短信已发送", Toast.LENGTH_SHORT).show();
						Log.i("TAG", "短信已发送");
					} else {
						((Throwable) data).printStackTrace();
					}
				}
				if (result == SMSSDK.RESULT_ERROR) {
					try {
						Throwable throwable = (Throwable) data;
						throwable.printStackTrace();
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");// 错误描述
						int status = object.optInt("status");// 错误代码
						if (status > 0 && !TextUtils.isEmpty(des)) {
							Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	};

	/**
	 * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
	 * 
	 * @param str
	 *            短信内容
	 * @return 截取得到的6位动态密码
	 */
	public static String getDynamicPassword(String str) {
		// 4是验证码的位数一般为四位
		Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			System.out.print(m.group());
			dynamicPassword = m.group();
		}
		return dynamicPassword;
	}

	// 销毁
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
		// this.getContentResolver().unregisterContentObserver(content);
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	public Handler smsHandler = new Handler() {
		// 这里可以进行回调的操作
		public void handleMessage(android.os.Message msg) {
			System.out.println("smsHandler 执行了.....");
		};
	};

	private String body;

	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			// 每当有新短信到来时，使用我们获取短消息的方法
			getSmsFromPhone();
		}
	}

	protected void showToast(String text) {
		Toast.makeText(UserRegisterActivity.this, text, Toast.LENGTH_SHORT).show();
	}

	private Uri SMS_INBOX = Uri.parse("content://sms/");

	public void getSmsFromPhone() {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] { "body", "address", "person" };// "_id",
																			// "address",
		// "person",, "date",
		// "type
		String where = " date >  " + (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
			String number = cur.getString(cur.getColumnIndex("address"));// 手机号
			String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
			body = cur.getString(cur.getColumnIndex("body"));

			inputsuncodeET.setText(getDynamicPassword(body));

			System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
			System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
			System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);

			// 这里我是要获取自己短信服务号码中的验证码~~
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]{5}");
			Matcher matcher = pattern.matcher(body);// String
													// body="测试验证码2346ds";
			if (matcher.find()) {
				String res = matcher.group().substring(0, 5);// 获取短信的内容
				showToast(res);
				System.out.println(res);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// sleep_care_title = (TextView) findViewById(R.id.sleep_care_title);
		loginBtn = (Button) findViewById(R.id.verification_login_btn);
		String login_btn_text = loginBtn.getText().toString();

		if (!"验证并登陆".equals(login_btn_text)) {
			if (UserEmailRegisterActivity.instance != null) {
				UserEmailRegisterActivity.instance.finish();
			}
			Intent intent = new Intent(UserRegisterActivity.this, UserEmailRegisterActivity.class);
			startActivity(intent);
			finish();
		}
		Log.e("TAG", "UserRegisterActivity.this" + "finish()");
	}

	private long mExitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 判断是否2秒之内退出
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// Object mHelperUtils;
				Toast.makeText(this, UserRegisterActivity.this.getString(R.string.then_click_one_exit_procedure),
						Toast.LENGTH_SHORT).show();
				// 获得当前的时间
				mExitTime = System.currentTimeMillis();
			} else {
				// UserRegisterActivity.this.finish();
				// System.exit(0);
				// android.os.Process.killProcess(android.os.Process.myPid());
				finish();
				ExitActivity.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	private Handler mSendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(UserRegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Intent intent = new Intent(UserRegisterActivity.this, UserChangeActivity.class);
				obtainCodeTV.setClickable(true);
				editor.putInt("removeregister", 1);
				editor.commit();
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			default:
				break;
			}
		}
	};
}
