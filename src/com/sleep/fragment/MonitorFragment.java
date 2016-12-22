package com.sleep.fragment;

import com.taiir.sleepcare.home.R;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sleep.activity.MainActivity;
import com.sleep.bluetooth.EquipmentData;
import com.sleep.utils.CircleBar_one;
import com.sleep.utils.CircleBar_three;
import com.sleep.utils.CircleBar_two;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MonitorFragment extends Fragment {
	private View view;
	private CircleBar_three circleBar_ml;
	private CircleBar_one circleBar_gz;
	private Chronometer mChronometer;
	private long currentTime = 0;
	private View popuview;
	private View contentView1;
	private View contentView2;
	private View contentView3;

	private TextView nametv;
	private Button dims;
	private Context context;
	private PopupWindow window;
	private String TAG;
	public SlidingMenu menu;
	private ImageView slidingiv;
	private TextView tv_x;
	private TextView tv_y;
	private TextView tv_z;
	private int axis_x=0;
	private int axis_y=0;
	private int axis_z=0;
	
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	/**
	 * The main renderer that includes all the renderers customizing a chart.
	 */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	private String seriesTitle1="呼吸数据";
	private XYSeries series1=new XYSeries(seriesTitle1);
	private byte[] temp=new byte[50];//数据缓存
	private int count=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Utils.TAG, "MonitorFragment, onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(Utils.TAG, "MonitorFragment, onCreateView()");
		Intent intent = getActivity().getIntent();
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_monitor, container, false);
			menu = MainActivity.instance.menu;
			slidingiv=(ImageView) view.findViewById(R.id.imageView1_mon);
			slidingiv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					menu.toggle();
				}
			});
			context = getActivity();
			String uname = intent.getStringExtra("cname");
			nametv = (TextView) view.findViewById(R.id.textView1_mon);
			nametv.setText(uname);
			tv_x=(TextView) view.findViewById(R.id.axis_x);
			tv_y=(TextView) view.findViewById(R.id.axis_y);
			tv_z=(TextView) view.findViewById(R.id.axis_z);
			
			mRenderer.setApplyBackgroundColor(true);
			mRenderer.setAxisTitleTextSize(20);
			mRenderer.setChartTitleTextSize(30);
			mRenderer.setLabelsTextSize(15);
			mRenderer.setMargins(new int[] { 20, 30, 15, 30 });
			mRenderer.setPanEnabled(false, false);
			mRenderer.setZoomEnabled(false, false);
			mRenderer.setZoomButtonsVisible(false);
			mRenderer.setMarginsColor(Color.WHITE);
			mRenderer.setShowGridX(true);
			mRenderer.setYAxisMin(-35);
			mRenderer.setYAxisMax(35);
			mRenderer.setPointSize(2);
			mRenderer.setLegendTextSize(30);
			mDataset.addSeries(series1);
			
			// create a new renderer for the new series
			XYSeriesRenderer renderer1 = new XYSeriesRenderer();
			renderer1.setColor(Color.GREEN);
			renderer1.setPointStyle(PointStyle.CIRCLE);
			renderer1.setFillPoints(true);
			renderer1.setDisplayChartValues(false);
			renderer1.setDisplayChartValuesDistance(10);
			mRenderer.addSeriesRenderer(renderer1);
			
			if (mChartView == null) {
				LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart);
				mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
				layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}else {
				mChartView.repaint();
			}

			// 关于弹框
			if (contentView1 != null) {
				return contentView1;
			} else {
				contentView1 = inflater.inflate(R.layout.dialog_explain, null);
				dims = (Button) contentView1.findViewById(R.id.close_button);
				dims.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();

					}
				});
			}
			if (contentView2 != null) {
				return contentView2;
			} else {
				contentView2 = inflater.inflate(R.layout.dialog_explain_ml, null);
				dims = (Button) contentView2.findViewById(R.id.close_button);
				dims.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();

					}
				});
			}
			if (contentView3 != null) {
				return contentView3;
			} else {
				contentView3 = inflater.inflate(R.layout.dialog_explain_gz, null);
				dims = (Button) contentView3.findViewById(R.id.close_button);
				dims.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						window.dismiss();

					}
				});
			}


			circleBar_ml = (CircleBar_three) view.findViewById(R.id.circle_3);
