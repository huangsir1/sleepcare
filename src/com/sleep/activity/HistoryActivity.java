package com.sleep.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taiir.sleepcare.home.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.UserHistory;
import com.sleep.myui.MonthDateView;
import com.sleep.myui.MonthDateView.DateClick;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity implements OnClickListener {

	private TextView tv;
	private ImageView iv_back;
	private ImageView iv_pick;
	private LineChart mChart_breath;
	private LineChart mChart_sleep;

	private TextView tv_today;
	private ImageView iv_left;
	private ImageView iv_right;

	private LinearLayout ll_date;
	private MonthDateView monthDateView;
	private TextView tv_check;
	private boolean isShow;

	private PublicDao pd;
	private int userId;

	private int year;
	private int month;
	private int day;
	private String monthStr;
	private String dayStr;
	private String today;
	private String theDate;
	private ArrayList<String> breathScores;
	private ArrayList<String> sleepScores;
	private ArrayList<String> dataRecords;
	private ArrayList<String> dataYyMmDd;
	private List<UserHistory> myHisList;
	private UserHistory uh = null;
	private Toast toast = null;

	private SimpleAdapter adapter;
	private ListView mlistView;
	private LinearLayout layoutlist;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historydata);
		ExitActivity.getInstance().addActivity(this);
		Intent intent = getIntent();
		userId = intent.getIntExtra("id", -1);

		initView();
		setOnClick();

		breathScores = new ArrayList<String>();
		sleepScores = new ArrayList<String>();
		dataRecords = new ArrayList<String>();
		dataYyMmDd = new ArrayList<String>();
		getScoreDate();

		System.out.println("呼吸评分个数" + breathScores.size());

		initChart(mChart_breath, breathScores);
		initChart(mChart_sleep, sleepScores);

	}

	/**
	 * 获取历史数据集合
	 */
	private void getScoreDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pd = new PublicDaoImpl(HistoryActivity.this);
		// UserHistory uh = pd.findHistoryToUserManager(userId);
		List<UserHistory> hisList = pd.findAllHistorys();
		myHisList = new ArrayList<UserHistory>();

		for (int i = 0; i < hisList.size(); i++) {
			if (userId == hisList.get(i).getUserid()) {
				myHisList.add(hisList.get(i));
				int breathscore = hisList.get(i).getHscoreHxzb();
				int sleepscore = hisList.get(i).getHsleepscore();
				int id = hisList.get(i).getId();

				long longs = ((long) hisList.get(i).getTestdate()) * 1000;

				Date date = new Date(longs);
				String dateString = sdf.format(date);
				String[] dateDivide = dateString.split("-");

				if (breathscore != 0 || sleepscore != 0) {
					String breath = String.valueOf(breathscore);
					String sleep = String.valueOf(sleepscore);
					String dateRecord = dateDivide[1] + "." + dateDivide[2];

					breathScores.add(breath);
					sleepScores.add(sleep);

					dataRecords.add(dateRecord);
					dataYyMmDd.add(dateString);
				}
			}
		}
		Log.w("TAG_History", "历史记录的个数为：" + myHisList.size());

		int size = dataRecords.size();
		if (size < 7) {
			int count = 7 - size;
			for (int i = 0; i < count; i++) {
				dataRecords.add("");
			}
		}

	}

	/**
	 * 加载试图
	 */
	private void initView() {
		tv = (TextView) findViewById(R.id.tv);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_pick = (ImageView) findViewById(R.id.iv_pick);
		mChart_breath = (LineChart) findViewById(R.id.chart_breath);
		mChart_sleep = (LineChart) findViewById(R.id.chart_sleep);

		tv_today = (TextView) findViewById(R.id.tv_today);
		ll_date = (LinearLayout) findViewById(R.id.ll_date);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_right = (ImageView) findViewById(R.id.iv_right);
		monthDateView = (MonthDateView) findViewById(R.id.mdv);
		monthDateView.setTextView(tv_today, null);
		tv_check = (TextView) findViewById(R.id.tv_check);
		// listview
		layoutlist = (LinearLayout) findViewById(R.id.history_list);
		mlistView = (ListView) findViewById(R.id.history_listview);

		// checkDateFormat();
		year = monthDateView.getmSelYear();
		month = monthDateView.getmSelMonth();
		day = monthDateView.getmSelDay();
		today = year + "-" + month + "-" + day;
		tv_today.setText(today);
	}

	/**
	 * 检查日期数据格式并更正
	 */
	private void checkDateFormat() {
		year = monthDateView.getmSelYear();
		month = monthDateView.getmSelMonth();
		day = monthDateView.getmSelDay();
		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = month + "";
		}
		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = day + "";
		}
		theDate = year + "-" + monthStr + "-" + dayStr;
		setHistoryItem(theDate);
	}

	private void setHistoryItem(String theDate) {
		final Intent intent = getIntent();
		if (isHaveDate(theDate)) {
			final List<UserHistory> hisList = new ArrayList<UserHistory>();
			for (int i = 0; i < myHisList.size(); i++) {
				if (theDate.equals(sdf.format(new Date(((long) myHisList.get(i).getTestdate()) * 1000)))) {
					hisList.add(myHisList.get(i));
					if (uh == null)
						uh = myHisList.get(i);
					else if (myHisList.get(i).getId() > uh.getId())
						uh = myHisList.get(i);
				}
			}
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Date date;
			Date edate;
			long lg = 0;
			long endg = 0;
			String str;
			String str1;
			for (int i = 0; i < hisList.size(); i++) {
				// date1 = String.valueOf(hisList.get(i).getTestdate());
				lg = (long) hisList.get(i).getTestdate() * 1000;
				endg = (long) hisList.get(i).getHendtimes() * 1000;
				date = new Date(lg);
				edate = new Date(endg);

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
				str = simpleDateFormat.format(date);
				str1 = simpleDateFormat.format(edate);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("his", str);
				map.put("hend", str1);
				map.put("img", R.drawable.history_records);
				list.add(map);
			}

			layoutlist.setVisibility(View.VISIBLE);
			adapter = new SimpleAdapter(HistoryActivity.this, list, R.layout.history_listview_adapter,
					new String[] { "img", "his", "hend" },
					new int[] { R.id.image_adapter, R.id.his_rec_adapter, R.id.his_end_times });
			mlistView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			// 点击查看历史
			mlistView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Bundle bundle = new Bundle();
					bundle.putSerializable("uh", hisList.get(position));
					intent.putExtra("uh", bundle);
					HistoryActivity.this.setResult(0, intent);
					finish();
				}
			});

		} else {
			layoutlist.setVisibility(View.GONE);
			this.setResult(1, intent);
			showTextToast(theDate + HistoryActivity.this.getString(R.string.no_measured_data));
		}
	}

	/**
	 * 整个页面的点击事件的设置
	 */
	private void setOnClick() {
		tv_today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShow) {
					ll_date.setVisibility(View.GONE);
					iv_left.setVisibility(View.INVISIBLE);
					iv_right.setVisibility(View.INVISIBLE);
					layoutlist.setVisibility(View.GONE);
					tv_today.setText(today);
					monthDateView.setTodayToView();
					isShow = false;
				} else {
					ll_date.setVisibility(View.VISIBLE);
					iv_right.setVisibility(View.VISIBLE);
					iv_left.setVisibility(View.VISIBLE);
					checkDateFormat();
					isShow = true;
				}

			}
		});

		monthDateView.setDateClick(new DateClick() {

			@Override
			public void onClickOnDate() {
				checkDateFormat();
			}
		});

		iv_back.setOnClickListener(this);
		iv_pick.setOnClickListener(this);
		iv_left.setOnClickListener(this);
		iv_right.setOnClickListener(this);
		tv_check.setOnClickListener(this);
		// tv.setOnClickListener(this);
	}

	/**
	 * 点击事件的响应
	 */
	@Override
	public void onClick(View v) {
		if (DiaUtilsBtnSleep.isFastClick()) {
			return;
		}
		final Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.iv_back:
			this.setResult(1, intent);
			finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			break;
		case R.id.iv_left:
			monthDateView.onLeftClick();
			checkDateFormat();

			break;
		case R.id.iv_right:
			monthDateView.onRightClick();
			checkDateFormat();

			break;
		case R.id.iv_pick:
			if (isShow) {
				ll_date.setVisibility(View.GONE);
				iv_left.setVisibility(View.INVISIBLE);
				iv_right.setVisibility(View.INVISIBLE);
				layoutlist.setVisibility(View.GONE);
				tv_today.setText(today);
				monthDateView.setTodayToView();
				isShow = false;
			} else {
				ll_date.setVisibility(View.VISIBLE);
				iv_right.setVisibility(View.VISIBLE);
				iv_left.setVisibility(View.VISIBLE);
				checkDateFormat();
				isShow = true;
			}
			break;
		}
	}

	/**
	 * 确定所选日期是否存在数据
	 * 
	 * @param theDate
	 *            所选日期
	 * @return
	 */
	private boolean isHaveDate(String theDate) {
		for (int i = 0; i < dataYyMmDd.size(); i++) {
			System.out.println("dataYyMmDd.get(i)" + "===" + dataYyMmDd.get(i));
			System.out.println("theDate" + "===" + theDate);
			if (theDate.equals(dataYyMmDd.get(i))) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 加载折现图
	 * 
	 * @param mChart
	 *            图例
	 * @param Scores
	 *            Y轴数据集合
	 */
	private void initChart(LineChart mChart, ArrayList<String> Scores) {
		/**
		 * ====================1.初始化-自由配置===========================
		 */
		Legend legend = mChart.getLegend();
		legend.setEnabled(false);
		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setEnabled(true);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawLabels(true);
		xAxis.setSpaceBetweenLabels(1);
		xAxis.setAvoidFirstLastClipping(true); // 定制X轴起点和终点Label不能超出屏幕。
		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setAxisMaxValue(115);
		YAxis rightAxis = mChart.getAxisRight();
		rightAxis.setEnabled(false);
		// 是否在折线图上添加边框
		mChart.setDrawGridBackground(false);
		mChart.setDrawBorders(false);
		// 设置右下角描述
		mChart.setDescription("");
		// 设置透明度
		mChart.setAlpha(0.8f);
		// 设置网格底下的那条线的颜色
		mChart.setBorderColor(Color.rgb(226, 226, 226));
		// 设置高亮显示
		mChart.setHighlightEnabled(true);
		// 设置是否可以触摸，如为false，则不能拖动，缩放等
		mChart.setTouchEnabled(true);
		// 设置是否可以拖拽
		mChart.setDragEnabled(false);
		// 设置是否可以缩放
		mChart.setScaleEnabled(false);
		// 设置是否能扩大扩小
		mChart.setPinchZoom(false);
		/**
		 * ====================2.布局点添加数据-自由布局===========================
		 */
		// 折线图的点，点击战士的布局和数据
		// MyMarkView mv = new MyMarkView(this);
		// mChart.setMarkerView(mv);
		// 加载数据
		mChart.setNoDataText(HistoryActivity.this.getString(R.string.no_measured_data_please_check_back_to_see));
		LineData data = getLineData(Scores);
		mChart.setData(data);
		/**
		 * ====================3.x，y动画效果和刷新图表等===========================
		 */
		// 从X轴进入的动画
		mChart.animateX(4000);
		mChart.animateY(3000); // 从Y轴进入的动画
		mChart.animateXY(3000, 3000); // 从XY轴一起进入的动画
		// 设置最小的缩放
		mChart.setScaleMinima(0.5f, 1f);

		Legend l = mChart.getLegend();
		l.setForm(Legend.LegendForm.LINE); // 设置图最下面显示的类型
		l.setTextSize(15);
		l.setTextColor(Color.rgb(124, 124, 124));
		l.setFormSize(30f);
		// 刷新图表
		mChart.invalidate();
	}

	/**
	 * 装载横纵坐标轴具体数值
	 * 
	 * @param Scores
	 *            Y轴数值
	 * @return
	 */
	private LineData getLineData(ArrayList<String> Scores) {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = dataRecords.size() - 7; i < dataRecords.size(); i++) {
			xVals.add(dataRecords.get(i));
		}

		ArrayList<Entry> yVals = new ArrayList<Entry>();
		if (Scores.size() > 7) {
			for (int i = Scores.size() - 7; i < Scores.size(); i++) {
				yVals.add(new Entry(Float.parseFloat(Scores.get(i)), i - Scores.size() + 7));
			}
		} else {
			for (int i = 0; i < Scores.size(); i++) {
				yVals.add(new Entry(Float.parseFloat(Scores.get(i)), i));
			}
		}

		LineDataSet set1 = new LineDataSet(yVals, "");
		set1.setDrawCubic(false); // 设置曲线为圆滑的线
		set1.setCubicIntensity(0.2f);
		set1.setDrawFilled(false); // 设置包括的范围区域填充颜色
		set1.setDrawCircles(true); // 设置有圆点
		set1.setLineWidth(2f); // 设置线的宽度
		set1.setCircleSize(5f); // 设置小圆的大小
		set1.setHighLightColor(Color.rgb(255, 255, 255));
		set1.setColor(Color.rgb(56, 180, 234)); // 设置曲线的颜色

		return new LineData(xVals, set1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = getIntent();
			this.setResult(1, intent);
			finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showTextToast(String msg) {
		if (toast == null) {
			toast = Toast.makeText(HistoryActivity.this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
