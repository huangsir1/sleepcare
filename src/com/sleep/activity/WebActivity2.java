package com.sleep.activity;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity2 extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		webView = (WebView) findViewById(R.id.webview);
		ExitActivity.getInstance().addActivity(this);
		WebSettings settings = webView.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		Intent intent = getIntent();
		String weburl = intent.getStringExtra("weburl");
		webView.loadUrl(weburl);
		webView.setWebViewClient(new MyWebViewClient());

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
