package com.sleep.activity;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends Activity {
	private WebView webView;
	private TextView titletv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		webView = (WebView) findViewById(R.id.webview);
		ExitActivity.getInstance().addActivity(this);
		titletv =(TextView) findViewById(R.id.tv_title);
		WebSettings settings = webView.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		Intent intent = getIntent();
		String fileName = intent.getStringExtra("fileName");
		String 	tn = intent.getStringExtra("tn");
		titletv.setText(tn);
		String url = "file:///android_asset/" + fileName + ".html";
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());

	}

	public void back(View view) {
		finish();
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		// 在WebView中而不是默认浏览器中显示页面
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
