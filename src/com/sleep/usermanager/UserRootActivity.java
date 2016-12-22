package com.sleep.usermanager;

import java.util.Calendar;
import java.util.Date;

import com.taiir.sleepcare.home.R;
import com.sleep.updatedata.activity.SwitchUserActivity;
import com.sleep.utils.DensityUtil;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

public class UserRootActivity extends Activity implements OnValueChangeListener, OnScrollListener, Formatter {
	private EditText rnameET;
	private TextView rsexET, rheiET, rweiET;
	private TextView rageET;
	private Button submitBtn;

	private NumberPicker picker1;
	private Button npbutton;

	private NumberPicker picker2;
	private ImageView backiv;

	private SharedPreferences msharedPreferences;
	private SharedPreferences.Editor mEditor;
	public static UserRootActivity instance = null;
	private int suactivityid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_root);
		ExitActivity.getInstance().addActivity(this);
		instance = this;
		msharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
		mEditor = msharedPreferences.edit();
		Intent intent = getIntent();
		suactivityid = intent.getIntExtra("suid", -1);
		initGroup();
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				String name = rnameET.getText().toString().trim();
				String sex = rsexET.getText().toString().trim();
				String age = rageET.getText().toString();
				String weight = rweiET.getText().toString().trim();
				String height = rheiET.getText().toString().trim();

				if (name.length() == 0) {
					Toast.makeText(UserRootActivity.this, UserRootActivity.this.getString(R.string.input_username),
							Toast.LENGTH_SHORT).show();
				} else if (name.length() != 0 && sex.length() == 0) {
					Toast.makeText(UserRootActivity.this, UserRootActivity.this.getString(R.string.input_gender),
							Toast.LENGTH_SHORT).show();
				} else if (name.length() != 0 && sex.length() != 0 && age.length() == 0) {
					Toast.makeText(UserRootActivity.this, UserRootActivity.this.getString(R.string.input_birthday),
							Toast.LENGTH_SHORT).show();
				} else if (name.length() != 0 && sex.length() != 0 && age.length() != 0 && weight.length() == 0) {
					Toast.makeText(UserRootActivity.this, UserRootActivity.this.getString(R.string.input_weight),
							Toast.LENGTH_SHORT).show();
				} else if (name.length() != 0 && sex.length() != 0 && age.length() != 0 && weight.length() != 0
						&& height.length() == 0) {
					Toast.makeText(UserRootActivity.this, UserRootActivity.this.getString(R.string.input_height),
							Toast.LENGTH_SHORT).show();
				} else if (name != null && !"".equals(name) && sex != null && !"".equals(sex) && age != null
						&& !"".equals(age) && weight != null && !"".equals(weight) && height != null
						&& !"".equals(height)) {

					Intent intent = new Intent();
					intent.putExtra("name", name);
					intent.putExtra("sex", sex);
					intent.putExtra("age", age);
					intent.putExtra("weight", weight);
					intent.putExtra("height", height);
					if ((UserRootActivity.this.getString(R.string.male)).equals(sex)) {
						intent.setClass(UserRootActivity.this, UserMediaclFHistory.class);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					} else if ((UserRootActivity.this.getString(R.string.female)).equals(sex)) {
						intent.setClass(UserRootActivity.this, UserMediaclMHistory.class);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					} else {
						// Toast.makeText(UserRootActivity.this,
						// UserRootActivity.this.getString(R.string.unknown_error_please_fill_in),
						// Toast.LENGTH_SHORT).show();
						Log.d("TAG", "未知错误");
					}
				}
			}
		});

	}

	// 体重
	private void showpopwindow1(View view) {
		View contentView = LayoutInflater.from(this).inflate(R.layout.numberpicker, null);
		picker1 = (NumberPicker) contentView.findViewById(R.id.numpicker_number);
		npbutton = (Button) contentView.findViewById(R.id.numpicker_button1);
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

		pw.showAsDropDown(view, 0, DensityUtil.dip2px(this, 8));
		// pw.showAtLocation(contentView, Gravity.BOTTOM, 0, 2);

		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				pw.dismiss();
			}
		});
		npbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * NumberPicker.getValue() 获取当前选定值，返回值int类型
				 */
				int values = picker1.getValue();
				rweiET.setText(String.valueOf(values));
				pw.dismiss();
			}
		});
	}

	private void initPop1() {
		picker1.setFormatter(this);
		picker1.setOnValueChangedListener(this);
		picker1.setOnScrollListener(this);

		picker1.setMinValue(3);
		picker1.setMaxValue(260);
		picker1.setValue(60);

	}

	// 身高
	private void showpopwindow2(View view) {
		View contentView = LayoutInflater.from(this).inflate(R.layout.numberpicker, null);
		picker1 = (NumberPicker) contentView.findViewById(R.id.numpicker_number);
		npbutton = (Button) contentView.findViewById(R.id.numpicker_button1);
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

		// pw.showAsDropDown(view, 0, DensityUtil.dip2px(this, 5));
		pw.showAtLocation(contentView, Gravity.BOTTOM, 0, 2);

		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				pw.dismiss();
			}
		});
		npbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * NumberPicker.getValue() 获取当前选定值，返回值int类型
				 */
				int values = picker1.getValue();
				rheiET.setText(String.valueOf(values));
				pw.dismiss();
			}
		});
	}

	private void initPop2() {
		picker1.setFormatter(this);
		picker1.setOnValueChangedListener(this);
		picker1.setOnScrollListener(this);

		picker1.setMinValue(50);
		picker1.setMaxValue(260);
		picker1.setValue(160);

	}

	// 性别
	private void showpopwindow3(View view) {
		View contentView = LayoutInflater.from(this).inflate(R.layout.numberpicker, null);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		picker2 = (NumberPicker) contentView.findViewById(R.id.numpicker_number);
		npbutton = (Button) contentView.findViewById(R.id.numpicker_button1);
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
		npbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int values = picker2.getValue();
				if (values == 0) {
					rsexET.setText(UserRootActivity.this.getString(R.string.male));
				} else {
					rsexET.setText(UserRootActivity.this.getString(R.string.female));
				}
				pw.dismiss();
			}
		});
	}

	private void initPop3() {
		picker2.setFormatter(this);
		picker2.setOnValueChangedListener(this);
		picker2.setOnScrollListener(this);

		String[] sex_m_f = { UserRootActivity.this.getString(R.string.male),
				UserRootActivity.this.getString(R.string.female) };
		picker2.setDisplayedValues(sex_m_f);
		picker2.setMinValue(0);
		picker2.setMaxValue(sex_m_f.length - 1);
		picker2.setValue(0);

	}

	private void initGroup() {
		backiv = (ImageView) findViewById(R.id.imageView1_one);
		backiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int backi = msharedPreferences.getInt("ucai", -1);
				int backd = msharedPreferences.getInt("swuai", -1);
				if (1 == backi) {
					Intent intent = new Intent(UserRootActivity.this, UserChangeActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
					finish();
				} else if (1 == backd) {
					Intent intent = new Intent(UserRootActivity.this, SwitchUserActivity.class);
					intent.putExtra("suid", suactivityid);
					startActivity(intent);
					overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
					finish();
				}
			}
		});
		// 姓名
		rnameET = (EditText) findViewById(R.id.tname_one);
		// 性别
		rsexET = (TextView) findViewById(R.id.tsex_one);
		rsexET.setInputType(InputType.TYPE_NULL);
		rsexET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow3(rsexET);
			}
		});

		// 年龄-生日
		rageET = (TextView) findViewById(R.id.tage_one);
		rageET.setInputType(InputType.TYPE_NULL);
		// rageET 监听
		rageET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				// c.set(1970, 0, 1,0,0,0);
				c.set(1980, Calendar.JANUARY, 1, 0, 0, 0);
				DatePickerDialog dialog = new DatePickerDialog(UserRootActivity.this,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
								// String str = year + "年" + (month + 1) + "月"
								// // + dayOfMonth + "日";
								String str = year + UserRootActivity.this.getString(R.string.years) + (month + 1)
										+ UserRootActivity.this.getString(R.string.months) + dayOfMonth
										+ UserRootActivity.this.getString(R.string.days);
								// Calendar mCalendar = Calendar.getInstance();
								// mCalendar.set(year, month, dayOfMonth, 23,
								// 59, 59);
								// if (mCalendar.getTimeInMillis() <
								// System.currentTimeMillis()) {
								rageET.setText(str);
								// }
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				// long time = (new Date().getTime())-(24*60*60*1000);
				// dialog.getDatePicker().setMaxDate(time);
				// new date
				dialog.getDatePicker().setMaxDate(new Date().getTime());
				dialog.show();
			}
		});

		// 体重
		rweiET = (TextView) findViewById(R.id.twid_one);
		rweiET.setInputType(InputType.TYPE_NULL);
		rweiET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow1(rweiET);
			}
		});

		// 身高
		rheiET = (TextView) findViewById(R.id.thei_one);
		rheiET.setInputType(InputType.TYPE_NULL);
		rheiET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showpopwindow2(rheiET);
			}
		});
		submitBtn = (Button) findViewById(R.id.submit_one);
	}

	/**
	 * 格式化数据
	 */
	@Override
	public String format(int value) {
		String tmpStr = String.valueOf(value);
		if (value < 10) {
			// tmpStr = "0" + tmpStr;
			Log.i("tmpStr", "tmpStr");
		}
		return tmpStr;
	}

	/**
	 * 滑动状态
	 */
	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		Log.i("TAG", "");
	}

	/**
	 * 取值
	 */
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		Log.i("TAG", "");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Log.e("onkeydown", keyCode+"");
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			int backi = msharedPreferences.getInt("ucai", -1);
			int backd = msharedPreferences.getInt("swuai", -1);
			if (1 == backi) {
				Intent intent = new Intent(UserRootActivity.this, UserChangeActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
				finish();
			} else if (1 == backd) {
				Intent intent = new Intent(UserRootActivity.this, SwitchUserActivity.class);
				intent.putExtra("suid", suactivityid);
				startActivity(intent);
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
