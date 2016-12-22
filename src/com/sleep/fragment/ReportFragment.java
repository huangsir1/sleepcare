package com.sleep.fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.application.MyApplication;
import com.sleep.activity.HistoryActivity;
import com.sleep.activity.MainActivity;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.dialog.AHIdialog;
import com.sleep.dialog.DTQdialog;
import com.sleep.dialog.HXZTdialog;
import com.sleep.dialog.ODIdialog;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserManager;
import com.sleep.overriding_methods.MyPercentFormatter;
import com.sleep.overriding_methods.OperationFileHelper;
import com.sleep.overriding_methods.ScreenShot;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.NetworkProber;
import com.sleep.utils.Utils;
import com.taiir.sleepcare.home.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportFragment extends Fragment {
	private View view;
	private PieChart mChart;
	private int[] time_pie = { 10, 10, 10 };
	private String sleep[] = { MyApplication.getContext().getString(R.string.deep_sleep),
			MyApplication.getContext().getString(R.string.light_sleep),
			MyApplication.getContext().getString(R.string.sober) };

	// 测试结论 osahs 低氧血症
	private TextView osahstv, dyxztv;
	// 呼吸指标 ahi 呼吸暂停次数 低通气次数
	private TextView ahitv, hxztcstv, dtqcstv;
	// 血氧饱和度低于90%占比 平均血氧饱和度 最低血氧饱和度
	private TextView dyzbtv, pjxybhdtv, zdxybhdtv;
	// 氧减总次数
	private TextView yjzcstv;
	// 氧减危害指数
	private TextView yjwhzstv;
	// 最高脉率
	private TextView zgmltv;
	// 最低脉率
	private TextView zdmltv;
	// 平均脉率
	private TextView pjmltv;
	// 平均血流灌注度
	private TextView pjxlgzdtv;
	/** 呼吸质量评分 */
	private TextView hxzlpftv;
	private AlertDialog dialog;

	// 分享
	private ImageView sharedateiv;
	private ScrollView scrollview_view;
	private String fname;
	private Uri imageUri;
	// 发送电子邮件
	private ImageView sendemail;
	private TextView rp_title;
	private RelativeLayout relativeLayout;
	private Dialog progressDialog;
	private String pdfpath;
	/**
	 * 睡眠时间
	 */
	// 开始时 开始分
	private TextView starthtv, startmtv;
	// 结束时 结束分
	private TextView endhtv, endmtv;
	// 睡眠时长 //时 分
	private TextView smschtv, smscmtv;
	// 深睡期时长 //时 分
	private TextView ssqschtv, ssqscmtv;
	// 浅睡期时长 //时 分
	private TextView qsqschtv, qsqscmtv;
	// 清醒期时长 //时 分
	private TextView qxqschtv, qxqscmtv;
	/** 睡眠质量评分 */
	private TextView smzlpftv;

	private PublicDao pd;
	private AHIdialog ahIdialog;
	private ODIdialog odIdialog;
	private HXZTdialog hxzTdialog;
	private DTQdialog dtQdialog;

	private SlidingMenu menu;
	private ImageView slidingiv;
	private ImageView ivrep_frport;
	private int rfid;

	/** 名词解释 */
	private TextView hxztdtqzs_frport;
	private TextView hxztcs_frport;
	private TextView dtqcs_frport;
	private TextView yjzcs_freport;
	private TextView pjxybhd_frport;
	private TextView zdxybhd_frport;
	private TextView xybhddy_frport;
	private TextView yjwhzs_freport;

	private PopupWindow window;
	private View conView1, conView2, conView3, conView4, conView5, conView6, conView7, conView8;
	private Button closepw1, closepw2, closepw3, closepw4, closepw5, closepw6, closepw7, closepw8;
	public SharedPreferences mSharedPreferences;
	private String urlpath = "http://121.42.204.40/web/api/getPdf/";

	// private boolean isConnected;
	// private boolean isConnected3g;

	private UserHistory uhInfo = null;
	// private IntentFilter mfilter = new IntentFilter();
	private UserHistory last_uh;
	private final int startBy_onResume = 0;
	private final int startBy_calculated = 1;
	private final int startBy_broadcast = 2;

	private UserManager um;
	private DiabetesHy dh;
	private Epworth ep;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Utils.TAG, "ReportFragment, onCreate()");
	}

	@SuppressWarnings("static-access")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(Utils.TAG, "ReportFragment, onCreateView()");

		// Intent intent = getActivity().getIntent();
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_report, container, false);
			menu = MainActivity.instance.menu;
			rfid = getActivity().getIntent().getIntExtra("cid", -1);

			pd = new PublicDaoImpl(getActivity());
			um = pd.findAllUserById(rfid);
			ep = pd.findAllEpworthById(rfid);
			dh = pd.findAllDiabetesHyById(rfid);

			slidingiv = (ImageView) view.findViewById(R.id.ivback_frport);
			slidingiv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					menu.toggle();
				}
			});
			ivrep_frport = (ImageView) view.findViewById(R.id.ivrep_frport);
			ivrep_frport.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), HistoryActivity.class);
					intent.putExtra("id", rfid);
					startActivityForResult(intent, 0);
					getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
			mSharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
			ahIdialog = new AHIdialog(getActivity());
			odIdialog = new ODIdialog(getActivity());
			hxzTdialog = new HXZTdialog(getActivity());
			dtQdialog = new DTQdialog(getActivity());

			if (conView1 != null) {
				return conView1;
			} else {
				conView1 = inflater.inflate(R.layout.annotation_ahi, null);
				closepw1 = (Button) conView1.findViewById(R.id.close_popw1);
				closepw1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			if (conView2 != null) {
				return conView2;
			} else {
				conView2 = inflater.inflate(R.layout.annotation_smhxzt, null);
				closepw2 = (Button) conView2.findViewById(R.id.close_popw2);
				closepw2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			if (conView3 != null) {
				return conView3;
			} else {
				conView3 = inflater.inflate(R.layout.annotation_dtq, null);
				closepw3 = (Button) conView3.findViewById(R.id.close_popw3);
				closepw3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}

			if (conView4 != null) {
				return conView4;
			} else {
				conView4 = inflater.inflate(R.layout.annotation_lspo2, null);
				closepw4 = (Button) conView4.findViewById(R.id.close_popw4);
				closepw4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			if (conView5 != null) {
				return conView5;
			} else {
				conView5 = inflater.inflate(R.layout.annotation_yjzcs, null);
				closepw5 = (Button) conView5.findViewById(R.id.close_popw5);
				closepw5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			// bm1tykmzri

			if (conView6 != null) {
				return conView6;
			} else {
				conView6 = inflater.inflate(R.layout.annotation_mspo2, null);
				closepw6 = (Button) conView6.findViewById(R.id.close_popw6);
				closepw6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			if (conView7 != null) {
				return conView7;
			} else {
				conView7 = inflater.inflate(R.layout.annotation_tp90, null);
				closepw7 = (Button) conView7.findViewById(R.id.close_popw7);
				closepw7.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			if (conView8 != null) {
				return conView8;
			} else {
				conView8 = inflater.inflate(R.layout.annotation_yjwhzs, null);
				closepw8 = (Button) conView8.findViewById(R.id.close_p_yjwhzs);
				closepw8.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
			mChart = (PieChart) view.findViewById(R.id.spread_pie_chart);
			// 分析图
			initgroup();
			initData();

			// mfilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			// getActivity().registerReceiver(mReceiver, mfilter);

		}
		// dayin_psreport = (ImageView) view.findViewById(R.id.dayin_psreport);

		// isConnected = NetworkProber.isWifi(MyApplication.getContext());
		// isConnected3g = NetworkProber.is3G(MyApplication.getContext());

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		// dayin();

		// muuid = mSharedPreferences.getString("mreportid", null);

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 0) {
			Bundle dataBun = data.getBundleExtra("uh");
			uhInfo = (UserHistory) dataBun.getSerializable("uh");
		}
	}

	private void showHisDate() {
		Log.d(Utils.TAG, "showHisDate()");
		Log.d(Utils.TAG,
				uhInfo == null ? "uhInfo == null"
						: ("uhInfo.getUploadfile() = " + uhInfo.getUploadfile() + ", uhInfo.getHistoryupload() = "
								+ uhInfo.getHistoryupload()));
		if (uhInfo != null && uhInfo.getUploadfile() == 1 && uhInfo.getHistoryupload() == 1) {
			// ahi
			ahitv.setText(uhInfo.getHahiIndex());
			// 呼吸暂停次数
			hxztcstv.setText(uhInfo.getHhxztIndex());
			// 低通气次数
			dtqcstv.setText(uhInfo.getHdtqIndex());
			// 90%占比
			dyzbtv.setText(uhInfo.getHxybhdzbIndex());
			// 平均血氧饱和度
			pjxybhdtv.setText(uhInfo.getHpjxybhdIndex());
			// 最低血氧饱和度
			zdxybhdtv.setText(uhInfo.getHzdxybhdIndex());
			// 氧减总次数
			yjzcstv.setText(uhInfo.getHyjzcsIndex());
			// 氧减危害指数
			yjwhzstv.setText(uhInfo.getHxywhzsIndex());
			// 最高脉率
			zgmltv.setText(String.valueOf(uhInfo.getHmaxml()));
			// 最低脉率
			zdmltv.setText(String.valueOf(uhInfo.getHminml()));
			// 平均脉率
			pjmltv.setText(uhInfo.getHavgml());
			// 平均血流灌注度
			pjxlgzdtv.setText(uhInfo.getHavgxlgzd());
			/** 呼吸质量评分 */
			hxzlpftv.setText(String.valueOf(uhInfo.getHscoreHxzb()));
			/**
			 * 睡眠时间
			 */
			// 开始时
			starthtv.setText(String.valueOf(uhInfo.getHstarthour()));
			// 开始分
			startmtv.setText(Utils.formTime(String.valueOf(uhInfo.getHstartminute())));
			// 结束时
			endhtv.setText(String.valueOf(uhInfo.getHendhour()));
			// 结束分
			endmtv.setText(Utils.formTime(String.valueOf(uhInfo.getHendminute())));
			/**
			 * 睡眠时长
			 */
			// 睡眠时长
			// 时
			smschtv.setText(String.valueOf(uhInfo.getHsmschour()));
			// 分
			smscmtv.setText(Utils.formTime(String.valueOf(uhInfo.getHsmscminute())));
			// 深睡期时长
			// 时
			ssqschtv.setText(String.valueOf(uhInfo.getHsshour()));
			// 分
			ssqscmtv.setText(String.valueOf(uhInfo.getHssminute()));
			// 浅睡期时长
			// 时
			qsqschtv.setText(String.valueOf(uhInfo.getHqshour()));
			// 分
			qsqscmtv.setText(String.valueOf(uhInfo.getHqsminute()));
			// 清醒期时长
			// 时
			qxqschtv.setText(String.valueOf(uhInfo.getHqxhour()));
			// 分
			qxqscmtv.setText(String.valueOf(uhInfo.getHqxminute()));
			/** 睡眠质量评分 */
			smzlpftv.setText(String.valueOf(uhInfo.getHsleepscore()));

			if ("重度".equals(uhInfo.getHosahsdegree()) || "Severe".equalsIgnoreCase(uhInfo.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.severe));
			} else if ("中度".equals(uhInfo.getHosahsdegree()) || "Moderate".equalsIgnoreCase(uhInfo.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.moderate));
			} else if ("轻度".equals(uhInfo.getHosahsdegree()) || "Mild".equalsIgnoreCase(uhInfo.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.light));
			} else if ("无".equals(uhInfo.getHosahsdegree()) || "Normal".equalsIgnoreCase(uhInfo.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.no_input));
			}

			if ("重度".equals(uhInfo.getHdyxzdegree()) || "Severe".equalsIgnoreCase(uhInfo.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.severe));
			} else if ("中度".equals(uhInfo.getHdyxzdegree()) || "Moderate".equalsIgnoreCase(uhInfo.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.moderate));
			} else if ("轻度".equals(uhInfo.getHdyxzdegree()) || "Mild".equalsIgnoreCase(uhInfo.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.light));
			} else if ("无".equals(uhInfo.getHdyxzdegree()) || "Normal".equalsIgnoreCase(uhInfo.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.no_input));
			}

			// osahstv.setText(uhInfo.getHosahsdegree());
			// dyxztv.setText(uhInfo.getHdyxzdegree());

			time_pie = new int[3];
			sleep = new String[3];
			time_pie[0] = uhInfo.getHsshour() * 60 + uhInfo.getHssminute(); // 深睡
			time_pie[1] = uhInfo.getHqshour() * 60 + uhInfo.getHqsminute(); // 浅睡
			time_pie[2] = uhInfo.getHqxhour() * 60 + uhInfo.getHqxminute(); // 清醒
			sleep[0] = MyApplication.getContext().getString(R.string.deep_sleep);
			sleep[1] = MyApplication.getContext().getString(R.string.light_sleep);
			sleep[2] = MyApplication.getContext().getString(R.string.sober);
			selectPieData();
			initChart(time_pie, sleep);
		} else {
			initData();
			Toast.makeText(getActivity(), MyApplication.getContext()
					.getString(R.string.report_did_not_successfully_uploaded_open_the_network), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 控件
	 */
	private void initgroup() {
		rp_title = (TextView) view.findViewById(R.id.title_frport);
		relativeLayout = (RelativeLayout) view.findViewById(R.id.en_email);
		scrollview_view = (ScrollView) view.findViewById(R.id.scrollview_view);

		hxztdtqzs_frport = (TextView) view.findViewById(R.id.hxztdtqzs_frport);
		hxztdtqzs_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_ahi();
			}
		});
		hxztcs_frport = (TextView) view.findViewById(R.id.hxztcs_frport);
		hxztcs_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_hxztcs();
			}
		});
		dtqcs_frport = (TextView) view.findViewById(R.id.dtqcs_frport);
		dtqcs_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_dtq();
			}
		});
		xybhddy_frport = (TextView) view.findViewById(R.id.xybhddy_frport);
		xybhddy_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_xybhd();
			}
		});
		yjwhzs_freport = (TextView) view.findViewById(R.id.yjwhzs_freport);
		yjwhzs_freport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_yjwhzs();
			}

		});

		yjzcs_freport = (TextView) view.findViewById(R.id.yjzcs_freport);
		yjzcs_freport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_yjzcs();
			}
		});
		pjxybhd_frport = (TextView) view.findViewById(R.id.pjxybhd_frport);
		pjxybhd_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_pjxybhd();
			}
		});
		zdxybhd_frport = (TextView) view.findViewById(R.id.zdxybhd_frport);
		zdxybhd_frport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				showpopwindow_zdxybhd();
			}
		});

		// osahs
		osahstv = (TextView) view.findViewById(R.id.osahs_frport);
		// 低氧血症
		dyxztv = (TextView) view.findViewById(R.id.dyxz_frport);
		// ahi
		ahitv = (TextView) view.findViewById(R.id.xh_ahi_frport);
		ahitv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				if (uhInfo != null) {
					ahIdialog.AhiData(um, dh, uhInfo, ep);
				} else {
					ahIdialog.AhiData(um, dh, last_uh, ep);
				}
				ahIdialog.viewDialog(MyApplication.getContext().getString(R.string.apnea_hypopnea_index_ahi));
			}
		});
		// 呼吸暂停次数
		hxztcstv = (TextView) view.findViewById(R.id.h_x_z_t_frport);
		hxztcstv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				if (uhInfo != null) {
					hxzTdialog.HxztData(uhInfo);
				} else {
					hxzTdialog.HxztData(last_uh);
				}
				hxzTdialog.viewDialog("呼吸暂停指数");
			}
		});
		// 低通气次数
		dtqcstv = (TextView) view.findViewById(R.id.d_t_q_frport);
		dtqcstv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				if (uhInfo != null) {
					dtQdialog.DtqData(uhInfo);
				} else {
					dtQdialog.DtqData(last_uh);
				}
				dtQdialog.viewDialog("低通气指数");
			}
		});
		// 90%占比
		dyzbtv = (TextView) view.findViewById(R.id.x_y_b_h_d_frport);
		// 平均血氧饱和度
		pjxybhdtv = (TextView) view.findViewById(R.id.p_j_x_y_b_h_d_frport);
		pjxybhdtv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (DiaUtilsBtnSleep.isFastClick()) {
					return;
				}
				if (uhInfo != null) {
					odIdialog.OdiData(uhInfo);
				} else {
					odIdialog.OdiData(last_uh);
				}
				odIdialog.viewDialog(MyApplication.getContext().getString(R.string.mean_blood_oxygen_saturation));
			}
		});

		// 最低血氧饱和度
		zdxybhdtv = (TextView) view.findViewById(R.id.z_d_x_y_b_h_d_frport);
		// 氧减总次数
		yjzcstv = (TextView) view.findViewById(R.id.y_j_z_c_s_freport);
		// 氧减危害指数
		yjwhzstv = (TextView) view.findViewById(R.id.y_j_w_h_z_s_freport);
		// 最高脉率
		zgmltv = (TextView) view.findViewById(R.id.z_g_m_l_freport);
		// 最低脉率
		zdmltv = (TextView) view.findViewById(R.id.z_d_m_l_freport);
		// 平均脉率
		pjmltv = (TextView) view.findViewById(R.id.p_j_m_l_freport);
		// 平均血流灌注度
		pjxlgzdtv = (TextView) view.findViewById(R.id.p_j_x_y_g_z_d_freport);
		/** 呼吸质量评分 */
		hxzlpftv = (TextView) view.findViewById(R.id.h_x_z_l_p_f_freport);
		/**
		 * 睡眠时间
		 */
		// 开始时
		starthtv = (TextView) view.findViewById(R.id.start_hour_freport);
		// 开始分
		startmtv = (TextView) view.findViewById(R.id.start_minute_freport);
		// 结束时
		endhtv = (TextView) view.findViewById(R.id.end_hour_freport);
		// 结束分
		endmtv = (TextView) view.findViewById(R.id.end_minute_freport);
		/**
		 * 睡眠时长
		 */
		// 睡眠时长
		// 时
		smschtv = (TextView) view.findViewById(R.id.smsc_hour_freport);
		// 分
		smscmtv = (TextView) view.findViewById(R.id.smsc_minute_freport);
		// 深睡期时长
		// 时
		ssqschtv = (TextView) view.findViewById(R.id.sssc_h_freport);
		// 分
		ssqscmtv = (TextView) view.findViewById(R.id.sssc_m_freport);
		// 浅睡期时长
		// 时
		qsqschtv = (TextView) view.findViewById(R.id.qsqsc_h_freport);
		// 分
		qsqscmtv = (TextView) view.findViewById(R.id.qsqsc_m_freport);
		// 清醒期时长
		// 时
		qxqschtv = (TextView) view.findViewById(R.id.qxqsc_h_freport);
		// 分
		qxqscmtv = (TextView) view.findViewById(R.id.qxqsc_m_freport);
		/** 睡眠质量评分 */
		smzlpftv = (TextView) view.findViewById(R.id.hxzlpf_freport);

		String mTitle = rp_title.getText().toString();
		if ("Analysis".equals(mTitle)) {
			relativeLayout.setVisibility(View.VISIBLE);
			// 分享
			sharedateiv = (ImageView) view.findViewById(R.id.share_data);
			sharedateiv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (DiaUtilsBtnSleep.isFastClick()) {
						return;
					}
					File sdcardDir = Environment.getExternalStorageDirectory();
					String imagepath = sdcardDir.getPath() + File.separator + "TaiirHome" + File.separator + "image";
					File file = new File(imagepath);
					OperationFileHelper.DeleteDataFile(file);
					String shareahi = ahitv.getText().toString();
					if (!shareahi.equals("--")) {
						progressDialog = new Dialog(getActivity(), R.style.progress_dialog);
						progressDialog.setContentView(R.layout.send_and_share_waitdialog);
						progressDialog.setCancelable(true);
						progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
						progressDialog.show();
						mShareHandler.sendEmptyMessageDelayed(1, 1000);
					} else {
						Toast.makeText(getActivity(), "No report!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			// 发邮件
			sendemail = (ImageView) view.findViewById(R.id.send_email);
			sendemail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (DiaUtilsBtnSleep.isFastClick()) {
						return;
					}
					File sdcardDir = Environment.getExternalStorageDirectory();
					String pdfpath = sdcardDir.getPath() + File.separator + "TaiirHome" + File.separator + "pdf";
					File file = new File(pdfpath);
					OperationFileHelper.DeleteDataFile(file);
					String sendahi = ahitv.getText().toString();
					if (!sendahi.equals("--")) {
						progressDialog = new Dialog(getActivity(), R.style.progress_dialog);
						progressDialog.setContentView(R.layout.send_and_share_waitdialog);
						progressDialog.setCancelable(true);
						progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
						progressDialog.show();
						new sendEmail().start();
					} else {
						Toast.makeText(getActivity(), "No report!", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			relativeLayout.setVisibility(View.GONE);
		}

	}

	// wanghanqing
	public void showDataByUpload(int startBy) {
		Log.d(Utils.TAG, "showDataByUpload(), startBy: " + (startBy == startBy_onResume ? "startBy_onResume"
				: (startBy == startBy_calculated ? "startBy_calculated" : "startBy_broadcast")));
		pd = new PublicDaoImpl(getActivity());
		if (rfid != -1) {
			last_uh = pd.findMaxHistoryIdByUserId(rfid);
		}
		Log.d(Utils.TAG,
				last_uh == null ? "last_uh == null"
						: ("last_uh.getUploadfile() = " + last_uh.getUploadfile() + ", last_uh.getHistoryupload() = "
								+ last_uh.getHistoryupload()));
		if (last_uh != null && last_uh.getUploadfile() == 1 && last_uh.getHistoryupload() == 1) {
			// ahi
			ahitv.setText(last_uh.getHahiIndex());
			// 呼吸暂停次数
			hxztcstv.setText(last_uh.getHhxztIndex());
			// 低通气次数
			dtqcstv.setText(last_uh.getHdtqIndex());
			// 90%占比
			dyzbtv.setText(last_uh.getHxybhdzbIndex());
			// 平均血氧饱和度
			pjxybhdtv.setText(last_uh.getHpjxybhdIndex());
			// 最低血氧饱和度
			zdxybhdtv.setText(last_uh.getHzdxybhdIndex());
			// 氧减总次数
			yjzcstv.setText(last_uh.getHyjzcsIndex());
			// 氧减危害指数
			yjwhzstv.setText(last_uh.getHxywhzsIndex());
			// 最高脉率
			zgmltv.setText(String.valueOf(last_uh.getHmaxml()));
			// 最低脉率
			zdmltv.setText(String.valueOf(last_uh.getHminml()));
			// 平均脉率
			pjmltv.setText(last_uh.getHavgml());
			// 平均血流灌注度
			pjxlgzdtv.setText(last_uh.getHavgxlgzd());
			/** 呼吸质量评分 */
			hxzlpftv.setText(String.valueOf(last_uh.getHscoreHxzb()));
			/**
			 * 睡眠时间
			 */
			// 开始时
			starthtv.setText(String.valueOf(last_uh.getHstarthour()));
			// 开始分
			startmtv.setText(Utils.formTime(String.valueOf(last_uh.getHstartminute())));
			// 结束时
			endhtv.setText(String.valueOf(last_uh.getHendhour()));
			// 结束分
			endmtv.setText(Utils.formTime(String.valueOf(last_uh.getHendminute())));
			/**
			 * 睡眠时长
			 */
			// 睡眠时长
			// 时
			smschtv.setText(String.valueOf(last_uh.getHsmschour()));
			// 分
			smscmtv.setText(Utils.formTime(String.valueOf(last_uh.getHsmscminute())));
			// 深睡期时长
			// 时
			ssqschtv.setText(String.valueOf(last_uh.getHsshour()));
			// 分
			ssqscmtv.setText(String.valueOf(last_uh.getHssminute()));
			// 浅睡期时长
			// 时
			qsqschtv.setText(String.valueOf(last_uh.getHqshour()));
			// 分
			qsqscmtv.setText(String.valueOf(last_uh.getHqsminute()));
			// 清醒期时长
			// 时
			qxqschtv.setText(String.valueOf(last_uh.getHqxhour()));
			// 分
			qxqscmtv.setText(String.valueOf(last_uh.getHqxminute()));
			/** 睡眠质量评分 */
			smzlpftv.setText(String.valueOf(last_uh.getHsleepscore()));

			if ("重度".equals(last_uh.getHosahsdegree()) || "Severe".equalsIgnoreCase(last_uh.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.severe));
			} else if ("中度".equals(last_uh.getHosahsdegree())
					|| "Moderate".equalsIgnoreCase(last_uh.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.moderate));
			} else if ("轻度".equals(last_uh.getHosahsdegree()) || "Mild".equalsIgnoreCase(last_uh.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.light));
			} else if ("无".equals(last_uh.getHosahsdegree()) || "Normal".equalsIgnoreCase(last_uh.getHosahsdegree())) {
				osahstv.setText(MyApplication.getContext().getString(R.string.no_input));
			}

			if ("重度".equals(last_uh.getHdyxzdegree()) || "Severe".equalsIgnoreCase(last_uh.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.severe));
			} else if ("中度".equals(last_uh.getHdyxzdegree()) || "Moderate".equalsIgnoreCase(last_uh.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.moderate));
			} else if ("轻度".equals(last_uh.getHdyxzdegree()) || "Mild".equalsIgnoreCase(last_uh.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.light));
			} else if ("无".equals(last_uh.getHdyxzdegree()) || "Normal".equalsIgnoreCase(last_uh.getHdyxzdegree())) {
				dyxztv.setText(MyApplication.getContext().getString(R.string.no_input));
			}

			// osahstv.setText(last_uh.getHosahsdegree());
			// dyxztv.setText(last_uh.getHdyxzdegree());

			time_pie = new int[3];
			sleep = new String[3];
			time_pie[0] = last_uh.getHsshour() * 60 + last_uh.getHssminute(); // 深睡
			time_pie[1] = last_uh.getHqshour() * 60 + last_uh.getHqsminute(); // 浅睡
			time_pie[2] = last_uh.getHqxhour() * 60 + last_uh.getHqxminute(); // 清醒
			sleep[0] = MyApplication.getContext().getString(R.string.deep_sleep);
			sleep[1] = MyApplication.getContext().getString(R.string.light_sleep);
			sleep[2] = MyApplication.getContext().getString(R.string.sober);
			selectPieData();
			initChart(time_pie, sleep);
		} else {
			initData();
			if (startBy == startBy_calculated && last_uh != null) {
				Toast.makeText(getActivity(),
						MyApplication.getContext().getString(
								R.string.report_did_not_successfully_uploaded_open_the_network),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 网络未连接时，调用设置方法
	 */
	private void setNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		dialog = builder.create();
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("");
		builder.setMessage(MyApplication.getContext()
				.getString(R.string.network_is_not_available_set_the_mobile_network_view_data));
		builder.setPositiveButton(MyApplication.getContext().getString(R.string.setting_st),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						/**
						 * 判断手机系统的版本！如果API大于10 就是3.0+
						 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
						 */
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									// ACTION_WIFI_SETTINGS
									android.provider.Settings.ACTION_WIFI_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName("com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						startActivity(intent);

					}
				});
		boolean isConnected = NetworkProber.isWifi(MyApplication.getContext());
		boolean isConnected3g = NetworkProber.is3G(MyApplication.getContext());
		if (isConnected || isConnected3g) {
			// showData();
			dialog.dismiss();
		} else {
			builder.create();
			builder.show();
		}

	}

	// wanghanqing
	private void initData() {
		Log.d(Utils.TAG, "initData()");
		// ahi
		ahitv.setText("--");
		// 呼吸暂停次数
		hxztcstv.setText("--");
		// 低通气次数
		dtqcstv.setText("--");
		// 90%占比
		dyzbtv.setText("--");
		// 平均血氧饱和度
		pjxybhdtv.setText("--");
		// 最低血氧饱和度
		zdxybhdtv.setText("--");
		// 氧减总次数
		yjzcstv.setText("--");
		// 氧减危害指数
		yjwhzstv.setText("--");
		// 最高脉率
		zgmltv.setText("--");
		// 最低脉率
		zdmltv.setText("--");
		// 平均脉率
		pjmltv.setText("--");
		// 平均血流灌注度
		pjxlgzdtv.setText("--");
		/** 呼吸质量评分 */
		hxzlpftv.setText("--");
		/**
		 * 睡眠时间
		 */
		// 开始时
		starthtv.setText("--");
		// 开始分
		startmtv.setText("--");
		// 结束时
		endhtv.setText("--");
		// 结束分
		endmtv.setText("--");
		/**
		 * 睡眠时长
		 */
		// 睡眠时长
		// 时
		smschtv.setText("--");
		// 分
		smscmtv.setText("--");
		// 深睡期时长
		// 时
		ssqschtv.setText("--");
		// 分
		ssqscmtv.setText("--");
		// 浅睡期时长
		// 时
		qsqschtv.setText("--");
		// 分
		qsqscmtv.setText("--");
		// 清醒期时长
		// 时
		qxqschtv.setText("--");
		// 分
		qxqscmtv.setText("--");
		/** 睡眠质量评分 */
		smzlpftv.setText("--");

		osahstv.setText("--");
		dyxztv.setText("--");

		time_pie = new int[3];
		sleep = new String[3];
		time_pie[0] = 10; // 深睡
		time_pie[1] = 10; // 浅睡
		time_pie[2] = 10; // 清醒
		sleep[0] = MyApplication.getContext().getString(R.string.deep_sleep);
		sleep[1] = MyApplication.getContext().getString(R.string.light_sleep);
		sleep[2] = MyApplication.getContext().getString(R.string.sober);
		initChart(time_pie, sleep);
	}

	// wanghanqing
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Utils.DEAL_RESULTS:
				Log.d(Utils.TAG, "case Utils.DEAL_RESULTS");
				showDataByUpload(startBy_calculated);
				uhInfo = null;
				break;
			case Utils.NETWORKBROADCAST:
				Log.d(Utils.TAG, "case Utils.NETWORKBROADCAST");
				showDataByUpload(startBy_broadcast);
				uhInfo = null;
				break;
			}
		}
	};

	private void initChart(int[] time_pie, String[] sleep) {
		int time_pie_length = 0;
		int sleep_length = 0;
		if (time_pie != null) {
			time_pie_length = time_pie.length;
		}
		if (sleep != null) {
			sleep_length = sleep.length;
		}
		PieData mPieData1 = getPieData(time_pie, time_pie_length, sleep, sleep_length);
		showChart(mChart, mPieData1);
	}

	private PieData getPieData(int[] range, int rangeLength, String[] sleep, int sleepLength) {

		ArrayList<String> xValues = new ArrayList<String>(); // xVals用来表示每个饼块上的内容
		for (int i = 0; i < sleepLength; i++) {
			if (range[i] == 0) {
				sleep[i] = "";
			}
		}
		for (int i = 0; i < sleepLength; i++) {
			xValues.add(sleep[i]); // 饼块上显示成Quarterly1, Quarterly2, Quarterly3,
									// Quarterly4
		}

		ArrayList<Entry> yValues = new ArrayList<Entry>(); // yVals用来表示封装每个饼块的实际数据

		// 饼图数据
		/**
		 * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14%
		 */

		int sum = 0;
		for (int i : range)
			sum += i;
		if (sum == 0)
			sum = 1;
		for (int i = 0; i < rangeLength; i++) {
			float quarterly = (range[i] * 100 / sum) / 100f;
			yValues.add(new Entry(quarterly, 0));
		}

		// int sum = (int) (range[0] + range[1] + range[2]);
		// if (sum == 0) sum = 1;

		// float quarterly1 = (range[0] * 100 / sum) / 100f;
		// float quarterly2 = (range[1] * 100 / sum) / 100f;
		// float quarterly3 = (range[2] * 100 / sum) / 100f;
		//
		// yValues.add(new Entry(quarterly1, 0));
		// yValues.add(new Entry(quarterly2, 1));
		// yValues.add(new Entry(quarterly3, 2));

		// y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues, ""/* 显示在比例图上 */);
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
		pieDataSet.setValueTextSize(13);// 饼状图各个区域文字大小
		pieDataSet.setValueTextColor(getResources().getColor(R.color.n_select));// 饼状图各个区域文字颜色
		ArrayList<Integer> colors = new ArrayList<Integer>();
		// 饼图颜色
		colors.add(getResources().getColor(R.color.bxt_thr));
		colors.add(getResources().getColor(R.color.bxt_two));
		colors.add(getResources().getColor(R.color.bxt_one));
		// colors.add(Color.rgb(57, 135, 200));

		pieDataSet.setColors(colors);

		// DisplayMetrics metrics = getResources().getDisplayMetrics();
		// float px = 3 * (metrics.densityDpi / 160f);
		// pieDataSet.setSelectionShift(px); // 选中态多出的长度

		PieData pieData = new PieData(xValues, pieDataSet);

		return pieData;
	}

	private void showChart(PieChart pieChart, PieData pieData) {
		// pieChart.setHoleColorTransparent(true);

		// pieChart.setHoleRadius(40f); //半径
		pieChart.setTransparentCircleRadius(0); // 半透明圈
		pieChart.setHoleRadius(0); // 实心圆

		pieChart.setDescription(""); // 设置文字

		// mChart.setDrawYValues(true); //设置是否显示y轴的值的数据
		pieChart.setDrawCenterText(false); // 饼状图中间可以添加文字

		pieChart.setDrawHoleEnabled(true);

		pieChart.setRotationAngle(90); // 初始旋转角度

		// draws the corresponding description value into the slice
		// mChart.setDrawXValues(true);

		// enable rotation of the chart by touch
		pieChart.setRotationEnabled(false); // 可以手动旋转

		// display percentage values
		pieChart.setUsePercentValues(true); // 显示成百分比
		pieData.setValueFormatter(new MyPercentFormatter()); // 设置显示百分号(小数)

		// mChart.setUnit("");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		// mChart.setOnChartValueSelectedListener(this);
		pieChart.setTouchEnabled(false); // 触屏事件

		// mChart.setOnAnimationListener(this);

		// pieChart.setCenterText("Quarterly Revenue"); //饼状图中间的文字

		// 设置数据
		pieChart.setData(pieData);

		// undo all highlights
		// pieChart.highlightValues(null);
		// pieChart.invalidate();

		// 设置饼图说明
		Legend mLegend = pieChart.getLegend(); // 设置比例图
		mLegend.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE); // 最右边显示
		mLegend.setTextColor(getResources().getColor(R.color.n_select));

		// mLegend.setEnabled(false);
		// mLegend.setForm(LegendForm.LINE); //设置比例图的形状，默认是方形
		// mLegend.setXEntrySpace(30f);
		// mLegend.setYEntrySpace(30f);
		// mLegend.setXOffset(-10f);
		mLegend.setYOffset(185f);
		pieChart.animateXY(1000, 1000); // 设置动画
		// mChart.spin(2000, 0, 360);
	}

	/**
	 * 筛选饼图中需要显示的数据，去除百分比位0的数据
	 */
	private void selectPieData() {
		int count = 0; // 计算值为0的数据个数
		for (int i : time_pie) {
			if (i == 0)
				count++;
		}
		if (count > 0) {
			int len = time_pie.length - count;
			int temp_count = 0;
			int[] temp_time_pie = new int[len];
			String[] temp_sleep = new String[len];
			for (int i = 0; i < time_pie.length; i++) {
				if (time_pie[i] != 0) {
					temp_time_pie[temp_count] = time_pie[i];
					temp_sleep[temp_count] = sleep[i];
					temp_count++;
				}
			}
			time_pie = temp_time_pie;
			sleep = temp_sleep;
		}
	}

	@Override
	public void onResume() {
		Log.i(Utils.TAG, "ReportFragment, onResume()");
		super.onResume();
		showDataByUpload(startBy_onResume);
		if (uhInfo != null) {
			showHisDate();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(Utils.TAG, "ReportFragment, onStart()");
		// ditText.setInputType(InputType.TYPE_NULL);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(Utils.TAG, "ReportFragment, onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(Utils.TAG, "ReportFragment, onStop()");

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.i(Utils.TAG, "ReportFragment, onDestroyView()");
	}

	@Override
	public void onDestroy() {
		// if (mReceiver != null) {
		// getActivity().unregisterReceiver(mReceiver);
		// }
		super.onDestroy();
		Log.i(Utils.TAG, "ReportFragment, onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i(Utils.TAG, "ReportFragment, onDetach()");
	}

	private void showpopwindow_ahi() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView1);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_hxztcs() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView2);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_dtq() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView3);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_xybhd() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView4);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_yjwhzs() {
		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView8);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_yjzcs() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView5);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_pjxybhd() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView6);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);

	}

	private void showpopwindow_zdxybhd() {
		window = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(conView7);
		// 在底部显示
		window.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	/**
	 * 
	 * 王宁
	 */
	public void showPDF(String urlpath, String username) throws Exception {
		URL u = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		Locale lca = MyApplication.getContext().getResources().getConfiguration().locale;
		conn.setRequestProperty("language", lca.getLanguage());
		String path = createDir(username + ".pdf");

		byte[] buffer = new byte[1024 * 8];
		int read;
		BufferedInputStream bin;
		try {
			bin = new BufferedInputStream(conn.getInputStream());
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path));
			while ((read = bin.read(buffer)) > -1) {
				bout.write(buffer, 0, read);
			}
			bout.flush();
			bout.close();
			// open_pdf
			// File file = new File(path);
			// if (file.exists() && file.length() > 0) {
			// Uri uri = Uri.parse(path);
			// Log.d(Utils.TAG, "ReportFragment.showPDF(), ----------------uri =
			// " + uri);
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String createDir(String filename) {
		File sdcardDir = Environment.getExternalStorageDirectory();
		// File.separator.replace->"/"
		String path = sdcardDir.getPath() + File.separator + "TaiirHome" + File.separator + "pdf";
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		path = path + File.separator + filename;
		return path;
	}

	// public BroadcastReceiver mReceiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
	// isConnected = NetworkProber.isWifi(context);
	// isConnected3g = NetworkProber.is3G(context);
	//
	// if (isConnected || isConnected3g) {
	//
	// showData2();
	// } else {
	// initData();
	// }
	// }
	// }
	// };
	class sendEmail extends Thread {
		@Override
		public void run() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			PublicDao pd = new PublicDaoImpl(getActivity());
			UserManager um = pd.findAllUserById(rfid);
			UserHistory uh = pd.findMaxHistoryIdByUserId(rfid);
			if (um != null && uh != null) {
				String username = um.getUsername();
				String uuid = uh.getReportuuid();
				long starttime = (long) uh.getTestdate() * 1000;
				String strdate = sdf.format(new Date(starttime));

				if (uuid != null && username != null && strdate != null) {
					try {
						showPDF(urlpath + uuid, username + "_" + strdate);
						// urlpath + uuid/zh
						// urlpath + uuid/en
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.cancel();
					}
					Intent email = new Intent(android.content.Intent.ACTION_SEND);
					File sdcardDir = Environment.getExternalStorageDirectory();
					String filename = username + "_" + strdate;
					pdfpath = sdcardDir.getPath() + File.separator + "TaiirHome" + File.separator + "pdf"
							+ File.separator + filename + ".pdf";
					File file = new File(pdfpath);
					// 邮件发送类型：带附件的邮件
					email.setType("application/octet-stream");
					// 邮件接收者（数组，可以是多位接收者）
					String[] emailReciver = new String[] { " " };
					String emailTitle = "sleepcare";
					String emailContent = "sleepcare";
					// 设置邮件地址
					email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
					// 设置邮件标题
					email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);
					// 设置发送的内容
					email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
					// 附件
					email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					// 调用系统的邮件系统
					startActivity(Intent.createChooser(email, "Send PDF"));

					mSendHandler.sendEmptyMessageDelayed(2, 900000);
				}
			} else {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.cancel();
				}
				mSendHandler.sendEmptyMessage(1);
				Log.d("TAG", "报告为空，不能发送邮件！");
			}
		}

	}

	private  Handler mShareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// fname =
				// ScreenShot.savePic(ScreenShot.compressImage(ScreenShot.getBitmapByView(scrollview_view)));
				Bitmap mBitmap1 = ScreenShot.getBitmapByView(scrollview_view);

				// Bitmap mBitmap2 =
				// ScreenShot.compressImage(mBi tmap1);
				fname = ScreenShot.savePic(mBitmap1);
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.cancel();
				}
				// 由文件得到uri
				imageUri = Uri.fromFile(new File(fname));

				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
				shareIntent.setType("image/*");
				startActivity(Intent.createChooser(shareIntent, "Report Share"));
				break;
			default:
				break;
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler mSendHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(getActivity(), "No report!", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				delFile(pdfpath);
				Log.d("TAG", "PDF(email)文件删除");
				break;
			default:
				break;
			}
		}
	};

	// 删除文件
	public static void delFile(String fileName) {
		String path = fileName;
		File file = new File(path);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
	}

}
