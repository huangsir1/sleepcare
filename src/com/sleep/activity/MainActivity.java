package com.sleep.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.application.MyApplication;
import com.sleep.adapter.MyViewPagerAdapter;
import com.sleep.analysis.Sleep_ApneaDetection;
import com.sleep.analysis.Sleep_stageSeperate;
import com.sleep.bluetooth.BluetoothUtils;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.dialog.WaitDialog;
import com.sleep.fragment.InformationFragment;
import com.sleep.fragment.MonitorFragment;
import com.sleep.fragment.ReportFragment;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserHistory;
import com.sleep.runningman.CustomProgressDialog_Connected;
import com.sleep.runningman.CustomProgressDialog_Searching;
import com.sleep.runningman.CustomDialog_new;
import com.sleep.updatedata.activity.PersonalDataActivity;
import com.sleep.updatedata.activity.SwitchUserActivity;
import com.sleep.utils.CheckNotUploadFiles;
import com.sleep.utils.DeleteFileAndUserHistory;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.GZipUtils;
import com.sleep.utils.NetworkProber;
import com.sleep.utils.Utils;
import com.taiir.sleepcare.home.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private final String TAG = this.getClass().getSimpleName();
	private ViewPager pager;
	private ArrayList<Fragment> fragments;
	private MyViewPagerAdapter mAdapter;
	private ArrayList<RadioButton> btnTitles;
	public SlidingMenu menu;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private int cid;
	private PublicDao pdDel;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟

	// wanghanqing
	private Sleep_ApneaDetection mSleep_ApneaDetection;
	private Sleep_stageSeperate mSleep_stageSeperate;
	private InformationFragment mInformationFragment;
	private MonitorFragment mMonitorFragment;
	private ReportFragment mReportFragment;
	private String userName;

	// wanghanqing
	private CustomProgressDialog_Searching dialog_searching;
	private CustomProgressDialog_Connected dialog_connected;
	private CustomDialog_new dialog_abnomal;
	private CustomDialog_new dialog_no_finger;
	private CustomDialog_new dialog_reconnection;
	private CustomDialog_new dialog_change_user;
	private WaitDialog calculatingDialog;
	private Timer t_connected;

	private PublicDao pd;
	private DiabetesHy dh;
	private Epworth epw;
	private UserHistory uh;
	public static MainActivity instance = null;

