package com.sleep.dialog;

import com.sleep.local.classs.UserHistory;
import com.taiir.sleepcare.home.R;

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

public class ODIdialog {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private UserHistory uh;

	public ODIdialog(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		mSharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void OdiData(UserHistory uh) {
		this.uh = uh;
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.report_odi_dialog, null);

		builder.create();
		// ODI
		TextView odi = (TextView) view.findViewById(R.id.xybhd_odi);
		// 醒时
		TextView smsOber = (TextView) view.findViewById(R.id.xs_time);
		// 最低
		TextView smMin = (TextView) view.findViewById(R.id.zd_time);
		// 最高
		TextView smMax = (TextView) view.findViewById(R.id.zg_time);
		// 平均
		TextView smAverage = (TextView) view.findViewById(R.id.pj_time);
		/**
		 * a-代表(90%-100%)
		 */
		// a总时间时
		TextView axyfbzsjHour = (TextView) view.findViewById(R.id.zsj_xs_houra);
		// a总时间分
		TextView axyfbzsjMinute = (TextView) view.findViewById(R.id.zsj_xs_minutea);
		// a总时间秒
		TextView axyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_seca);
		// a-氧减总次数
		TextView ayjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_houra);
		/**
		 * b-代表(80%-89%)
		 */
		// b总时间时
		TextView bxyfbzsjHour = (TextView) view.findViewById(R.id.zsj_xs_hourb);
		// b总时间分
		TextView bxyfbzsjMinute = (TextView) view.findViewById(R.id.zsj_xs_minuteb);
		// b总时间秒
		TextView bxyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_secb);
		// b-氧减总次数
		TextView byjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_hourb);
		/**
		 * c-代表(70%-79%)
		 */
		// c总时间时
		TextView cxyfbzsjHour = (TextView) view.findViewById(R.id.zsj_xs_hourc);
		// c总时间分
		TextView cxyfbzsjMinute = (TextView) view.findViewById(R.id.zsj_xs_minutec);
		// c总时间秒
		TextView cxyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_secc);
		// c-氧减总次数
		TextView cyjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_hourc);
		/**
		 * d-代表(60%-69%)
		 */
		// d总时间时
		TextView dxyfbzsjhour = (TextView) view.findViewById(R.id.zsj_xs_hourd);
		// d总时间分
		TextView dxyfbzsjHinute = (TextView) view.findViewById(R.id.zsj_xs_minuted);
		// d总时间秒
		TextView dxyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_secd);
		// d-氧减总次数
		TextView dyjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_hourd);
		/**
		 * e-代表(50%-59%)
		 */
		// e总时间时
		TextView exyfbzsjHour = (TextView) view.findViewById(R.id.zsj_xs_houre);
		// e总时间分
		TextView exyfbzsjMinute = (TextView) view.findViewById(R.id.zsj_xs_minutee);
		// e总时间秒
		TextView exyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_sece);
		// e-氧减总次数
		TextView eyjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_houre);
		/**
		 * f-代表(<50%)
		 */
		// f总时间时
		TextView fxyfbzsjHour = (TextView) view.findViewById(R.id.zsj_xs_hourf);
		// f总时间分
		TextView fxyfbzsjMinute = (TextView) view.findViewById(R.id.zsj_xs_minutef);
		// f总时间秒
		TextView fxyfbzsjSec = (TextView) view.findViewById(R.id.zsj_xs_secf);
		// f-氧减总次数
		TextView fyjzcsNum = (TextView) view.findViewById(R.id.yjzcs_xs_hourf);

		// 设定显示的View
		builder.setView(view);
		if (uh != null) {
			// ODI
			odi.setText(uh.getHxyodi());

			// 醒时
			smsOber.setText("--");
			// 最低
			smMin.setText(uh.getHzdxybhdIndex());
			// 最高
			smMax.setText(uh.getHsmmax());
			// 平均
			smAverage.setText(uh.getHpjxybhdIndex());
			/**
			 * a-代表(90%-100%)
			 */
			// a总时间时
			if (uh.getHaxyfbzsjhour() < 10) {
				axyfbzsjHour.setText("0" + String.valueOf(uh.getHaxyfbzsjhour()));
			} else {
				axyfbzsjHour.setText(String.valueOf(uh.getHaxyfbzsjhour()));
			}
			// a总时间分
			if (uh.getHaxyfbzsjminute() < 10) {
				axyfbzsjMinute.setText("0" + String.valueOf(uh.getHaxyfbzsjminute()));
			} else {
				axyfbzsjMinute.setText(String.valueOf(uh.getHaxyfbzsjminute()));
			}
			// a总时间秒
			if (uh.getHaxyfbzsjsec() < 10) {
				axyfbzsjSec.setText("0" + String.valueOf(uh.getHaxyfbzsjsec()));
			} else {
				axyfbzsjSec.setText(String.valueOf(uh.getHaxyfbzsjsec()));
			}
			// a-氧减总次数
			ayjzcsNum.setText(uh.getHayjzcsnum());
			/**
			 * b-代表(80%-89%)
			 */
			// b总时间时
			if (uh.getHbxyfbzsjhour() < 10) {
				bxyfbzsjHour.setText("0" + String.valueOf(uh.getHbxyfbzsjhour()));
			} else {
				bxyfbzsjHour.setText(String.valueOf(uh.getHbxyfbzsjhour()));
			}
			// b总时间分
			if (uh.getHbxyfbzsjminute() < 10) {
				bxyfbzsjMinute.setText("0" + String.valueOf(uh.getHbxyfbzsjminute()));
			} else {
				bxyfbzsjMinute.setText(String.valueOf(uh.getHbxyfbzsjminute()));
			}
			// b总时间秒
			if (uh.getHbxyfbzsjsec() < 10) {
				bxyfbzsjSec.setText("0" + String.valueOf(uh.getHbxyfbzsjsec()));
			} else {
				bxyfbzsjSec.setText(String.valueOf(uh.getHbxyfbzsjsec()));
			}
			// b-氧减总次数
			byjzcsNum.setText(uh.getHbyjzcsnum());
			/**
			 * c-代表(70%-79%)
			 */
			// c总时间时
			if (uh.getHcxyfbzsjhour() < 10) {
				cxyfbzsjHour.setText("0" + String.valueOf(uh.getHcxyfbzsjhour()));
			} else {
				cxyfbzsjHour.setText(String.valueOf(uh.getHcxyfbzsjhour()));
			}
			// c总时间分
			if (uh.getHcxyfbzsjminute() < 10) {
				cxyfbzsjMinute.setText("0" + String.valueOf(uh.getHcxyfbzsjminute()));
			} else {
				cxyfbzsjMinute.setText(String.valueOf(uh.getHcxyfbzsjminute()));
			}
			// c总时间秒
			if (uh.getHcxyfbzsjsec() < 10) {
				cxyfbzsjSec.setText("0" + String.valueOf(uh.getHcxyfbzsjsec()));
			} else {
				cxyfbzsjSec.setText(String.valueOf(uh.getHcxyfbzsjsec()));
			}
			// c-氧减总次数
			cyjzcsNum.setText(uh.getHcyjzcsnum());
			/**
			 * d-代表(60%-69%)
			 */
			// d总时间时
			if (uh.getHdxyfbzsjhour() < 10) {
				dxyfbzsjhour.setText("0" + String.valueOf(uh.getHdxyfbzsjhour()));
			} else {
				dxyfbzsjhour.setText(String.valueOf(uh.getHdxyfbzsjhour()));
			}
			// d总时间分
			if (uh.getHdxyfbzsjminute() < 10) {
				dxyfbzsjHinute.setText("0" + String.valueOf(uh.getHdxyfbzsjminute()));
			} else {
				dxyfbzsjHinute.setText(String.valueOf(uh.getHdxyfbzsjminute()));
			}
			// d总时间秒
			if (uh.getHdxyfbzsjsec() < 10) {
				dxyfbzsjSec.setText("0" + String.valueOf(uh.getHdxyfbzsjsec()));
			} else {
				dxyfbzsjSec.setText(String.valueOf(uh.getHdxyfbzsjsec()));
			}
			// d-氧减总次数
			dyjzcsNum.setText(uh.getHdyjzcsnum());
			/**
			 * e-代表(50%-59%)
			 */
			// e总时间时
			if (uh.getHexyfbzsjhour() < 10) {
				exyfbzsjHour.setText("0" + String.valueOf(uh.getHexyfbzsjhour()));
			} else {
				exyfbzsjHour.setText(String.valueOf(uh.getHexyfbzsjhour()));
			}
			// e总时间分
			if (uh.getHexyfbzsjminute() < 10) {
				exyfbzsjMinute.setText("0" + String.valueOf(uh.getHexyfbzsjminute()));
			} else {
				exyfbzsjMinute.setText(String.valueOf(uh.getHexyfbzsjminute()));
			}
			// e总时间秒
			if (uh.getHexyfbzsjsec() < 10) {
				exyfbzsjSec.setText("0" + String.valueOf(uh.getHexyfbzsjsec()));
			} else {
				exyfbzsjSec.setText(String.valueOf(uh.getHexyfbzsjsec()));
			}
			// e-氧减总次数
			eyjzcsNum.setText(uh.getHeyjzcsnum());
			/**
			 * f-代表(<50%)
			 */
			// f总时间时
			if (uh.getHfxyfbzsjhour() < 10) {
				fxyfbzsjHour.setText("0" + String.valueOf(uh.getHfxyfbzsjhour()));
			} else {
				fxyfbzsjHour.setText(String.valueOf(uh.getHfxyfbzsjhour()));
			}
			// f总时间分
			if (uh.getHfxyfbzsjminute() < 10) {
				fxyfbzsjMinute.setText("0" + String.valueOf(uh.getHfxyfbzsjminute()));
			} else {
				fxyfbzsjMinute.setText(String.valueOf(uh.getHfxyfbzsjminute()));
			}
			// f总时间秒
			if (uh.getHfxyfbzsjsec() < 10) {
				fxyfbzsjSec.setText("0" + String.valueOf(uh.getHfxyfbzsjsec()));
			} else {
				fxyfbzsjSec.setText(String.valueOf(uh.getHfxyfbzsjsec()));
			}
			// f-氧减总次数
			fyjzcsNum.setText(uh.getHfyjzcsnum());
		}

		// 设置dialog是否为模态，false表示模态，true表示非模态
		// ab.setCancelable(false);
		// 对话框的创建、显示,这里显示的位置是在屏幕的最下面，但是很不推荐这个种做法，因为距底部有一段空隙
		dialog = builder.create();
		Window window = dialog.getWindow();

		WindowManager.LayoutParams lp = window.getAttributes();

		window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

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
		// dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
