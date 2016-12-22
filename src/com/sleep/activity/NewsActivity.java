package com.sleep.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taiir.sleepcare.home.R;
import com.loopj.android.image.SmartImageView;
import com.sleep.domain.NewInfo;
import com.sleep.utils.ExitActivity;
import com.sleep.utils.StreamTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;

public class NewsActivity extends Activity {
	protected static final int SUCCESS = 1;
	protected static final int FAILED = 2;

	private ViewPager vp_news;
	private LinearLayout ll_points_news;

	private List<NewInfo> newInfoList;
	private ArrayList<SmartImageView> imageList;

	private int lastPosition;

	private boolean isRunning = false;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				vp_news.setCurrentItem(vp_news.getCurrentItem() + 1);
				if (isRunning) {
					handler.sendEmptyMessageDelayed(0, 3000);
				}
				break;
			case SUCCESS:
				System.out.println("成功");
				newInfoList = (List<NewInfo>) msg.obj;
				loadImagesPoints();

				vp_news.setAdapter(new MyPagerAdapter());
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
				isRunning = true;
				handler.sendEmptyMessageDelayed(0, 3000);

				break;
			case FAILED:
				Toast.makeText(NewsActivity.this,
						NewsActivity.this.getString(R.string.please_check_your_network_and_try_again),
						Toast.LENGTH_SHORT).show();
				System.out.println("失败");
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		ExitActivity.getInstance().addActivity(this);
		init();

	}

	/**
	 * 1：初始化viewpager和point的线性布局 2：联网获取数据 3：发送handler消息
	 */
	private void init() {
		vp_news = (ViewPager) findViewById(R.id.vp_news);
		ll_points_news = (LinearLayout) findViewById(R.id.ll_points_news);

		new Thread(new Runnable() {

			@Override
			public void run() {
				List<NewInfo> newInfoList = getNewsFromInternet();
				Message msg = new Message();
				if (newInfoList != null) {
					msg.what = SUCCESS;
					msg.obj = newInfoList;
				} else {
					msg.what = FAILED;
				}
				handler.sendMessage(msg);
			}
		}).start();
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

		imageList = new ArrayList<SmartImageView>();
		for (int i = 0; i < newInfoList.size(); i++) {
			NewInfo newInfo = new NewInfo();
			newInfo = newInfoList.get(i);
			String imageUrl = newInfo.getImageUrl();
			final String content = newInfo.getContent();

			SmartImageView smartImageView = new SmartImageView(getApplicationContext());
			smartImageView.setImageUrl(imageUrl);
			smartImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewsActivity.this, WebActivity.class);
					System.out.println(content);
					intent.putExtra("content", content);
					startActivity(intent);
				}
			});
			imageList.add(smartImageView);

			ImageView point = new ImageView(this);
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

			return newInfoList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(imageList.get(position));
			return imageList.get(position);

			// count>3时使用，不然会报错
			// container.addView(imageList.get(position % imageList.size()));
			// return imageList.get(position % imageList.size());
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

}
