package com.sleep.updatedata.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.taiir.sleepcare.home.R;
import com.loopj.android.application.MyApplication;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.dialog.SubmitDialog;
import com.sleep.dialog.ViewEssDialog;
import com.sleep.dialog.ViewMedicalHistory;
import com.sleep.local.classs.UserManager;
import com.sleep.local.classs.UserNumber;
import com.sleep.sleepservice.WebHelper;
import com.sleep.utils.DensityUtil;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.NetworkProber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.User;
import taiyi.web.model.dto.Status;

public class PersonalDataActivity extends Activity implements OnValueChangeListener, OnScrollListener, Formatter {
	// 返回
	private ImageView backIV;
	private SharedPreferences msharedPreferences;
	private SharedPreferences.Editor mEditor;
	// 日期函数
	private Calendar calendar;
	private EditText usernameTV;
	private TextView userageTV, usersexTV, weightTV, heightTV;
	private EditText ucell;
	private EditText uemail;

	private Button pdsubmit;

	private NumberPicker picker1;
	private Button surebtn;
	private NumberPicker picker2;

	private int pdid;
	private PublicDao pd;
	private UserManager um;

	private Button lookbs;
	private Button lookess;
	private ViewMedicalHistory vmh;
	private ViewEssDialog ved;
	private SubmitDialog sdialog;
	private Handler handler;

