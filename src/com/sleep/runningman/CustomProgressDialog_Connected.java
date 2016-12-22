package com.sleep.runningman;

import com.taiir.sleepcare.home.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description:自定义对话框
 * @author http://blog.csdn.net/finddreams
 */
public class CustomProgressDialog_Connected extends ProgressDialog {

	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView mImageView;

	private TextView mLoadingTv;
	private String mLoadingTip;
	private int count = 0;
	private String oldLoadingTip;
	private int mResid;

	public CustomProgressDialog_Connected(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}


	private void initView() {
		setContentView(R.layout.progress_dialog1);
		mLoadingTv = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}

	/*
	 * @Override public void onWindowFocusChanged(boolean hasFocus) { // TODO
	 * Auto-generated method stub mAnimation.start();
	 * super.onWindowFocusChanged(hasFocus); }
	 */
}
