package com.sleep.dialog;




import com.taiir.sleepcare.home.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SubmitDialog extends Dialog{
	private Context mContext;
	private ImageView icon;
	private String contentString = "loading...";
	private TextView content;

	private Animation animation = null;

	public SubmitDialog(Context context) {
		super(context, R.style.submit_dialog);
		this.mContext = context;
	}
	
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.dialog_submit);
		initWidget();
		init();
	}

	private void initWidget() {
		this.icon = ((ImageView) findViewById(R.id.dialog_submit_icon));
		this.content = ((TextView) findViewById(R.id.dialog_submit_content));
	}

	private void init() {
		animation = AnimationUtils.loadAnimation(mContext,
				R.anim.loading_animation);
	}

	private void setAnimation() {
		this.icon.startAnimation(this.animation);
	}

	public void setContent(String paramString) {
		this.contentString = paramString;
		if (this.content != null)
			this.content.setText(this.contentString);
	}

	public void show() {
		super.show();
		setAnimation();
		this.content.setText(this.contentString);
	}

}
