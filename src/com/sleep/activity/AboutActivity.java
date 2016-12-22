package com.sleep.activity;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity {
	private ImageView backIV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);
		ExitActivity.getInstance().addActivity(this);
		TextView tv = (TextView) findViewById(R.id.introduce);
		// tv.setText(Html.fromHtml("<a href=\"http://www.taiir.com\">Taiir
		// SleepCare</a> "
		// + "is an innovative sleep and respiratory care solution designed to
		// help you evaluate the quality of sleep and empower you to live a
		// healthy life with the ability to sleep and breathe naturally. By
		// monitoring blood oxygen saturation and pulse rate during your sleep,
		// Taiir SleepCare can make a comprehensive analysis of medical indexes
		// related to sleep apnea and hypopnea. Taiir SleepCare is not only an
		// exceptional tool for SAHS screening but also a partner to help you
		// enjoying a better night’s sleep by providing healthcare
		// guidelines."));
		// tv.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		// tv.getPaint().setAntiAlias(true);//抗锯齿
		// tv.getPaint().setFlags(0); // 取消设置的的划线
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		TextView sv_tv = (TextView) findViewById(R.id.web_software_version);
		String sv = sv_tv.getText().toString();
		if (sv.contains("使用条款")) {
			sv_tv.setMovementMethod(LinkMovementMethod.getInstance());
		} else {
			sv_tv.setMovementMethod(LinkMovementMethod.getInstance());
		}

		backIV = (ImageView) findViewById(R.id.imageView1_about);
		// 返回
		backIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutActivity.this.finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			AboutActivity.this.finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