//			circleBar_ml.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (DiaUtilsBtnSleep.isFastClick()) {
//						return;
//					}
//					showPopwindowMl();
//
//				}
//
//			});
			circleBar_gz = (CircleBar_one) view.findViewById(R.id.circle_1);
//			circleBar_gz.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (DiaUtilsBtnSleep.isFastClick()) {
//						return;
//					}
//					showPopwindowGz();
//
//				}
//
//			});
			
			circleBar_ml.setMaxstepnumber(190);
			circleBar_gz.setMaxstepnumber(20);

			// 获得计时器对象
			mChronometer = (Chronometer) view.findViewById(R.id.textv1);

		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		return view;
	}

	// wanghanqing
	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Utils.SHOW_DATA:
				// 实时显示脉率和血氧
				if (((EquipmentData) msg.obj).getMl() == 255 || ((EquipmentData) msg.obj).getXy() == 127) {
					circleBar_ml.update(0, 0);
					circleBar_gz.update(0, 0);
				} else {
					
//					if (count++<temp.length) {
//						
//					}else {
//						count=temp.length;
//					}
						mDataset.removeSeries(series1);
						series1.clear();
						for (int i = temp.length-1; i>0; i--) {
							temp[i] = temp[i-1];
						}
						temp[0] = ((EquipmentData) msg.obj).getPress();
						for (int i = 0; i < temp.length; i++) {
							series1.add(i, temp[i]);
						}
						mDataset.addSeries(series1);
						mChartView.repaint();
						axis_x=((EquipmentData) msg.obj).getAxis_x();
						axis_y=((EquipmentData) msg.obj).getAxis_y();
						axis_z=((EquipmentData) msg.obj).getAxis_z();
						tv_x.setText(String.valueOf(axis_x)+":"+String.valueOf((180*Math.atan(axis_x/Math.sqrt(axis_y*axis_y+axis_z*axis_z)))/Math.PI));
						tv_y.setText(String.valueOf(axis_y)+":"+String.valueOf((180*Math.atan(axis_y/Math.sqrt(axis_x*axis_x+axis_z*axis_z)))/Math.PI));
						tv_z.setText(String.valueOf(axis_z)+":"+String.valueOf((180*Math.atan(axis_z/Math.sqrt(axis_y*axis_y+axis_x*axis_x)))/Math.PI));
					circleBar_ml.update(((EquipmentData) msg.obj).getMl() > 0 ? ((EquipmentData) msg.obj).getMl() : 0, 0);
					circleBar_gz.update(((EquipmentData) msg.obj).getXy()  > 0 ? ((EquipmentData) msg.obj).getXy(): 0, 0);
				}
				break;
			case Utils.CHRONOMETER:
				if (mChronometer != null) {
					switch (msg.arg1) {
					case Utils.CHRONOMETER_START:
						mChronometer.setBase(SystemClock.elapsedRealtime());
						mChronometer.start();
						break;
					case Utils.CHRONOMETER_STOP:
						mChronometer.stop();
						currentTime = SystemClock.elapsedRealtime();
						break;
					case Utils.CHRONOMETER_RESET:
						mChronometer.setBase(SystemClock.elapsedRealtime());
						break;
					case Utils.CHRONOMETER_CONTINUE:
						if(currentTime != 0){
							mChronometer.setBase(SystemClock.elapsedRealtime() - (currentTime - mChronometer.getBase()));
						}else{
							Log.w(TAG, "mChronometer has not stopped!");
						}
						mChronometer.start();
						currentTime = 0;
						break;
					default:
						break;
					}
				}
				break;
			case Utils.RESET_DATA:
				break;
			}
		}
	};

	/**
	 * 显示popupWindow
	 */
	private void showPopwindowGz() {

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(contentView1);
		// 在底部显示
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});

	}

	private void showPopwindowMl() {

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setContentView(contentView2);
		// 在底部显示
		window.showAtLocation(MonitorFragment.this.view.findViewById(R.id.circle_3), Gravity.BOTTOM, 0, 0);

		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});

	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(Utils.TAG, "MonitorFragment, onResume()");

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(Utils.TAG, "MonitorFragment, onStart()");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(Utils.TAG, "MonitorFragment, onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(Utils.TAG, "MonitorFragment, onStop()");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.i(Utils.TAG, "MonitorFragment, onDestroyView()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(Utils.TAG, "MonitorFragment, onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i(Utils.TAG, "MonitorFragment, onDetach()");
	}

}