	// hide or show
	// private TextView u_personal;
	private RelativeLayout r_call;
	private RelativeLayout r_eamil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_data_fragment);
		ExitActivity.getInstance().addActivity(this);
		msharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
		mEditor = msharedPreferences.edit();
		Intent intent = getIntent();
		pdid = intent.getIntExtra("mid", -1);
		if (pdid != -1) {
			mEditor.putInt("muserid", pdid).commit();
		}
		if (pdid == -1) {
			pdid = msharedPreferences.getInt("muserid", -1);
		}
		pd = new PublicDaoImpl(PersonalDataActivity.this);
		um = pd.findAllUserById(pdid);
		vmh = new ViewMedicalHistory(PersonalDataActivity.this);
		ved = new ViewEssDialog(PersonalDataActivity.this);
		sdialog = new SubmitDialog(PersonalDataActivity.this);

		inintgroup();
		if (um == null && um.getUsername() == null) {
			usernameTV.setText("");
			usersexTV.setText("");
			userageTV.setText("");
			weightTV.setText("");
			heightTV.setText("");
			ucell.setText("");
			uemail.setText("");
			Toast.makeText(PersonalDataActivity.this, PersonalDataActivity.this.getString(R.string.please_add_user),
					Toast.LENGTH_SHORT).show();
		} else {
			usernameTV.setText(um.getUsername());
			if ("男".equals(um.getUsersex()) || "male".equalsIgnoreCase(um.getUsersex())) {
				usersexTV.setText(PersonalDataActivity.this.getString(R.string.male));
				mEditor.putString("sex", PersonalDataActivity.this.getString(R.string.male));
				mEditor.commit();
			} else {
				usersexTV.setText(PersonalDataActivity.this.getString(R.string.female));
				mEditor.putString("sex", PersonalDataActivity.this.getString(R.string.female));
				mEditor.commit();
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + MyApplication.getContext().getString(R.string.years)
					+ "MM" + MyApplication.getContext().getString(R.string.months) + "dd"
					+ MyApplication.getContext().getString(R.string.days));
			try {
				userageTV.setText(sdf.format(format.parse(um.getUserage())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			weightTV.setText(String.valueOf(um.getUserweight()));
			heightTV.setText(String.valueOf(um.getUserheight()));
			if (um.getMobilenumber() != null && !"".equals(um.getMobilenumber())) {
				ucell.setText(um.getMobilenumber());
				r_call.setVisibility(View.VISIBLE);
			} else {
				r_call.setVisibility(View.GONE);
			}
			if (um.getUseremail() != null && !"".equals(um.getUseremail())) {
				uemail.setText(um.getUseremail());
				r_eamil.setVisibility(View.VISIBLE);
			} else {
				r_eamil.setVisibility(View.GONE);
			}

		}
	}

	private void inintgroup() {
		backIV = (ImageView) findViewById(R.id.imageView2_personal);
		backIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			}
		});
		lookbs = (Button) findViewById(R.id.bs_history_personal);
		lookbs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				vmh.viewDialog(PersonalDataActivity.this.getString(R.string.view_medical_history));
			}
		});
		lookess = (Button) findViewById(R.id.ss_ess_personal);
		lookess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				ved.viewDialog(PersonalDataActivity.this.getString(R.string.degree_of_sleepiness));
			}
		});
		calendar = Calendar.getInstance();
		// u_personal = (TextView) findViewById(R.id.u_personal);
		// String upersonal = u_personal.getText().toString();
		r_call = (RelativeLayout) findViewById(R.id.u_call);
		r_eamil = (RelativeLayout) findViewById(R.id.u_email);

		// if ("个人资料".equals(upersonal)) {
		// r_call.setVisibility(View.VISIBLE);
		// r_eamil.setVisibility(View.GONE);
		// } else if ("Current Profile".equals(upersonal)) {
		// r_call.setVisibility(View.GONE);
		// r_eamil.setVisibility(View.VISIBLE);
		// }
		ucell = (EditText) findViewById(R.id.pucell_ucell);
		ucell.setFocusable(false);
		ucell.setEnabled(false);

		uemail = (EditText) findViewById(R.id.e_email);
		uemail.setFocusable(false);
		uemail.setEnabled(false);

		usernameTV = (EditText) findViewById(R.id.editText2_personal);
		usernameTV.setFocusable(false);
		usernameTV.setEnabled(false);

		userageTV = (TextView) findViewById(R.id.editText3_personal);
		userageTV.setInputType(InputType.TYPE_NULL);
		userageTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				final Calendar c = Calendar.getInstance();
				try {
					Date date = sdf.parse(um.getUserage());
					c.setTime(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int y = c.get(Calendar.YEAR);
				int m = c.get(Calendar.MONTH);
				int d = c.get(Calendar.DAY_OF_MONTH);
				c.set(y, m, d, 0, 0, 0);
				DatePickerDialog dialog = new DatePickerDialog(PersonalDataActivity.this,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
								String str = year + PersonalDataActivity.this.getString(R.string.years) + (month + 1)
										+ PersonalDataActivity.this.getString(R.string.months) + dayOfMonth
										+ PersonalDataActivity.this.getString(R.string.days);
								// Calendar mCalendar = Calendar.getInstance();
								// mCalendar.set(year, month, dayOfMonth, 23,
								// 59, 59);
								// if (mCalendar.getTimeInMillis() <
								// System.currentTimeMillis()) {
								userageTV.setText(str);
								// }
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				dialog.getDatePicker().setMaxDate(new Date().getTime());
				dialog.show();
			}
		});
		usersexTV = (TextView) findViewById(R.id.usersex_personal);
		usersexTV.setFocusable(false);
		usersexTV.setEnabled(false);
		usersexTV.setInputType(InputType.TYPE_NULL);
		usersexTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow3(usersexTV);
			}
		});

		weightTV = (TextView) findViewById(R.id.editText4_personal);
		weightTV.setInputType(InputType.TYPE_NULL);
		weightTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow2(weightTV);
			}
		});

		heightTV = (TextView) findViewById(R.id.editText5_personal);
		heightTV.setInputType(InputType.TYPE_NULL);
		heightTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow1(heightTV);
			}
		});
		// 修改
		pdsubmit = (Button) findViewById(R.id.submit_personal);
		pdsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String userage = userageTV.getText().toString();
				String userweight = weightTV.getText().toString();
				String userheight = heightTV.getText().toString();
				// String cell = ucell.getText().toString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy" + MyApplication.getContext().getString(R.string.years) + "MM"
								+ MyApplication.getContext().getString(R.string.months) + "dd"
								+ MyApplication.getContext().getString(R.string.days));
				pd = new PublicDaoImpl(PersonalDataActivity.this);
				um = pd.findAllUserById(pdid);

				// if (cell.length() < 11) {
				// Toast.makeText(PersonalDataActivity.this,
				// PersonalDataActivity.this.getString(R.string.please_enter_the_correct_phone_number),
				// Toast.LENGTH_SHORT).show();
				// } else {
				boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
				boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
				if (isConnected || isConnected3g) {
					if (!userage.equals(um.getUserage()) || !userweight.equals(String.valueOf(um.getUserweight()))
							|| !userheight.equals(String.valueOf(um.getUserheight()))) {
						mHandler.sendEmptyMessage(2);
						String uage;
						try {
							uage = sdf1.format(sdf.parse(userage));
							um.setUserage(uage);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						um.setUserweight(Integer.parseInt(userweight));
						um.setUserheight(Integer.parseInt(userheight));
						int USER_BMI = 0;
						int wt = Integer.parseInt(userweight);
						float ht = Integer.parseInt(userheight) / 100f;
						if (ht != 0) {
							USER_BMI = (int) (wt / (ht * ht));
						} else {
							USER_BMI = 0;
						}
						um.setUserbmi(USER_BMI);
						// um.setMobilenumber(cell);

						new Thread() {
							public void run() {
								pd = new PublicDaoImpl(PersonalDataActivity.this);
								final UserNumber un = pd.findTelorEmail();
								try {
									if (un != null && un.getToken() != null) {
										Status testToken = new WebAPI().testToken(un.getToken());
										if (!Status.isSuccess(testToken)) {
											mHandler.sendEmptyMessage(1);
										} else {
											User user = WebHelper.userManagerToUser(um);
											// Looper.prepare();
											Thread.sleep(3000);
											Status status = new WebAPI().updateUser(user);
											Log.d("TAG", "status.getCode() = " + status.getCode());
											if (Status.isSuccess(status)) {
												pd.modifyusermanager(um);
												PersonalDataActivity.this.finish();
												Log.d("TAG", "用户修改成功!" + status);
												mHandler.sendEmptyMessage(0);
												overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
											} else {
												Log.d("TAG", "用户修改失败!" + status);
												mHandler.sendEmptyMessage(1);
											}
											// finally {
											// Looper.loop();
											// }
											mHandler.sendEmptyMessage(3);
										}
									} else {
										User user = WebHelper.userManagerToUser(um);
										// Looper.prepare();
										Thread.sleep(3000);
										Status status = new WebAPI().updateUser(user);
										Log.d("TAG", "status.getCode() = " + status.getCode());
										if (Status.isSuccess(status)) {
											pd.modifyusermanager(um);
											PersonalDataActivity.this.finish();
											Log.d("TAG", "用户修改成功!" + status);
											mHandler.sendEmptyMessage(0);
											overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
										} else {
											Log.d("TAG", "用户修改失败!" + status);
											mHandler.sendEmptyMessage(1);
										}
										// finally {
										// Looper.loop();
										// }
										mHandler.sendEmptyMessage(3);
									}
								} catch (Exception e) {
									e.printStackTrace();
									mHandler.sendEmptyMessage(1);
								}
							}
						}.start();
					} else {
						Toast.makeText(PersonalDataActivity.this,
								PersonalDataActivity.this.getString(R.string.no_modification), Toast.LENGTH_LONG)
								.show();
					}
				} else {
					mHandler.sendEmptyMessage(3);
					Toast.makeText(PersonalDataActivity.this,
							PersonalDataActivity.this.getString(R.string.modify_failed_please_open_the_network),
							Toast.LENGTH_LONG).show();
				}
				// }
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(PersonalDataActivity.this,
						PersonalDataActivity.this.getString(R.string.success_of_user_information_modification),
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(PersonalDataActivity.this,
						PersonalDataActivity.this.getString(R.string.user_information_modification_failed),
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Log.d("TAG", "mHandler, case 2:");
				if (sdialog == null) {
					sdialog = new SubmitDialog(PersonalDataActivity.this);
				}
				sdialog.setContent(
						PersonalDataActivity.this.getString(R.string.modifying_user_information_please_wait));
				sdialog.show();
				break;
			case 3:
				Log.d("TAG", "mHandler, case 3:");
				if (sdialog != null && sdialog.isShowing()) {
					sdialog.cancel();
				}
				break;
			default:
				break;
			}
		}
	};

	// 性别
	private void showpopwindow3(View view) {
		View contentView = LayoutInflater.from(PersonalDataActivity.this).inflate(R.layout.updatenumberpicker, null);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		picker2 = (NumberPicker) contentView.findViewById(R.id.update_numpicker_number);
		surebtn = (Button) contentView.findViewById(R.id.button1_update);
		// surebtn.getBackground().setAlpha(1);
		initPop3();
		final PopupWindow pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pw.setTouchable(true);
		pw.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("TAG", "");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		pw.setBackgroundDrawable(new BitmapDrawable());

		pw.showAsDropDown(view, 0, DensityUtil.dip2px(this, 15));

		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				pw.dismiss();
			}
		});
		surebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int values = picker2.getValue();
				if (values == 0) {
					usersexTV.setText(PersonalDataActivity.this.getString(R.string.male));
				} else {
					usersexTV.setText(PersonalDataActivity.this.getString(R.string.female));
				}
				pw.dismiss();
			}
		});
	}

	private void initPop3() {
		picker2.setFormatter(this);
		picker2.setOnValueChangedListener(this);
		picker2.setOnScrollListener(this);

		String[] sex_m_f = { PersonalDataActivity.this.getString(R.string.male),
				PersonalDataActivity.this.getString(R.string.female) };
		picker2.setDisplayedValues(sex_m_f);
		picker2.setMinValue(0);
		picker2.setMaxValue(sex_m_f.length - 1);
		picker2.setValue(0);

	}

	// 体重
	private void showpopwindow2(View view) {
		View contentView = LayoutInflater.from(PersonalDataActivity.this).inflate(R.layout.updatenumberpicker, null);
		picker1 = (NumberPicker) contentView.findViewById(R.id.update_numpicker_number);
		surebtn = (Button) contentView.findViewById(R.id.button1_update);
		// surebtn.getBackground().setAlpha(1);
		initPop2();
		final PopupWindow pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pw.setTouchable(true);
		pw.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("TAG", "");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		pw.setBackgroundDrawable(new BitmapDrawable());

		pw.showAsDropDown(view, 0, DensityUtil.dip2px(this, 8));
		// pw.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				pw.dismiss();
			}
		});
		surebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * NumberPicker.getValue() 获取当前选定值，返回值int类型
				 */
				int values = picker1.getValue();
				weightTV.setText(String.valueOf(values));
				pw.dismiss();
			}
		});
	}

	private void initPop2() {
		picker1.setFormatter(this);
		picker1.setOnValueChangedListener(this);
		picker1.setOnScrollListener(this);

		picker1.setMinValue(3);
		picker1.setMaxValue(260);
		picker1.setValue(um.getUserweight());

	}

	// 身高
	private void showpopwindow1(View view) {
		View contentView = LayoutInflater.from(PersonalDataActivity.this).inflate(R.layout.updatenumberpicker, null);
		picker1 = (NumberPicker) contentView.findViewById(R.id.update_numpicker_number);
		surebtn = (Button) contentView.findViewById(R.id.button1_update);
		// surebtn.getBackground().setAlpha(1);
		initPop1();
		final PopupWindow pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pw.setTouchable(true);
		pw.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("TAG", "");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		pw.setBackgroundDrawable(new BitmapDrawable());

		pw.showAsDropDown(view, 0, DensityUtil.dip2px(this, 5));
		// pw.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				pw.dismiss();
			}
		});
		surebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * NumberPicker.getValue() 获取当前选定值，返回值int类型
				 */
				int values = picker1.getValue();
				heightTV.setText(String.valueOf(values));
				pw.dismiss();
			}
		});
	}

	private void initPop1() {
		picker1.setFormatter(this);
		picker1.setOnValueChangedListener(this);
		picker1.setOnScrollListener(this);

		picker1.setMinValue(50);
		picker1.setMaxValue(260);
		picker1.setValue(um.getUserheight());
	}

	@Override
	public String format(int value) {
		String tmpStr = String.valueOf(value);
		if (value < 10) {
			// tmpStr = "0" + tmpStr;
			Log.i("tmpStr", "tmpStr");
		}
		return tmpStr;
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		Log.i("TAG", "");
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		Log.i("TAG", "");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}