//	private TextView tx_visibility;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_main);
		mSharedPreferences = this.getSharedPreferences("user", MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		instance = this;
		ExitActivity.getInstance().addActivity(this);
		userName = getIntent().getStringExtra("cname");
		cid = getIntent().getIntExtra("cid", -1);

		// 2016.7.30
		//CheckNotUploadFiles.getInstance().setUserId(cid);
//		Log.w(TAG, "mCheckNotUploadFiles = " + CheckNotUploadFiles.getInstance());

		this.initView();
		this.setListener();
		this.setCurrentPage(1);// 设置Home为启动页面

		registerReceiver(mNetworkBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		//创建实例并初始化
		BluetoothUtils.creatNewInstance();
		BluetoothUtils.getInstance().BluetoothUtilsInitial(MainActivity.this, mHandler, mMonitorFragment.mHandler, userName);
		// 打开蓝牙
		BluetoothUtils.getInstance().enable();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (BluetoothUtils.getInstance().isEnable()) {
					BluetoothUtils.getInstance().registerBroadcastReceiver(MainActivity.this);
					BluetoothUtils.getInstance().startScan(true);
				} else {
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.bluetooth_failed_to_open_and_then_try_again),
							Toast.LENGTH_LONG).show();
				}
			}
		}, 3000);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// land
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// port
		}
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
//		tx_visibility = (TextView) findViewById(R.id.tx_visibility);
//		String text = tx_visibility.getText().toString();
//		Log.d("TAG", "mainactivity - text = " + text);
	}

	/**
	 * 实例化控件,并初始化控件
	 */
	private void initView() {
		pager = (ViewPager) findViewById(R.id.pager);// 初始化viewpager控件
		pager.setOffscreenPageLimit(2);
		fragments = new ArrayList<Fragment>();// 初始化数据

		mInformationFragment = new InformationFragment();
		mMonitorFragment = new MonitorFragment();
		mReportFragment = new ReportFragment();
		fragments.add(mInformationFragment);
		fragments.add(mMonitorFragment);
		fragments.add(mReportFragment);

		this.setViewPager();

		btnTitles = new ArrayList<RadioButton>();
		initTitle();
		initSlidingMenu();
	}

	/**
	 * 初始化ViewPager
	 */
	private void setViewPager() {
		mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(mAdapter);

	}

	private void setListener() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int id) {
				btnTitles.get(id).setChecked(true);// 保持页面跟按钮的联动
				// 下面这个条件语句里面的是针对侧滑效果的
				if (id == 0) {
					// 如果当前是第一页，触摸范围为全屏
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置成全屏响应
				} else {
					// 如果不是第一页，触摸范围是边缘60px的地方
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		for (int i = 0; i < btnTitles.size(); i++) {
			btnTitles.get(i).setOnClickListener(new MyOnClickListener(i, false));
		}
		// menu.getMenu().findViewById(R.id.personal_data).setOnClickListener(new
		// MyOnClickListener(0, true));
		menu.getMenu().findViewById(R.id.personal_data).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, PersonalDataActivity.class);
				intent.putExtra("mid", cid);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		// menu.getMenu().findViewById(R.id.switch_user).setOnClickListener(new
		// MyOnClickListener(1, true));
		menu.getMenu().findViewById(R.id.switch_user).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (BluetoothUtils.getInstance() != null && BluetoothUtils.getInstance().getBtConnectionState() != BluetoothUtils.DISCONNECTED) {
					showDialog_change_user();
				} else {
					Intent intent = new Intent();
					intent.putExtra("suid", cid);
					intent.setClass(MainActivity.this, SwitchUserActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			}
		});
		menu.getMenu().findViewById(R.id.user_feedback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SuggestionActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		menu.getMenu().findViewById(R.id.about_sleep).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

	}

	/**
	 * 初始化几个用来显示title的RadioButton
	 */
	private void initTitle() {
		btnTitles.add((RadioButton) findViewById(R.id.radio_information));
		btnTitles.add((RadioButton) findViewById(R.id.radio_monitor));
		btnTitles.add((RadioButton) findViewById(R.id.radio_report));
	}

	/**
	 * 初始化开源组件SlidingMenu
	 */
	@SuppressWarnings("deprecation")
	private void initSlidingMenu() {
		menu = new SlidingMenu(this); // 实例化滑动菜单对象
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width); // 设置菜单边缘的渐变颜色宽度 （阴影效果宽度）
		menu.setShadowDrawable(R.drawable.slidingmenu_shadow); // 设置滑动阴影的图像资源
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset); // 设置滑动菜单视图的宽度
		menu.setFadeDegree(0.35f);// 边框的角度，这里指边界地方（设置渐入渐出效果的值 ）
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT); // 把侧滑栏关联到当前的Activity
		// menu.setMenu(R.layout.layout_menu);
		menu.setMode(SlidingMenu.LEFT);
		menu.setMenu(R.layout.layout_menu);// 设置当前的视图
		WindowManager wManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		long screenWidth = wManager.getDefaultDisplay().getWidth();// 获取屏幕的宽度
		menu.setBehindWidth((int) (screenWidth * 0.7));// 设置左页的宽度
	}

	/**
	 * 根据id设置切换页面和底部菜单
	 * 
	 * @param currentId
	 */
	private void setCurrentPage(int currentId) {
		pager.setCurrentItem(currentId);
		btnTitles.get(currentId).setChecked(true);
	}

	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, MainActivity.this.getString(R.string.then_click_one_exit_procedure),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				ExitActivity.getInstance().exit();
			}
		}

	}

	/**
	 * 重写OnClickListener的响应函数，主要目的就是实现点击title时，pager会跟着响应切换
	 * 
	 * @author tdx
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index;
		private boolean isMenuBtn;

		public MyOnClickListener(int index, boolean isMenubtn) {
			this.index = index;
			this.isMenuBtn = isMenubtn;
		}

		@Override
		public void onClick(View v) {
			if (isMenuBtn) {
				MainActivity.this.setCurrentPage(index);
				menu.showContent();
			} else {
				MainActivity.this.setCurrentPage(index);
			}
		}
	}

	private Runnable cancel_searching = new Runnable() {
		@Override
		public void run() {
			if (dialog_searching != null && dialog_searching.isShowing()) {
				dialog_searching.dismiss();
			}
			if (BluetoothUtils.getInstance() != null && BluetoothUtils.getInstance().isScanning()) {
				BluetoothUtils.getInstance().startScan(false);
			}
		}
	};

	/**
	 * 显示对话框，提示搜索设备
	 * 
	 * @param v
	 */
	private void showDialog_searching() {
		if (dialog_searching == null) {
			dialog_searching = new CustomProgressDialog_Searching(this,
					MainActivity.this.getString(R.string.search_equipment), R.anim.frame);
			dialog_searching.setCancelable(false);
		}
		if (!dialog_searching.isShowing()) {
			mHandler.postDelayed(cancel_searching, 45000);
			dialog_searching.show();
		} else {
			Log.d(TAG, "dialog_searching.isShowing()");
		}
	}

	/**
	 * 显示对话框，提示设备已连接
	 * 
	 * @param v
	 */
	private void showDialog_connected() {
		if (dialog_connected == null) {
			dialog_connected = new CustomProgressDialog_Connected(this);
			dialog_connected.setCancelable(false);
		}
		if (dialog_connected != null && !dialog_connected.isShowing()) {
			dialog_connected.show();
			if (t_connected == null) {
				t_connected = new Timer();
			}
			t_connected.schedule(new TimerTask() {
				@Override
				public void run() {
					if (dialog_connected != null && dialog_connected.isShowing())
						dismissDialog();
				}
			}, 2000);
		} else {
			Log.d(TAG, "dialog_connected.isShowing()");
		}
	}

	/**
	 * 显示对话框，提示测量异常
	 * 
	 * @param v
	 */
	private void showDialog_abnomal() {
		if (dialog_abnomal == null) {
			dialog_abnomal = new CustomDialog_new(MainActivity.this,
					MainActivity.this.getString(R.string.whether_thend_of_this_measurement_generation_phase_report),
					true, MainActivity.this.getString(R.string.end), new CustomDialog_new.OnCustomDialogListener() {
						@Override
						public void callback() {
							BluetoothUtils.getInstance().stop();
							BluetoothUtils.getInstance().exeCalculation(1);
						}
					}, true, MainActivity.this.getString(R.string.no), new CustomDialog_new.OnCustomDialogListener() {
						@Override
						public void callback() {
							if (BluetoothUtils.getInstance().getCalculationState() == BluetoothUtils.STATE_UNCALCULATING) {
								BluetoothUtils.getInstance().timerCountStop();
								BluetoothUtils.getInstance().exeCalculation(0);
							}
						}
					});
		}
		dialog_abnomal.show();
	}

	/**
	 * 显示对话框，提示传感器脱落
	 * 
	 * @param v
	 */
	private void showDialog_no_finger() {
		if (dialog_no_finger == null) {
			dialog_no_finger = new CustomDialog_new(MainActivity.this, MainActivity.this.getString(R.string.message),
					true, MainActivity.this.getString(R.string.delete_user_cancle), null, false, null, null);
		}
		dialog_no_finger.show();
	}

	/**
	 * 显示对话框，提示重新搜索蓝牙设备
	 * 
	 * @param v
	 */
	private void showDialog_reconnection() {
		if (dialog_reconnection == null) {
			dialog_reconnection = new CustomDialog_new(MainActivity.this,
					MainActivity.this.getString(R.string.bluetooth_device_connection_failed_after_the_re_search_device),
					true, MainActivity.this.getString(R.string.re_search),
					new CustomDialog_new.OnCustomDialogListener() {
						@Override
						public void callback() {
							if (BluetoothUtils.getInstance().isScanning()) {
								BluetoothUtils.getInstance().startScan(false);
							}
							BluetoothUtils.getInstance().reconnectionAndResetCount();
							BluetoothUtils.getInstance().startScan(true);
						}
					}, true, MainActivity.this.getString(R.string.cancel), 
					new CustomDialog_new.OnCustomDialogListener() {
						@Override
						public void callback() {
							if (BluetoothUtils.getInstance().isScanning()) {
								BluetoothUtils.getInstance().startScan(false);
							}
							BluetoothUtils.getInstance().stop();
							
							//开启自动连接
							if (getCurrentPageId() == 1) {
								mHandler.postDelayed(new Runnable() {
									@Override
									public void run() {
										BluetoothUtils.getInstance().startAutoConnect();
									}
								}, 3000);
							}
						}
					});
		}
		dialog_reconnection.show();
	}

	/**
	 * 显示对话框，提示用户切换
	 * 
	 * @param v
	 */
	private void showDialog_change_user() {
		if (dialog_change_user == null) {
			dialog_change_user = new CustomDialog_new(MainActivity.this,
					MainActivity.this.getString(R.string.currently_being_measured_sure_you_want_to_switch_users), true,
					MainActivity.this.getString(R.string.OKbtn), new CustomDialog_new.OnCustomDialogListener() {
						@Override
						public void callback() {
							// 关闭蓝牙
//							BluetoothUtils.getInstance().killDataThread(false);
							BluetoothUtils.getInstance().stop();

							Intent intent = new Intent();
							intent.putExtra("suid", cid);
							intent.putExtra("wait", true);
							intent.setClass(MainActivity.this, SwitchUserActivity.class);
							MainActivity.this.startActivity(intent);
							overridePendingTransition(R.anim.push_left_inin, R.anim.push_left_outout);
						}
					}, true, MainActivity.this.getString(R.string.delete_user_cancle), null);
		}
		dialog_change_user.show();
	}

	/**
	 * 移除所有正在显示的dialog
	 */
	private void dismissDialog() {
		if (dialog_searching != null && dialog_searching.isShowing()) {
			dialog_searching.dismiss();
		}
		if (dialog_connected != null && dialog_connected.isShowing()) {
			dialog_connected.dismiss();
		}
		if (dialog_abnomal != null) {
			dialog_abnomal.dismiss();
		}
		if (dialog_no_finger != null) {
			dialog_no_finger.dismiss();
		}
		if (dialog_reconnection != null) {
			dialog_reconnection.dismiss();
		}
		if (dialog_change_user != null) {
			dialog_change_user.dismiss();
		}
	}

	/**
	 * 移除所有正在显示的dialog
	 */
	private void dismissDialogByInsertFinger() {
		if (dialog_searching != null && dialog_searching.isShowing()) {
			dialog_searching.dismiss();
		}
		if (dialog_connected != null && dialog_connected.isShowing()) {
			dialog_connected.dismiss();
		}
		if (dialog_abnomal != null) {
			dialog_abnomal.dismiss();
		}
		if (dialog_no_finger != null) {
			dialog_no_finger.dismiss();
		}
	}

	// wanghanqing
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Utils.DELIVER_RESULTS:
				@SuppressWarnings("unchecked")
				final HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				if (map != null) {
					mSleep_ApneaDetection = (Sleep_ApneaDetection) map.get("mSleep_ApneaDetection");
					mSleep_stageSeperate = (Sleep_stageSeperate) map.get("mSleep_stageSeperate");
					int isFinalReport = (Integer) map.get("isFinalReport");
					boolean isAutoCalculate = (Boolean) map.get("isAutoCalculate");

					if (mSleep_ApneaDetection != null && mSleep_stageSeperate != null) {
						pd = new PublicDaoImpl(MainActivity.this);
						dh = pd.findAllDiabetesHyById(cid);
						epw = pd.findAllEpworthById(cid);

						// 2016.7.25
						uh = pd.findMaxHistoryIdByUserId(cid);
						if(uh == null){
							Log.e(TAG, "mUserHistory表的初始化有问题！！！");
							break;
						}
						
						/**
						 * 睡眠
						 */
						// 起始时间
						uh.setHstarthour(mSleep_stageSeperate.getTime_start()[0]);
						uh.setHstartminute(mSleep_stageSeperate.getTime_start()[1]);
						uh.setHstarttimes((int) (mSleep_stageSeperate.getTime_start2() / 1000));
						// Log.d("TAG", "-------------------" + (int)
						// (mSleep_stageSeperate.getTime_start2() / 1000));
						// 结束时间
						uh.setHendhour(mSleep_stageSeperate.getTime_end()[0]);
						uh.setHendminute(mSleep_stageSeperate.getTime_end()[1]);
						uh.setHendtimes((int) (mSleep_stageSeperate.getTime_end2() / 1000));
						// Log.d("TAG", "-------------------" + (int)
						// (mSleep_stageSeperate.getTime_end2() / 1000));

						// 持续时间
						uh.setHsmschour(mSleep_stageSeperate.getTime_last()[0]);
						uh.setHsmscminute(mSleep_stageSeperate.getTime_last()[1]);
						uh.setHsmsctimes((int) (mSleep_stageSeperate.getTime_last2() / 1000));

						// 浅睡
						uh.setHqshour(mSleep_stageSeperate.getTime_light()[0]);
						uh.setHqsminute(mSleep_stageSeperate.getTime_light()[1]);
						uh.setHqssctimes((int) (mSleep_stageSeperate.getTime_light2() / 1000));

						// 深睡
						uh.setHsshour(mSleep_stageSeperate.getTime_deep()[0]);
						uh.setHssminute(mSleep_stageSeperate.getTime_deep()[1]);
						uh.setHsssctimes((int) (mSleep_stageSeperate.getTime_deep2() / 1000));

						// 清醒
						uh.setHqxhour(mSleep_stageSeperate.getTime_wake()[0]);
						uh.setHqxminute(mSleep_stageSeperate.getTime_wake()[1]);
						uh.setHqxsctimes((int) (mSleep_stageSeperate.getTime_wake2() / 1000));

						/** 脉率 */
						// 最高脉率
						uh.setHmaxml(String.valueOf(mSleep_stageSeperate.getPr_max()));
						// 发生于 时
						uh.setHmaxmlhour(mSleep_stageSeperate.getPr_max_timePoint()[0]);
						// 发生于 分
						uh.setHmaxmlminute(mSleep_stageSeperate.getPr_max_timePoint()[1]);
						// 发生于 秒
						uh.setHmaxmlsec(mSleep_stageSeperate.getPr_max_timePoint()[2]);
						uh.setHmaxmltimes((int) (mSleep_stageSeperate.getPr_max_timePoint2() / 1000));
						// 最低脉率
						uh.setHminml(String.valueOf(mSleep_stageSeperate.getPr_min()));
						// 发生于 时
						uh.setHminmlhour(mSleep_stageSeperate.getPr_min_timePoint()[0]);
						// 发生于 分
						uh.setHminmlminute(mSleep_stageSeperate.getPr_min_timePoint()[1]);
						// 发生于 秒
						uh.setHminmlsec(mSleep_stageSeperate.getPr_min_timePoint()[2]);
						uh.setHminmltimes((int) (mSleep_stageSeperate.getPr_min_timePoint2() / 1000));
						// 平均脉率
						uh.setHavgml(String.valueOf(Utils.formFloat(mSleep_stageSeperate.getPr_average(), 2)));

						// 睡眠评分
						int qs = mSleep_stageSeperate.getTime_light()[0] * 60 + mSleep_stageSeperate.getTime_light()[1];
						int ss = mSleep_stageSeperate.getTime_deep()[0] * 60 + mSleep_stageSeperate.getTime_deep()[1];
						int qx = mSleep_stageSeperate.getTime_wake()[0] * 60 + mSleep_stageSeperate.getTime_wake()[1];
						int ss_rate = ss * 100 / (qs + ss + qx);
						if (ss_rate > 35) {
							uh.setHsleepscore(95 + (int) (Math.random() * 5));
						} else if (ss_rate > 25) {
							uh.setHsleepscore(90 + (int) (Math.random() * 5));
						} else if (ss_rate > 20) {
							uh.setHsleepscore(85 + (int) (Math.random() * 5));
						} else if (ss_rate > 15) {
							uh.setHsleepscore(80 + (int) (Math.random() * 5));
						} else if (ss_rate > 10) {
							uh.setHsleepscore(70 + (int) (Math.random() * 10));
						} else if (ss_rate > 5) {
							uh.setHsleepscore(60 + (int) (Math.random() * 10));
						} else {
							uh.setHsleepscore(50 + (int) (Math.random() * 10));
						}
						/**
						 * 指标
						 */
						// AHI
						uh.setHahiIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getAHI(), 2)));
						// 呼吸暂停次数
						uh.setHhxztIndex(String.valueOf(mSleep_ApneaDetection.getAI_count()));
						// 低通气次数
						uh.setHdtqIndex(String.valueOf(mSleep_ApneaDetection.getHI_count()));
						// 氧减次数
						uh.setHyjzcsIndex(String.valueOf(mSleep_ApneaDetection.getODI4_count()));
						// 平均血氧饱和度
						uh.setHpjxybhdIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getMSPO2(), 2)));
						// 最低血氧饱和度
						uh.setHzdxybhdIndex(String.valueOf(mSleep_ApneaDetection.getLSPO2()));
						// 血氧低于90%占比
						uh.setHxybhdzbIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getTS90(), 2)));
						/** 氧降 */
						// 最大氧降 持续时
						uh.setHmaxyjcxhour(mSleep_ApneaDetection.getODI4_largest_duration()[0]);
						// 最大氧降 持续分
						uh.setHmaxyjcxminute(mSleep_ApneaDetection.getODI4_largest_duration()[1]);
						// 最大氧降 持续秒
						uh.setHmaxyjcxsec(mSleep_ApneaDetection.getODI4_largest_duration()[2]);
						uh.setHmaxyjcxhour((int) (mSleep_ApneaDetection.getODI4_largest_duration2() / 1000));
						// 最大氧降 发生时
						uh.setHmaxyjfshour(mSleep_ApneaDetection.getODI4_largest_timePoint()[0]);
						// 最大氧降 发生分
						uh.setHmaxyjfsminute(mSleep_ApneaDetection.getODI4_largest_timePoint()[1]);
						// 最大氧降 发生秒
						uh.setHmaxyjfssec(mSleep_ApneaDetection.getODI4_largest_timePoint()[2]);
						uh.setHmaxyjfstimes((int) (mSleep_ApneaDetection.getODI4_largest_timePoint2() / 1000));
						// 最长氧降 持续时
						uh.setHlongyjcxhour(mSleep_ApneaDetection.getODI4_longest_duration()[0]);
						// 最长氧降 持续分
						uh.setHlongyjcxminute(mSleep_ApneaDetection.getODI4_longest_duration()[1]);
						// 最长氧降 持续秒
						uh.setHlongyjcxsec(mSleep_ApneaDetection.getODI4_longest_duration()[2]);
						uh.setHlongyjcxtimes((int) (mSleep_ApneaDetection.getODI4_longest_duration2() / 1000));
						// 最长氧降 发生时
						uh.setHlongyjfshour(mSleep_ApneaDetection.getODI4_longest_timePoint()[0]);
						// 最长氧降 发生分
						uh.setHlongyjfsminute(mSleep_ApneaDetection.getODI4_longest_timePoint()[1]);
						// 最长氧降 发生秒
						uh.setHlongyjfssec(mSleep_ApneaDetection.getODI4_longest_timePoint()[2]);
						uh.setHlongyjfstimes((int) (mSleep_ApneaDetection.getODI4_longest_timePoint2() / 1000));
						// 氧减危害指数
						uh.setHxywhzsIndex(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getODHI(), 2)));
						// 平均血流灌注度
						uh.setHavgxlgzd(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getPI(), 2)));
						// 评分
						int score;
						if (mSleep_ApneaDetection.getAHI() % 1 < 0.5) {
							score = 100 - (int) mSleep_ApneaDetection.getAHI();
						} else {
							score = 99 - (int) mSleep_ApneaDetection.getAHI();
						}
						if (score < 0)
							score = 0;
						uh.setHscoreHxzb(score);
						// 最长呼吸暂停时长
						uh.setHzchxzthour(mSleep_ApneaDetection.getAI_longest_duration()[0]);
						uh.setHzchxztminute(mSleep_ApneaDetection.getAI_longest_duration()[1]);
						uh.setHzchxztsec(mSleep_ApneaDetection.getAI_longest_duration()[2]);
						uh.setHzchxzttimes((int) (mSleep_ApneaDetection.getAI_longest_duration2() / 1000));
						// 发生于
						uh.setHhxhapperhour(mSleep_ApneaDetection.getAI_longest_timePoint()[0]);
						uh.setHhxhapperminute(mSleep_ApneaDetection.getAI_longest_timePoint()[1]);
						uh.setHhxhappersec(mSleep_ApneaDetection.getAI_longest_timePoint()[2]);
						uh.setHhxhappertimes((int) (mSleep_ApneaDetection.getAI_longest_timePoint2() / 1000));
						// 总时间
						uh.setHhxztzsjhour(mSleep_ApneaDetection.getAI_time_sum()[0]);
						uh.setHhxztzsjminute(mSleep_ApneaDetection.getAI_time_sum()[1]);
						uh.setHhxztzsjsec(mSleep_ApneaDetection.getAI_time_sum()[2]);
						uh.setHhxztzsjtimes((int) (mSleep_ApneaDetection.getAI_time_sum2() / 1000));
						// 最长低通气时长
						uh.setHzcdtqhour(mSleep_ApneaDetection.getHI_longest_duration()[0]);
						uh.setHzcdtqminute(mSleep_ApneaDetection.getHI_longest_duration()[1]);
						uh.setHzcdtqsec(mSleep_ApneaDetection.getHI_longest_duration()[2]);
						uh.setHzcdtqtimes((int) (mSleep_ApneaDetection.getHI_longest_duration2() / 1000));
						// 发生于
						uh.setHdtqhapperhour(mSleep_ApneaDetection.getHI_longest_timePoint()[0]);
						uh.setHdtqhapperminute(mSleep_ApneaDetection.getHI_longest_timePoint()[1]);
						uh.setHdtqhappersec(mSleep_ApneaDetection.getHI_longest_timePoint()[2]);
						uh.setHdtqhappertimes((int) (mSleep_ApneaDetection.getHI_longest_timePoint2() / 1000));
						// 总时间
						uh.setHdtqzsjhour(mSleep_ApneaDetection.getHI_time_sum()[0]);
						uh.setHdtqzsjminute(mSleep_ApneaDetection.getHI_time_sum()[1]);
						uh.setHdtqzsjsec(mSleep_ApneaDetection.getHI_time_sum()[2]);
						uh.setHdtqzsjtimes((int) (mSleep_ApneaDetection.getHI_time_sum2() / 1000));
						// ODI
						uh.setHxyodi(String.valueOf(Utils.formFloat(mSleep_ApneaDetection.getODI4_index(), 2)));
						// 醒时
						// 最高
						uh.setHsmmax(String.valueOf(mSleep_ApneaDetection.getHSPO2()));

						// 90-100总时间
						uh.setHaxyfbzsjhour(mSleep_ApneaDetection.getDistribution_90_100()[0]);
						uh.setHaxyfbzsjminute(mSleep_ApneaDetection.getDistribution_90_100()[1]);
						uh.setHaxyfbzsjsec(mSleep_ApneaDetection.getDistribution_90_100()[2]);
						uh.setHaxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_90_1002()[0] / 1000);
						// 90-100氧减总次数
						uh.setHayjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_90_100()[3]));
						// 80-90
						uh.setHbxyfbzsjhour(mSleep_ApneaDetection.getDistribution_80_90()[0]);
						uh.setHbxyfbzsjminute(mSleep_ApneaDetection.getDistribution_80_90()[1]);
						uh.setHbxyfbzsjsec(mSleep_ApneaDetection.getDistribution_80_90()[2]);
						uh.setHbxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_80_902()[0] / 1000);
						uh.setHbyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_80_90()[3]));
						// 70-80
						uh.setHcxyfbzsjhour(mSleep_ApneaDetection.getDistribution_70_80()[0]);
						uh.setHcxyfbzsjminute(mSleep_ApneaDetection.getDistribution_70_80()[1]);
						uh.setHcxyfbzsjsec(mSleep_ApneaDetection.getDistribution_70_80()[2]);
						uh.setHcxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_70_802()[0] / 1000);
						uh.setHcyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_70_80()[3]));
						// 60-70
						uh.setHdxyfbzsjhour(mSleep_ApneaDetection.getDistribution_60_70()[0]);
						uh.setHdxyfbzsjminute(mSleep_ApneaDetection.getDistribution_60_70()[1]);
						uh.setHdxyfbzsjsec(mSleep_ApneaDetection.getDistribution_60_70()[2]);
						uh.setHdxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_60_702()[0] / 1000);
						uh.setHdyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_60_70()[3]));
						// 50-60
						uh.setHexyfbzsjhour(mSleep_ApneaDetection.getDistribution_50_60()[0]);
						uh.setHexyfbzsjminute(mSleep_ApneaDetection.getDistribution_50_60()[1]);
						uh.setHexyfbzsjsec(mSleep_ApneaDetection.getDistribution_50_60()[2]);
						uh.setHexyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_50_602()[0] / 1000);
						uh.setHeyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_50_60()[3]));
						// 0-50
						uh.setHfxyfbzsjhour(mSleep_ApneaDetection.getDistribution_0_50()[0]);
						uh.setHfxyfbzsjminute(mSleep_ApneaDetection.getDistribution_0_50()[1]);
						uh.setHfxyfbzsjsec(mSleep_ApneaDetection.getDistribution_0_50()[2]);
						uh.setHfxyfbzsjtimes((int) mSleep_ApneaDetection.getDistribution_0_502()[0] / 1000);
						uh.setHfyjzcsnum(String.valueOf(mSleep_ApneaDetection.getDistribution_0_50()[3]));

						int essscore = epw.getSumscore();
						float AHI = mSleep_ApneaDetection.getAHI();
						int lspo2 = mSleep_ApneaDetection.getLSPO2();
						boolean flag_osahs = false;
						if (MainActivity.this.getString(R.string.insomnia).equals(dh.getLosesleep()) || MainActivity.this.getString(R.string.hypertension).equals(dh.getHypertension())
								|| MainActivity.this.getString(R.string.coronary_heart_disease).equals(dh.getCoronaryheartdisease())
								|| MainActivity.this.getString(R.string.cerebral_vascular_disease).equals(dh.getCerebrovasculardisease()) || MainActivity.this.getString(R.string.diabetes).equals(dh.getDiabetes())) {
							flag_osahs = true;
						}
						// 判断是否患OSAHS
						if ((essscore >= 9 && AHI > 5) || (essscore < 9 && AHI >= 10) || (AHI > 5 && flag_osahs)) {
							uh.setHudegree(MainActivity.this.getString(R.string.osahs_text));
						} else {
							uh.setHudegree(null);
						}
						// OSAHS
						if (MainActivity.this.getString(R.string.osahs_text).equals(uh.getHudegree())) {
							if (AHI <= 5 && lspo2 >= 90)
								uh.setHudegree(MainActivity.this.getString(R.string.no_input));
							if (AHI > 30) {
								uh.setHudegree(MainActivity.this.getString(R.string.severe_osahs));
							} else if (AHI > 15) {
								uh.setHudegree(MainActivity.this.getString(R.string.moderate_osahs));
							} else if (AHI > 5) {
								uh.setHudegree(MainActivity.this.getString(R.string.light_osahs));
							}
						}
						if (uh.getHudegree() != null) {
							if (lspo2 < 80) {
								uh.setHudegree(uh.getHudegree() + MainActivity.this.getString(R.string.and_severe_hypoxia));
							} else if (lspo2 < 85) {
								uh.setHudegree(uh.getHudegree() + MainActivity.this.getString(R.string.and_moderate_hypoxia));
							} else if (lspo2 < 90) {
								uh.setHudegree(uh.getHudegree() + MainActivity.this.getString(R.string.and_mild_hypoxia));
							}
						} else {
							if (lspo2 < 80) {
								uh.setHudegree(MainActivity.this.getString(R.string.severe_hypoxia));
							} else if (lspo2 < 85) {
								uh.setHudegree(MainActivity.this.getString(R.string.moderate_hypoxia));
							} else if (lspo2 < 90) {
								uh.setHudegree(MainActivity.this.getString(R.string.mild_hypoxia));
							}
						}
						// OSAHS程度
						if (AHI > 30) {
							uh.setHosahsdegree(MainActivity.this.getString(R.string.severe));
						} else if (AHI > 15) {
							uh.setHosahsdegree(MainActivity.this.getString(R.string.moderate));
						} else if (AHI > 5) {
							uh.setHosahsdegree(MainActivity.this.getString(R.string.light));
						} else {
							uh.setHosahsdegree(MainActivity.this.getString(R.string.no_input));
						}
						// 低血氧症程度
						if (lspo2 < 80) {
							uh.setHdyxzdegree(MainActivity.this.getString(R.string.severe));
						} else if (lspo2 < 85) {
							uh.setHdyxzdegree(MainActivity.this.getString(R.string.moderate));
						} else if (lspo2 < 90) {
							uh.setHdyxzdegree(MainActivity.this.getString(R.string.light));
						} else {
							uh.setHdyxzdegree(MainActivity.this.getString(R.string.no_input));
						}

						//！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
						
						//为监测记录页面提供的历史数据表
						/**
						 * 历史记录
						 */
						
						// 监测记录的开始时间
						uh.setTestdate(uh.getHstarttimes());

						String filepath = uh.getFilepaths();
						Log.d(TAG, "ui.getFilepaths(), filepath = " + filepath);
						try {
							GZipUtils.compress(filepath, false);
						} catch (Exception e) {
							e.printStackTrace();
						}
						final String fileurl = filepath.replace(".dat", ".taiir");

						// 保存数据文件的路径
						uh.setFilepaths(fileurl);

						String reportfilepath = uh.getReportfilepath();
						if (reportfilepath != null) {
							File pdffile = new File(reportfilepath);
							if (pdffile != null && pdffile.exists()) {
								pdffile.delete();
								Log.d("TAG", "PDF文件删除成功");
							}
						}
						uh.setReportfilepath(null);

						uh.setHistoryupload(0);
						uh.setUploadfile(0);
						uh.setIsFinalReport(isFinalReport);
						uh.setIsComputed(1);

						pd.modifyHistorys(uh);

						pdDel = new PublicDaoImpl(MainActivity.this);
						List<UserHistory> uhList = pdDel.findAllHistoryByUserId(cid);
						if (uhList.size() > 7) {
							new Thread(){
								@Override
								public void run() {
									new DeleteFileAndUserHistory().deleteByExceedLimitation(MainActivity.this, cid);
								}
							}.start();
						}

						boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
						boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());

						if (isConnected || isConnected3g) {
							new Thread() {
								public void run() {
									// 2016.7.30
									//需要一个查询当前用户下未完成上传的所有历史记录的数据库查询方法
//									CheckNotUploadFiles.getInstance().ExecutionThread(getApplicationContext(), new PublicDaoImpl(getApplicationContext()).findUnUploadUserHistoryByUserId(cid));
//									
//									mReportFragment.mHandler.sendEmptyMessageDelayed(Utils.DEAL_RESULTS, 5000);
								};
							}.start();
						} else {
							mReportFragment.mHandler.sendEmptyMessageDelayed(Utils.DEAL_RESULTS, 3000);
							Toast.makeText(MainActivity.this,
									MainActivity.this.getString(R.string.report_upload_failed_please_open_network),
									Toast.LENGTH_LONG).show();
						}
						
						BluetoothUtils.getInstance().setCalculationState(BluetoothUtils.STATE_UNCALCULATING);
						mHandler.obtainMessage(Utils.TOAST_COMPUTATION, Utils.COMPUTATION_FINISH, -1).sendToTarget(); // 弹出toast，计算结束
						if (!isAutoCalculate) {
							mHandler.obtainMessage(Utils.TOAST_COMPUTATION, Utils.COMPUTATION_OVER, -1).sendToTarget();
							dismissDialog();
							MainActivity.this.setCurrentPage(2); // 跳转到ReportFragment界面
						}

					} else {
						Log.w(TAG, "计算结果为空！");
						BluetoothUtils.getInstance().setCalculationState(BluetoothUtils.STATE_UNCALCULATING);
						if (!isAutoCalculate) {
							mHandler.obtainMessage(Utils.TOAST_COMPUTATION, Utils.COMPUTATION_OVER, -1).sendToTarget();
							dismissDialog();
						}
					}
				}
				break;
			case Utils.TOAST_BLUETOOTH:
				switch (msg.arg1) {
				case Utils.BT_CONNECTING:
					Log.d(TAG, "蓝牙设备正在连接中！");
					dismissDialog();
					if (calculatingDialog != null) {
						calculatingDialog.cancel();
					}
					// 开始搜索蓝牙
					showDialog_searching(); // 显示动画
					break;
				case Utils.BT_CONNECT_SUCCESS:
					Log.d(TAG, "蓝牙设备连接成功！");
					dismissDialog();
					if (dialog_searching != null) {
						mHandler.removeCallbacks(cancel_searching);
						dialog_searching.dismiss();
						showDialog_connected();
					}
					break;
				case Utils.BT_CONNECT_FAILED:
					Log.d(TAG, "蓝牙设备连接失败，请确认蓝牙设备已开启！");
					dismissDialog();
					// socket连接失败
					if (dialog_searching != null) {
						Log.d(TAG, "弹出重连窗口2");
						mHandler.removeCallbacks(cancel_searching);
						dialog_searching.dismiss();
						showDialog_reconnection();
					}
					break;
				}
				break;
			case Utils.TOAST_COMPUTATION:
				switch (msg.arg1) {
				case Utils.COMPUTATION_TOO_SHORT:
					Log.d(TAG, "测量时间太短！");
					// 测量时间少于3分钟
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.measurement_time_is_too_short), Toast.LENGTH_SHORT)
							.show();
					break;
				case Utils.COMPUTATION_FINISH:
					Log.d(TAG, "数据处理完毕！");
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.data_processing_is_completed), Toast.LENGTH_SHORT)
							.show();
					break;
				case Utils.COMPUTATION_DUE_TO_NORMAL:
					Log.d(TAG, "测量结束，参数计算中！");
					break;
				case Utils.COMPUTATION_DUE_TO_ERROR:
					Log.d(TAG, "设备或蓝牙异常，已终止测量！");
					break;
				case Utils.COMPUTATION_RUNNING:
					Log.d(TAG, "执行计算中，请稍后...");
					dismissDialog();
					if (calculatingDialog == null) {
						calculatingDialog = new WaitDialog(MainActivity.this);
					}
					calculatingDialog.setContent(MainActivity.this.getString(R.string.to_perform_calculations_please));
					calculatingDialog.show();
					break;
				case Utils.COMPUTATION_OVER:
					Log.d(TAG, "计算结束！");
					if (calculatingDialog != null) {
						calculatingDialog.cancel();
					}
					break;
				}
				break;
			case Utils.TOAST_MEASURE_REEOR:
				switch (msg.arg1) {
				case Utils.ERROR_ABNORMAL:
					Log.d(TAG, "测量异常，没有测量数据！");
					dismissDialog();
					showDialog_abnomal();
					Toast.makeText(MainActivity.this,
							MainActivity.this.getString(R.string.measurement_exception_no_measurement_data),
							Toast.LENGTH_SHORT).show();
					// 重置界面上显示的数值
					mMonitorFragment.mHandler.sendEmptyMessage(Utils.RESET_DATA);
					break;
				case Utils.ERROR_NO_FINGER:
					Log.d(TAG, "没有测量数据，请检查指端传感器是否脱落！");
					dismissDialog();
					showDialog_no_finger();
					Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.message), Toast.LENGTH_SHORT).show();
					// 重置界面上显示的数值
					mMonitorFragment.mHandler.sendEmptyMessage(Utils.RESET_DATA);
					break;
				}
				break;
			case Utils.SAVE_TO_SHAREDPREFERENCES:
				pd = new PublicDaoImpl(MainActivity.this);
				switch (msg.arg1) {
				case Utils.SAVE_BLUETOOTH_MAC:
					uh = pd.findMaxHistoryIdByUserId(cid);
					//************$$$$$$$$$##########%%%%%%%%%%%%%%
					break;
				// 2016.7.25
				case Utils.SAVE_BLUETOOTH_MAC_AND_FILE_PATH:
					Log.d(TAG, "MainActivity.mHandler, Utils.SAVE_BLUETOOTH_MAC_AND_FILE_PATH");
					uh = new UserHistory();
					@SuppressWarnings("unchecked")
					HashMap<String, Object> map_for_mac_path = (HashMap<String, Object>) msg.obj;
					String mFilePath = (String) map_for_mac_path.get("mFilePath");
					String mMacAddress = (String) map_for_mac_path.get("mMacAddress");
					Log.d(TAG, "mFilePath = " + mFilePath + ", mMacAddress = " + mMacAddress);
					mEditor.putString("bluetooth_mac", mMacAddress).commit();
					// 保存报告的UUID
					final String uuid = UUID.randomUUID().toString();
					uh.setReportuuid(uuid);
					uh.setUserid(cid);
					uh.setFilepaths(mFilePath);
					uh.setMacaddress(mMacAddress);
					uh.setIsComputed(0);
					pd.addHistorys(uh);
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(Utils.SAVE_TO_SHAREDPREFERENCES, Utils.SAVE_BLUETOOTH_MAC, -1),
							5000);
					break;
				}
				break;
			case Utils.ISCALCULATED:
				pd = new PublicDaoImpl(MainActivity.this);
				uh = pd.findMaxHistoryIdByUserId(cid);
				switch (msg.arg1) {
				case Utils.ISCALCULATED_FALSE:
					Log.d(TAG, "MainActivity.mHandler, Utils.ISCALCULATED_FALSE");
					if (uh != null) {
						uh.setIsComputed(0);
						pd.modifyHistorys(uh);
					} else {
						mHandler.sendMessageDelayed(
								mHandler.obtainMessage(Utils.ISCALCULATED, Utils.ISCALCULATED_FALSE, -1), 500);
					}
					break;
				case Utils.ISCALCULATED_TRUE:
					Log.d(TAG, "MainActivity.mHandler, Utils.ISCALCULATED_TRUE");
					break;
				case Utils.ISCALCULATED_COMPUTATION:
					Log.d(TAG, "MainActivity.mHandler, Utils.ISCALCULATED_COMPUTATION");
					break;
				}
				break;
			case Utils.DISMISS_DIALOG:
				switch (msg.arg1) {
				case Utils.DISMISS_ALL_DIALOG:
					Log.d(TAG, "MainActivity.mHandler, Utils.DISMISS_ALL_DIALOG");
					dismissDialog();
					break;
				case Utils.DISMISS_DIALOG_BY_INSERT_FINGER:
					Log.d(TAG, "MainActivity.mHandler, Utils.DISMISS_DIALOG_BY_INSERT_FINGER");
					dismissDialogByInsertFinger();
					break;
				}
				break;
			// 2016.7.30
			case Utils.DELETE_FILE:
				switch (msg.arg1) {
				case Utils.DELETE_FILE_ONLY:
					Log.d(TAG, "Utils.DELETE_FILE_ONLY");
					final String filepath = (String) msg.obj;
					new Thread(){
						@Override
						public void run() {
							//new DeleteFileAndUserHistory().deleteByUnconnect(filepath);
						}
					}.start();
					break;
				case Utils.DELETE_FILE_DATA_INSUFFICIENT:
					Log.d(TAG, "Utils.DELETE_FILE_DATA_INSUFFICIENT");
					final int historyID = msg.arg2;
					final String filepath2 = (String) msg.obj;
					new Thread(){
						@Override
						public void run() {
							new DeleteFileAndUserHistory().deleteByInsufficientData(MainActivity.this, cid, historyID, filepath2);
						}
					}.start();
					break;
				}
				break;
			case Utils.NO_DATA_TIMEOUT:
				Log.d(TAG, "Utils.NO_DATA_TIMEOUT");
				dismissDialog();
				if (msg.arg1 == 1) {
					MainActivity.this.setCurrentPage(2); // 跳转到ReportFragment界面
				}
				break;
			case Utils.FINAL_REPORT:
				Log.d(TAG, "Utils.FINAL_REPORT");
				pd = new PublicDaoImpl(MainActivity.this);
				uh = pd.findMaxHistoryIdByUserId(cid);
				uh.setIsFinalReport(1);
				pd.modifyHistorys(uh);
				break;
			}
		}
	};

	// 判断网络开启，进行报告的上传
	private BroadcastReceiver mNetworkBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				boolean isConnected = NetworkProber.isWifi(context);
				boolean isConnected3g = NetworkProber.is3G(context);

				if (isConnected || isConnected3g) {
					Log.d(TAG, "网络已打开，开始上传！");
					new Thread() {
						public void run() {
							// 2016.7.30
							//需要一个查询当前用户下未完成上传的所有历史记录的数据库查询方法
							//如果正在测量中，传入的UserHistory列表需要去掉正在测量的那张表，否则这张表会被删掉，引发error
							List<UserHistory> list_UserHistory = new PublicDaoImpl(getApplicationContext()).findUnUploadUserHistoryByUserId(cid);
							if (list_UserHistory.size() > 0) {
								if(BluetoothUtils.getInstance() != null && BluetoothUtils.getInstance().getBtConnectionState() != BluetoothUtils.DISCONNECTED){
									Log.d(TAG, "由于正在测量，最新的历史记录不进行上传！");
									list_UserHistory.remove(list_UserHistory.size() - 1);
								}
								//CheckNotUploadFiles.getInstance().ExecutionThread(getApplicationContext(), list_UserHistory);
							}
							
							mReportFragment.mHandler.sendEmptyMessageDelayed(Utils.NETWORKBROADCAST, 3000);
						};
					}.start();
				} else {
					Log.d(TAG, "网络已关闭！");
				}
			}
		}
	};

	// wanghanqing
	@SuppressLint("NewApi")
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		// 关闭蓝牙
		BluetoothUtils.getInstance().killDataThread(false);
		unregisterReceiver(mNetworkBroadcastReceiver);
		dismissDialog();
		if (MainActivity.this.isDestroyed()) {
			// avoid HandlerLeak
			if (mHandler != null)
				mHandler.removeCallbacksAndMessages(null);
		}
	}

	public String getMacAddress() {
		return mSharedPreferences.getString("bluetooth_mac", null);
	}

	public int getCurrentUserHistoryId() {
		PublicDao mPublicDao = new PublicDaoImpl(MainActivity.this);
		UserHistory mUserHistory = mPublicDao.findMaxHistoryIdByUserId(cid);
		return mUserHistory != null ? mUserHistory.getId() : 0;
	}
	
	public int getCurrentPageId(){
		return pager.getCurrentItem();
	}
}
