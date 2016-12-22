package com.sleep.runningman;

import com.taiir.sleepcare.home.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog_new extends Dialog {
	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void callback();
	}

	// private String textTitle;
	private TextView message;
	private String textMessage;
	private Button positiveButton;
	private String textPositive;
	private OnCustomDialogListener positiveButtonListener;
	private OnCustomDialogListener negativeButtonListener;
	private Button negativeButton;
	private String textNegative;
	private boolean positiveBtn = false;
	private boolean negativeBtn = false;

	/**
	 * 自定义dialog，可修改显示的内容，可隐藏按钮，可修改按钮中的文字，可自定义按钮触发的事件
	 * 
	 * @param context
	 *            上下文
	 * @param textMessage
	 *            dialog提示的内容
	 * @param positiveButton
	 *            true：显示按钮，false：隐藏按钮
	 * @param textPositive
	 *            positiveButton中的文字
	 * @param positiveButtonListener
	 *            positiveButton的点击事件监听器
	 * @param negativeButton
	 *            true：显示按钮，false：隐藏按钮
	 * @param textNegative
	 *            negativeButton中的文字
	 * @param negativeButtonListener
	 *            negativeButton的点击事件监听器
	 */
	public CustomDialog_new(Context context, String textMessage, boolean positiveBtn, String textPositive,
			OnCustomDialogListener positiveButtonListener, boolean negativeBtn, String textNegative, OnCustomDialogListener negativeButtonListener) {
		super(context);
		this.textMessage = textMessage;
		this.positiveBtn = positiveBtn;
		this.textPositive = textPositive;
		this.positiveButtonListener = positiveButtonListener;
		this.negativeBtn = negativeBtn;
		this.textNegative = textNegative;
		this.negativeButtonListener = negativeButtonListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置去除屏幕顶端的标题
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// 设置动画效果
		getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		// 设置背景图
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// 设置点击dialog外部时，是否可以取消dialog
		setCanceledOnTouchOutside(false);
		setCancelable(false);

		if(positiveBtn && negativeBtn){
			setContentView(R.layout.dialog_custom_new_double_button);
			positiveButton = (Button) findViewById(R.id.positive_button1);
			// 设置按钮的文字内容
			if (textPositive != null) {
				positiveButton.setText(textPositive);
			}
			// 设置按钮的点击事件监听器
			positiveButton.setOnClickListener(positiveClickListener);
			
			negativeButton = (Button) findViewById(R.id.close_button1);
			// 设置按钮的文字内容
			if (textNegative != null) {
				negativeButton.setText(textNegative);
			}
			negativeButton.setOnClickListener(negativeClickListener);
		}else if (positiveBtn) {
			setContentView(R.layout.dialog_custom_new_single_button);
			positiveButton = (Button) findViewById(R.id.positive_button1);
			// 设置按钮的文字内容
			if (textPositive != null) {
				positiveButton.setText(textPositive);
			}
			// 设置按钮的点击事件监听器
			positiveButton.setOnClickListener(positiveClickListener);
		} else if(negativeBtn){
			
		} else {
			setContentView(R.layout.dialog_custom_new_no_button);
		}
		// 设置dialog的文字内容
		message = (TextView) findViewById(R.id.message1);
		message.setText(textMessage);
	}

	private View.OnClickListener positiveClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if(positiveButtonListener != null){
				positiveButtonListener.callback();
			}
			CustomDialog_new.this.dismiss();
		}
	};
	
	private View.OnClickListener negativeClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (negativeButtonListener != null) {
				negativeButtonListener.callback();
			}
			CustomDialog_new.this.dismiss();
		}
	};

}