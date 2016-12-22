package com.sleep.welcome;

import java.util.ArrayList;
import java.util.List;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.ExitActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity {

	private SharedPreferences mSharedPreferences;
	private Button enterBtn;
	@SuppressWarnings("unused")
	private SharedPreferences.Editor mEditor;
	private ViewPager mViewPager;
	private int[] imageResources = { R.drawable.one, R.drawable.two, R.drawable.three };// R.drawable.bm4000,
	private LinearLayout dotlayout;
	private List<ImageView> all;
	private int startactivity;
	public static WelcomeActivity instance = null;

	@SuppressLint("CommitPrefEdits")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_layout);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ExitActivity.getInstance().addActivity(this);

		// 获取ViewPager对象
		mSharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		startactivity = mSharedPreferences.getInt("startactivity", 0);
		mViewPager = (ViewPager) findViewById(R.id.vp);
		enterBtn = (Button) findViewById(R.id.enter_btn);
		enterBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String menter = enterBtn.getText().toString();
				
				Intent intent = new Intent(WelcomeActivity.this, StartPictrueActivity.class);
				intent.putExtra("enterBtn", menter);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		instance = this;
		if (startactivity == 1) {
			Intent intent = new Intent(WelcomeActivity.this, StartPictrueActivity.class);
			startActivity(intent);
			finish();
		} else {
			all = getImageList();
			mViewPager.setAdapter(new ImageAdapter(this, all));
			initDot();
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					ImageView imageView1 = (ImageView) dotlayout.getChildAt(0);
					ImageView imageView2 = (ImageView) dotlayout.getChildAt(1);
					ImageView imageView3 = (ImageView) dotlayout.getChildAt(2);
					switch (arg0) {
					case 0:
						// 当滑动到第一个页面的时候
						imageView1.setImageResource(R.drawable.guide002);
						imageView2.setImageResource(R.drawable.guide0004);
						imageView3.setImageResource(R.drawable.guide0004);
						enterBtn.setVisibility(View.INVISIBLE);
						break;
					case 1:
						// 当滑动到第二个页面的时候
						imageView1.setImageResource(R.drawable.guide0004);
						imageView2.setImageResource(R.drawable.guide002);
						imageView3.setImageResource(R.drawable.guide0004);
						enterBtn.setVisibility(View.INVISIBLE);
						break;
					case 2:
						// 当滑动到第三个页面的时候
						imageView1.setImageResource(R.drawable.guide0004);
						imageView2.setImageResource(R.drawable.guide0004);
						imageView3.setImageResource(R.drawable.guide002);
						enterBtn.setVisibility(View.VISIBLE);
						break;

					default:
						break;
					}

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}
	}

	private List<ImageView> getImageList() {
		List<ImageView> all = new ArrayList<ImageView>();
		if (imageResources.length > 0) {
			for (int i = 0; i < imageResources.length; i++) {
				// 创建ImageView对象
				ImageView imageView = new ImageView(this);
				// 设置布局
				imageView.setLayoutParams(new LayoutParams(200, 300));
				// 设置资源
				imageView.setImageResource(imageResources[i]);
				// 将imageView添加到集合中
				all.add(imageView);
			}
		}
		return all;
	}

	/**
	 * 初始化点
	 */
	public void initDot() {
		dotlayout = (LinearLayout) findViewById(R.id.dotlayout);
		if (all != null && all.size() > 0) {
			for (int i = 0; i < all.size(); i++) {
				// 创建ImageView
				ImageView dotImageView = new ImageView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				// 设置引导页底部，几个点之间的距离
				lp.setMargins(10, 0, 10, 0);

				dotImageView.setLayoutParams(lp);

				// // 设置资源
				dotImageView.setImageResource(R.drawable.guide0004);
				dotlayout.addView(dotImageView);
			}
		}
		// 初始的时候让第一个点变成红色点
		ImageView imageView1 = (ImageView) dotlayout.getChildAt(0);
		imageView1.setImageResource(R.drawable.guide002);

	}

}
