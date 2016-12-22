package com.sleep.dialog;

import com.taiir.sleepcare.home.R;
import com.sleep.dao.PublicDao;
import com.sleep.dao.impl.PublicDaoImpl;
import com.sleep.local.classs.Epworth;
import com.sleep.updatedata.activity.UpdateEssActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ViewEssDialog {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences msharedPreferences;
	private PublicDao sd;
	private Epworth ep;

	public ViewEssDialog(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		msharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.look_ess_dialog, null);
		sd = new PublicDaoImpl(mContext);
		ep = sd.findAllEpworthById(msharedPreferences.getInt("muserid", -1));
		final int essid = msharedPreferences.getInt("muserid", -1);
		builder.create();
		// 坐着阅读时
		TextView z_z_y_d = (TextView) view.findViewById(R.id.textView6_1_ess_sslb);
		// 看电视时
		TextView k_d_s = (TextView) view.findViewById(R.id.textView6_2_ess_sslb);
		// 坐着与人谈话时
		TextView y_r_j_t = (TextView) view.findViewById(R.id.textView6_3_ess_sslb);
		// 开车等红绿灯时
		TextView k_c_d_h_l_d = (TextView) view.findViewById(R.id.textView6_4_ess_sslb);
		// 下午静卧休息时
		TextView x_w_j_w_x_x = (TextView) view.findViewById(R.id.textView6_5_ess_sslb);
		// 饭后休息时(未饮酒时)
		TextView f_h_x_x_s = (TextView) view.findViewById(R.id.textView6_6_ess_sslb);
		// 长时间坐车时中间不休息(超过1h)
		TextView c_s_j_b_x_x = (TextView) view.findViewById(R.id.textView6_7_ess_sslb);
		// 在公共场所坐着不动时(如在剧场或开会)
		TextView g_c_h_b_d = (TextView) view.findViewById(R.id.textView6_8_ess_sslb);
		// 总分
		TextView z_f_sum = (TextView) view.findViewById(R.id.zf_sum_ess_sslb);

		TextView x_g_ess = (TextView) view.findViewById(R.id.modify_ess_update);
		x_g_ess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UpdateEssActivity.class);
				intent.putExtra("essid", essid);
				mContext.startActivity(intent);
				dialog.dismiss();
				// ((Activity) mContext).finish();
				((Activity) mContext).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		// 设定显示的View
		builder.setView(view);
		if (ep != null) {
			z_z_y_d.setText(String.valueOf(ep.getSatreading()));
			k_d_s.setText(String.valueOf(ep.getWatchtv()));
			y_r_j_t.setText(String.valueOf(ep.getSatconversation()));
			k_c_d_h_l_d.setText(String.valueOf(ep.getTrafficlights()));
			x_w_j_w_x_x.setText(String.valueOf(ep.getJingworest()));
			f_h_x_x_s.setText(String.valueOf(ep.getAfterdinnerrest()));
			c_s_j_b_x_x.setText(String.valueOf(ep.getLongnotrest()));
			g_c_h_b_d.setText(String.valueOf(ep.getSatnotmove()));
			z_f_sum.setText(String.valueOf(ep.getSumscore()));
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
		// lp.width = 300; // 宽度
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
