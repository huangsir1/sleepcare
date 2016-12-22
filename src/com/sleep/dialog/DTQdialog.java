package com.sleep.dialog;

import com.taiir.sleepcare.home.R;
import com.sleep.local.classs.UserHistory;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class DTQdialog {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private UserHistory uh;

	public DTQdialog(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		mSharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void DtqData(UserHistory uh) {
		this.uh = uh;
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.report_dtq_dialog, null);

		builder.create();
		// 最长低通气时
		TextView zcdtqHour = (TextView) view.findViewById(R.id.zcdtq_hour);
		// 最长低通气分
		TextView zcdtqMinute = (TextView) view.findViewById(R.id.zcdtq_minute);
		// 最长低通气秒
		TextView zcdtqSec = (TextView) view.findViewById(R.id.zcdtq_sec);
		// 发生于时
		TextView dtqHapperHour = (TextView) view.findViewById(R.id.dtqfs_hour);
		// 发生于分
		TextView dtqHapperMinute = (TextView) view.findViewById(R.id.dtqfs_minute);
		// 发生于秒
		TextView dtqHapperSec = (TextView) view.findViewById(R.id.dtqfs_sec);
		// 低通气总时间时
		TextView dtqzsjhHour = (TextView) view.findViewById(R.id.dtqzsj_hour);
		// 低通气总时间分
		TextView dtqzsjMinute = (TextView) view.findViewById(R.id.dtqzsj_minute);
		// 低通气总时间秒
		TextView dtqzsjSec = (TextView) view.findViewById(R.id.dtqzsj_sec);
		// 设定显示的View
		builder.setView(view);
		if (uh != null) {
			// 最长低通气时
			if (uh.getHzcdtqhour() < 10) {
				zcdtqHour.setText("0" + String.valueOf(uh.getHzcdtqhour()));
			} else {
				zcdtqHour.setText(String.valueOf(uh.getHzcdtqhour()));
			}
			// 最长低通气分
			if (uh.getHzcdtqminute() < 10) {
				zcdtqMinute.setText("0" + String.valueOf(uh.getHzcdtqminute()));
			} else {
				zcdtqMinute.setText(String.valueOf(uh.getHzcdtqminute()));
			}
			// 最长低通气秒
			if (uh.getHzcdtqsec() < 10) {
				zcdtqSec.setText("0" + String.valueOf(uh.getHzcdtqsec()));
			} else {
				zcdtqSec.setText(String.valueOf(uh.getHzcdtqsec()));
			}
			// 发生于时
			if (uh.getHdtqhapperhour() < 10) {
				dtqHapperHour.setText("0" + String.valueOf(uh.getHdtqhapperhour()));
			} else {
				dtqHapperHour.setText(String.valueOf(uh.getHdtqhapperhour()));
			}
			// 发生于分
			if (uh.getHdtqhapperminute() < 10) {
				dtqHapperMinute.setText("0" + String.valueOf(uh.getHdtqhapperminute()));
			} else {
				dtqHapperMinute.setText(String.valueOf(uh.getHdtqhapperminute()));
			}
			// 发生于秒
			if (uh.getHdtqhappersec() < 10) {
				dtqHapperSec.setText("0" + String.valueOf(uh.getHdtqhappersec()));
			} else {
				dtqHapperSec.setText(String.valueOf(uh.getHdtqhappersec()));
			}
			// 低通气总时间时
			if (uh.getHdtqzsjhour() < 10) {
				dtqzsjhHour.setText("0" + String.valueOf(uh.getHdtqzsjhour()));
			} else {
				dtqzsjhHour.setText(String.valueOf(uh.getHdtqzsjhour()));
			}
			// 低通气总时间分
			if (uh.getHdtqzsjminute() < 10) {
				dtqzsjMinute.setText("0" + String.valueOf(uh.getHdtqzsjminute()));
			} else {
				dtqzsjMinute.setText(String.valueOf(uh.getHdtqzsjminute()));
			}
			// 低通气总时间秒
			if (uh.getHdtqzsjsec() < 10) {
				dtqzsjSec.setText("0" + String.valueOf(uh.getHdtqzsjsec()));
			} else {
				dtqzsjSec.setText(String.valueOf(uh.getHdtqzsjsec()));
			}
		}

		// 设置dialog是否为模态，false表示模态，true表示非模态
		// ab.setCancelable(false);
		// 对话框的创建、显示,这里显示的位置是在屏幕的最下面，但是很不推荐这个种做法，因为距底部有一段空隙
		dialog = builder.create();
		Window window = dialog.getWindow();

		WindowManager.LayoutParams lp = window.getAttributes();

		window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		//
		lp.x = 0; // 新位置X坐标
		lp.y = 0;
		; // 新位置Y坐标
			// lp.width =300; // 宽度
			// lp.height = 300; // 高度
			// lp.alpha = 1.0f; // 透明度
		window.setAttributes(lp);

		view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.myAnimationstyles); // 添加动画
		// 设置dialog的边框，这样可以去掉黑色的多余部分
		dialog.setView(view, 0, 0, 0, 0);
		// 对话框的外面点击,是否让对话框消失(fasle 不消失)
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
