package com.sleep.fragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taiir.sleepcare.home.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sleep.activity.MainActivity;
import com.sleep.activity.WebActivity;
import com.sleep.domain.NewInfo;
import com.sleep.html.en.WebActivityEn;
import com.sleep.utils.StreamTools;
import com.sleep.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import taiyi.web.jason.WebAPI;

public class InformationFragment extends Fragment implements OnClickListener, OnTouchListener {
	protected static final int SUCCESS = 1;
	protected static final int FAILED = 2;

	private ViewPager vp_news;
	private LinearLayout ll_points_news;
	private TextView title_en;
	private TextView tv_science;
	private TextView tv_note_one;
	private TextView tv_note_two;
	private TextView tv_tip_one;
	private TextView tv_tip_two;
	private TextView web_manuals;
	public SlidingMenu menu;
	private ImageView slidingiv;
	// private List<NewInfo> newInfoList;
	// private ArrayList<SmartImageView> imageList;
	private ArrayList<ImageView> imageList;
	private int[] imageIds = { R.drawable.vp1, R.drawable.vp2, R.drawable.vp3, R.drawable.vp4 };

	private int lastPosition;

	private boolean isRunning = false;
	private View view;// 缓存页面

	private MyPagerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Utils.TAG, "InformationFragment, onCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(Utils.TAG, "InformationFragment, onCreateView()");

