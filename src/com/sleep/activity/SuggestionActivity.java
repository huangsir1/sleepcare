package com.sleep.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.taiir.sleepcare.home.R;
import com.sleep.utils.DiaUtilsBtnSleep;
import com.sleep.utils.ExitActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import taiyi.web.jason.WebAPI;
import taiyi.web.model.dto.Status;

public class SuggestionActivity extends Activity {

	protected static final int SUCCESS = 0;
	protected static final int FAILED = 1;

	private EditText et_suggestion;
	private String suggestion;
	private String url = WebAPI.DEFAULT_URL + "/api/feedback/upload";
	// private String url = "http://192.168.11.109/web/api/feedback/upload";
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				Toast.makeText(SuggestionActivity.this,
						SuggestionActivity.this
								.getString(R.string.your_opinion_is_very_important_for_us_to_submit_to_success),
						1).show();
				finish();
				overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);	
				break;
			case FAILED:
				Toast.makeText(SuggestionActivity.this,
						SuggestionActivity.this.getString(R.string.submission_failed_please_try_again), 0).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		ExitActivity.getInstance().addActivity(this);
		et_suggestion = (EditText) findViewById(R.id.et_suggestion);
	}

	public void commit(View view) {
		suggestion = et_suggestion.getText().toString().trim();
		if (DiaUtilsBtnSleep.isFastClick()) {
			return;
		}
		if (TextUtils.isEmpty(suggestion)) {
			Toast.makeText(this, SuggestionActivity.this.getString(R.string.please_enter_your_valuable_comments), 1)
					.show();
			return;
		}
		saveSuggestion();
	}

	public void back(View view) {
		finish();
		overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
	}

	private void saveSuggestion() {

		new Thread(new Runnable() {
			Message msg = Message.obtain();

			@Override
			public void run() {
				try {
					JSONObject suggestObj = new JSONObject();
					suggestObj.put("content", suggestion);
					suggestObj.put("contact", "No...");
					suggestObj.put("token", "bbad89d2-97bf-4632-8de4-dc36558ddf1b");
					String suggestString = suggestObj.toString();
					Status statuss = new WebAPI().sendRequest(url, suggestString);
					System.out.println(statuss);
					msg.what = SUCCESS;

				} catch (IOException e) {

					msg.what = FAILED;
					System.out.println("IO异常");
					e.printStackTrace();

				} catch (JSONException e) {

					msg.what = FAILED;
					System.out.println("Json异常");
					e.printStackTrace();

				} finally {
					handler.sendMessage(msg);
				}

			}
		}).start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			SuggestionActivity.this.finish();
			overridePendingTransition(R.anim.right_left_out, R.anim.right_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
