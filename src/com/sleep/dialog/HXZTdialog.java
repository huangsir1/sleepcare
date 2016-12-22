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

public class HXZTdialog {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private UserHistory uh;

	public HXZTdialog(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		mSharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void HxztData(UserHistory uh) {
		this.uh = uh;
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.rpeort_hxztcs_dialog, null);

		builder.create();
		// 最长呼吸暂停时
		TextView zchxztHour = (TextView) view.findViewById(R.id.zchxzt_hour);
		// 最长呼吸暂停分
		TextView zchxztMinute = (TextView) view.findViewById(R.id.zchxzt_minute);
		// 最长呼吸暂停秒
		TextView zchxztSec = (TextView) view.findViewById(R.id.zchxzt_sec);

		// 发生于时
		TextView hxfsHour = (TextView) view.findViewById(R.id.hxfs_hour);
		// 发生于分
		TextView hxfsMinute = (TextView) view.findViewById(R.id.hxfs_minute);
		// 发生于秒
		TextView hxfsSec = (TextView) view.findViewById(R.id.hxfs_sec);

		// 呼吸暂停总时间时
		TextView hxztzsjHour = (TextView) view.findViewById(R.id.hxztzsj_hour);
		// 呼吸暂停总时间分
		TextView hxztzsjMinute = (TextView) view.findViewById(R.id.hxztzsj_minute);
		// 呼吸暂停总时间秒
		TextView hxztzsjSec = (TextView) view.findViewById(R.id.hxztzsj_sec);

		// 设定显示的View
		builder.setView(view);
		if (uh != null) {
			// 最长呼吸暂停时
			if (uh.getHzchxzthour() < 10) {
				zchxztHour.setText("0" + String.valueOf(uh.getHzchxzthour()));
			} else {
				zchxztHour.setText(String.valueOf(uh.getHzchxzthour()));
			}
			// 最长呼吸暂停分
			if (uh.getHzchxztminute() < 10) {
				zchxztMinute.setText("0" + String.valueOf(uh.getHzchxztminute()));
			} else {
				zchxztMinute.setText(String.valueOf(uh.getHzchxztminute()));
			}
			// 最长呼吸暂停秒
			if (uh.getHzchxztsec() < 10) {
				zchxztSec.setText("0" + String.valueOf(uh.getHzchxztsec()));
			} else {
				zchxztSec.setText(String.valueOf(uh.getHzchxztsec()));
			}

			// 发生于时
			if (uh.getHhxhapperhour() < 10) {
				hxfsHour.setText("0" + String.valueOf(uh.getHhxhapperhour()));
			} else {
				hxfsHour.setText(String.valueOf(uh.getHhxhapperhour()));
			}
			// 发生于分
			if (uh.getHhxhapperminute() < 10) {
				hxfsMinute.setText("0" + String.valueOf(uh.getHhxhapperminute()));
			} else {
				hxfsMinute.setText(String.valueOf(uh.getHhxhapperminute()));
			}
			// 发生于秒
			if (uh.getHhxhappersec() < 10) {
				hxfsSec.setText("0" + String.valueOf(uh.getHhxhappersec()));
			} else {
				hxfsSec.setText(String.valueOf(uh.getHhxhappersec()));
			}

			// 呼吸暂停总时间时
			if (uh.getHhxztzsjhour() < 10) {
				hxztzsjHour.setText("0" + String.valueOf(uh.getHhxztzsjhour()));
			} else {
				hxztzsjHour.setText(String.valueOf(uh.getHhxztzsjhour()));
			}
			// 呼吸暂停总时间分
			if (uh.getHhxztzsjminute() < 10) {
				hxztzsjMinute.setText("0" + String.valueOf(uh.getHhxztzsjminute()));
			} else {
				hxztzsjMinute.setText(String.valueOf(uh.getHhxztzsjminute()));
			}
			// 呼吸暂停总时间秒
			if (uh.getHhxztzsjsec() < 10) {
				hxztzsjSec.setText("0" + String.valueOf(uh.getHhxztzsjsec()));
			} else {
				hxztzsjSec.setText(String.valueOf(uh.getHhxztzsjsec()));
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
		lp.y = 0; // 新位置Y坐标
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