		if (view == null) {
			view = inflater.inflate(R.layout.activity_news, container, false);
			init();
			menu = MainActivity.instance.menu;
			slidingiv = (ImageView) view.findViewById(R.id.sliding_iv);
			slidingiv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					menu.toggle();
				}
			});
			isRunning = true;
			handler.sendEmptyMessageDelayed(0, 3000);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// 先移除
		}

		return view;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				vp_news.setCurrentItem(vp_news.getCurrentItem() + 1);
				if (isRunning) {
					handler.sendEmptyMessageDelayed(0, 3000);
				}
				break;
			// case SUCCESS:
			// System.out.println("成功");
			// newInfoList = (List<NewInfo>) msg.obj;
			// loadImagesPoints();
			//
			// vp_news.setAdapter(new MyPagerAdapter());
			// vp_news.setOnPageChangeListener(new OnPageChangeListener() {
			//
			// @Override
			// public void onPageSelected(int position) {
			// position = position % imageList.size();
			// ll_points_news.getChildAt(position).setEnabled(true);
			// ll_points_news.getChildAt(lastPosition).setEnabled(false);
			// lastPosition = position;
			// }
			//
			// @Override
			// public void onPageScrolled(int arg0, float arg1, int arg2) {
			//
			// }
			//
			// @Override
			// public void onPageScrollStateChanged(int arg0) {
			//
			// }
			// });
			// isRunning = true;
			// handler.sendEmptyMessageDelayed(0, 3000);
			//
			// break;
			// case FAILED:
			// // Toast.makeText(, "请检查您的网络，重新尝试！", 1).show();
			// System.out.println("失败");
			// break;

			default:
				break;
			}

		};
	};

	/**
	 * 1：初始化viewpager和point的线性布局 2：联网获取数据 3：发送handler消息
	 */
	private void init() {
		title_en = (TextView) view.findViewById(R.id.title_en);
		tv_science = (TextView) view.findViewById(R.id.tv_science);
		tv_note_one = (TextView) view.findViewById(R.id.tv_note_one);
		tv_note_two = (TextView) view.findViewById(R.id.tv_note_two);
		tv_tip_one = (TextView) view.findViewById(R.id.tv_tip_one);
		tv_tip_two = (TextView) view.findViewById(R.id.tv_tip_two);
		web_manuals = (TextView) view.findViewById(R.id.web_manuals);

		tv_science.setOnClickListener(this);
		tv_note_one.setOnClickListener(this);
		tv_note_two.setOnClickListener(this);
		tv_tip_one.setOnClickListener(this);
		tv_tip_two.setOnClickListener(this);
		web_manuals.setOnTouchListener(this);

		vp_news = (ViewPager) view.findViewById(R.id.vp_news);
		ll_points_news = (LinearLayout) view.findViewById(R.id.ll_points_news);
		loadImagesPoints();
		adapter = new MyPagerAdapter();
		vp_news.setAdapter(adapter);
		vp_news.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				position = position % imageList.size();
				ll_points_news.getChildAt(position).setEnabled(true);
				ll_points_news.getChildAt(lastPosition).setEnabled(false);
				lastPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// List<NewInfo> newInfoList = getNewsFromInternet();
		// Message msg = new Message();
		// if (newInfoList != null) {
		// msg.what = SUCCESS;
		// msg.obj = newInfoList;
		// } else {
		// msg.what = FAILED;
		// }
		// handler.sendMessage(msg);
		// }
		// }).start();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		String News_En = title_en.getText().toString().trim();
		switch (v.getId()) {
		case R.id.tv_science:
			// String sleep_disordered_breathing =
			// MyApplication.getContext().getString(R.string.);
			if ("News".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("ens", "scien_en");
				intent.putExtra("ten", "What's Respiratory and Sleep Disorders?");
				intent.setClass(getActivity(), WebActivityEn.class);
				startActivity(intent);
			} else if ("相关资讯".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("fileName", "scien");
				intent.putExtra("tn", "睡眠呼吸障碍大科普");
				intent.setClass(getActivity(), WebActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tv_note_one:
			if ("News".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("ens", "tenqu_en");
				intent.putExtra("ten", "Sleep Apnea");
				intent.setClass(getActivity(), WebActivityEn.class);
				startActivity(intent);
			} else if ("相关资讯".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("fileName", "tenqu");
				intent.putExtra("tn", "鼾症十问");
				intent.setClass(getActivity(), WebActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.tv_note_two:
			if ("News".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("ens", "ess_en");
				intent.putExtra("ten", "Epworth Sleepiness Scale");
				intent.setClass(getActivity(), WebActivityEn.class);
				startActivity(intent);
			} else if ("相关资讯".equals(News_En)) {
				intent = new Intent(getActivity(), WebActivity.class);
				intent.putExtra("fileName", "ess");
				intent.putExtra("tn", "Epworth嗜睡量表");
				startActivity(intent);
			}
			break;
		case R.id.tv_tip_one:
			if ("News".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("ens", "desc_en");
				intent.putExtra("ten", "Sleep Apnea Signs, Symptoms and Causes");
				intent.setClass(getActivity(), WebActivityEn.class);
				startActivity(intent);
			} else if ("相关资讯".equals(News_En)) {
				intent = new Intent(getActivity(), WebActivity.class);
				intent.putExtra("fileName", "desc");
				intent.putExtra("tn", "鼾症与阻塞性睡眠呼吸暂停综合征概述");
				startActivity(intent);
			}
			break;
		case R.id.tv_tip_two:
			if ("News".equals(News_En)) {
				intent = new Intent();
				intent.putExtra("ens", "cure_en");
				intent.putExtra("ten", "Sleep Apnea Diagnosis & Treatment");
				intent.setClass(getActivity(), WebActivityEn.class);
				startActivity(intent);
			} else if ("相关资讯".equals(News_En)) {
				intent = new Intent(getActivity(), WebActivity.class);
				intent.putExtra("fileName", "cure");
				intent.putExtra("tn", "伴有呼吸暂停的鼾症如何治疗?");
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 联网获取JSON数据
	 * 
	 * @return List集合
	 */
	private List<NewInfo> getNewsFromInternet() {
		URL url;
		try {
			url = new URL(WebAPI.DEFAULT_URL + "/api/news/getAll");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);

			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				String news = StreamTools.readFromStream(is);
				List<NewInfo> newInfoList = getNewListFromJson(news);
				return newInfoList;
			} else {
				System.out.println("访问失败" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析json数组
	 * 
	 * @param news
	 *            String类型的json数据
	 * @return List集合
	 */
	protected List<NewInfo> getNewListFromJson(String news) {

		List<NewInfo> newInfoList = new ArrayList<NewInfo>();
		try {
			JSONArray array = new JSONArray(news);
			for (int i = 0; i < array.length(); i++) {
				NewInfo newInfo = new NewInfo();

				JSONObject lan = array.getJSONObject(i);
				newInfo.setId(lan.getInt("id"));
				newInfo.setSorts(lan.getInt("sorts"));
				newInfo.setUploadDate(lan.getLong("uploadDate"));
				newInfo.setTitle(lan.getString("title"));
				newInfo.setContent(lan.getString("content"));
				newInfo.setImageUrl("http://pic23.nipic.com/20120808/10608426_165438049311_2.jpg");
				newInfoList.add(newInfo);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newInfoList;

	}

	/**
	 * 加载viewpager和point,设置点击事件，传送web内容
	 */
	private void loadImagesPoints() {

		// imageList = new ArrayList<SmartImageView>();
		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < 4; i++) {
			// NewInfo newInfo = new NewInfo();
			// newInfo = newInfoList.get(i);
			// String imageUrl = newInfo.getImageUrl();
			// final String content = newInfo.getContent();
			//
			// SmartImageView smartImageView = new
			// SmartImageView(getActivity());
			// smartImageView.setImageUrl(imageUrl);
			// smartImageView.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new Intent(getActivity(), WebActivity.class);
			// System.out.println(content);
			// intent.putExtra("content", content);
			// startActivity(intent);
			// }
			// });
			// imageList.add(smartImageView);
			ImageView imageView = new ImageView(getActivity());
			imageView.setBackgroundResource(imageIds[i]);
			imageList.add(imageView);

			ImageView point = new ImageView(getActivity());
			LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = 10;
			point.setLayoutParams(params);
			point.setBackgroundResource(R.drawable.point_select);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			ll_points_news.addView(point);
		}

	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// container.addView(imageList.get(position));
			// return imageList.get(position);

			// count>3时使用，不然会报错；count>3时，设置getCount---return Integer.Max

			container.addView(imageList.get(position % imageList.size()));
			return imageList.get(position % imageList.size());
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			if (view == object) {
				return true;
			} else {
				return false;
			}

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			object = null;

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(Utils.TAG, "InformationFragment, onResume()");

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(Utils.TAG, "InformationFragment, onStart()");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(Utils.TAG, "InformationFragment, onPause()");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(Utils.TAG, "InformationFragment, onStop()");
	}

	@Override
	public void onDestroyView() {
		adapter = null;
		super.onDestroyView();
		Log.i(Utils.TAG, "InformationFragment, onDestroyView()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(Utils.TAG, "InformationFragment, onDestroy()");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.i(Utils.TAG, "InformationFragment, onDetach()");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		String WebManuals = web_manuals.getText().toString();
		if ("Taiir睡眠监测系统使用说明".equals(WebManuals)) {
			SpannableString sstext = new SpannableString("Taiir睡眠监测系统使用说明");
			sstext.setSpan(new URLSpanNoUnderline("http://www.taiir.com/zh/Taiir_SleepCare_Manual.html"), 0,
					sstext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			web_manuals.setText(sstext);
			web_manuals.setMovementMethod(LinkMovementMethod.getInstance());
		} else if ("Taiir SleepCare Manual".equals(WebManuals)) {
			SpannableString sstext = new SpannableString("Taiir SleepCare Manual");
			sstext.setSpan(new URLSpanNoUnderline("http://www.taiir.com/en/Taiir_SleepCare_Manual.html"), 0,
					sstext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			web_manuals.setText(sstext);
			web_manuals.setMovementMethod(LinkMovementMethod.getInstance());
		}
		return false;
	}

	// 自定义urlspan 去掉下划线
	public class URLSpanNoUnderline extends URLSpan {
		public URLSpanNoUnderline(String url) {
			super(url);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
			ds.setColor(getResources().getColor(R.color.clj_ys));

		}
	}
}
