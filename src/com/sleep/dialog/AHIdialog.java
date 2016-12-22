package com.sleep.dialog;

import com.taiir.sleepcare.home.R;
import com.loopj.android.application.MyApplication;
import com.sleep.local.classs.DiabetesHy;
import com.sleep.local.classs.Epworth;
import com.sleep.local.classs.UserHistory;
import com.sleep.local.classs.UserManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AHIdialog {
	private Context mContext;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private UserManager um;
	private DiabetesHy dh;
	private UserHistory uh;
	private Epworth ep;

	public AHIdialog(Context context) {

		mContext = context;
		builder = new AlertDialog.Builder(mContext);
		mSharedPreferences = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void AhiData(UserManager um, DiabetesHy dh, UserHistory uh, Epworth ep) {
		this.um = um;
		this.dh = dh;
		this.uh = uh;
		this.ep = ep;
	}

	/**
	 * 自定义视图对话框
	 */
	public void viewDialog(String title) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.report_ahi_dialog_hxzt, null);

		LinearLayout layout = (LinearLayout) view.findViewById(R.id.remove_dialog_ahi);
		LinearLayout la1 = (LinearLayout) view.findViewById(R.id.layout_d1);
		LinearLayout la2 = (LinearLayout) view.findViewById(R.id.layout_d2);
		LinearLayout la3 = (LinearLayout) view.findViewById(R.id.layout_d3);
		LinearLayout la4 = (LinearLayout) view.findViewById(R.id.layout_d4);
		builder.create();
		// //病情程度
		TextView uOSAHS = (TextView) view.findViewById(R.id.osahs_tx);
		TextView ESSzf = (TextView) view.findViewById(R.id.s_s_z_f_ess);

		TextView s_m = (TextView) view.findViewById(R.id.s_m_dialog);
		TextView t_n_b = (TextView) view.findViewById(R.id.t_n_b_dialog);
		TextView g_x_y = (TextView) view.findViewById(R.id.g_x_y_dialog);
		TextView g_x_b = (TextView) view.findViewById(R.id.g_x_b_dialog);
		TextView x_l_s_j = (TextView) view.findViewById(R.id.x_l_s_j_dialog);
		TextView x_l_s_c = (TextView) view.findViewById(R.id.s_l_s_c_dialog);
		TextView b_q_z_s = (TextView) view.findViewById(R.id.b_q_z_s_dialog);
		TextView c_q_x_y = (TextView) view.findViewById(R.id.c_q_x_y_dialog);
		TextView n_x_g_j_b = (TextView) view.findViewById(R.id.n_x_g_j_b_dialog);
		TextView s_g_n_s_h = (TextView) view.findViewById(R.id.s_g_n_s_h_dialog);
		TextView c_q_y_j = (TextView) view.findViewById(R.id.c_q_d_l_y_j_dialog);
		TextView f_y_z_j_j = (TextView) view.findViewById(R.id.f_y_z_j_j_dialog);
		TextView osahs_j_z_s = (TextView) view.findViewById(R.id.osahs_j_z_s_dialog);
		TextView s_f_j_j = (TextView) view.findViewById(R.id.s_f_j_j_dialog);
		TextView x_y_c_c_d = (TextView) view.findViewById(R.id.x_y_c_z_d_dialog);

		TextView ess_c_d_b = (TextView) view.findViewById(R.id.s_s_z_f_s_s_l_b);
		TextView ess_z_b_x_s = (TextView) view.findViewById(R.id.ess_c_d_z_k);
		// Ess日间嗜睡
		TextView ess_r_j_s_s = (TextView) view.findViewById(R.id.r_j_s_s_b_z);
		// 设定显示的View
		builder.setView(view);
		if (dh != null) {
			if (dh.getLosesleep() != null) {
				s_m.setText(mContext.getString(R.string.insomnia));
			} else {
				s_m.setText(null);
				la1.removeView(s_m);
			}
			if (dh.getDiabetes() != null) {
				t_n_b.setText(mContext.getString(R.string.diabetes));
			} else {
				t_n_b.setText(null);
				la2.removeView(t_n_b);
			}
			if (dh.getHypertension() != null) {
				g_x_y.setText(mContext.getString(R.string.hypertension));
			} else {
				g_x_y.setText(null);
				la3.removeView(g_x_y);
			}
			if (dh.getCoronaryheartdisease() != null) {
				g_x_b.setText(mContext.getString(R.string.coronary_heart_disease));
			} else {
				g_x_b.setText(null);
				la4.removeView(g_x_b);
			}
			if (dh.getHeartfailure() != null) {
				x_l_s_j.setText(mContext.getString(R.string.heart_failure));
			} else {
				x_l_s_j.setText(null);
				la1.removeView(x_l_s_j);
			}
			if (dh.getArrhythmia() != null) {
				x_l_s_c.setText(mContext.getString(R.string.arrhythmia));
			} else {
				x_l_s_c.setText(null);
				la2.removeView(x_l_s_c);
			}
			if (dh.getCongestion() != null) {
				b_q_z_s.setText(mContext.getString(R.string.nasal_obstruction));
			} else {
				b_q_z_s.setText(null);
				la3.removeView(b_q_z_s);
			}
			if (dh.getLongsmoking() != null) {
				c_q_x_y.setText(mContext.getString(R.string.long_term_smoking));
			} else {
				c_q_x_y.setText(null);
				la4.removeView(c_q_x_y);
			}
			if (dh.getCerebrovasculardisease() != null) {
				n_x_g_j_b.setText(mContext.getString(R.string.cerebral_vascular_disease));
			} else {
				n_x_g_j_b.setText(null);
				la1.removeView(n_x_g_j_b);
			}
			if (dh.getRenalfailure() != null) {
				s_g_n_s_h.setText(mContext.getString(R.string.renal_function_damage));
			} else {
				s_g_n_s_h.setText(null);
				la2.removeView(s_g_n_s_h);
			}
			if (dh.getTakesedatives() != null) {
				f_y_z_j_j.setText(mContext.getString(R.string.take_a_sedative));
			} else {
				f_y_z_j_j.setText(mContext.getString(R.string.take_a_sedative));
				f_y_z_j_j.setText(null);
				la4.removeView(f_y_z_j_j);
			}
			if (dh.getLongdrinking() != null) {
				c_q_y_j.setText(mContext.getString(R.string.long_term_heavy_drinking));
			} else {
				c_q_y_j.setText(null);
				la3.removeView(c_q_y_j);
			}
			if (dh.getWhetherjjm() != null) {
				s_f_j_j.setText(mContext.getString(R.string.menopause));
			} else {
				s_f_j_j.setText(null);
				la2.removeView(s_f_j_j);
			}
			if (dh.getWhetherfmhy() != null) {
				osahs_j_z_s.setText(mContext.getString(R.string.a_family_history_of_osahs));
			} else {
				osahs_j_z_s.setText(null);
				la1.removeView(osahs_j_z_s);
			}
			if (dh.getWhetherxyccd() != null) {
				x_y_c_c_d.setText(mContext.getString(R.string.bulky_uvula));
			} else {
				x_y_c_c_d.setText(null);
				la3.removeView(x_y_c_c_d);
			}
			if (dh.getLosesleep() == null && dh.getHeartfailure() == null && dh.getCerebrovasculardisease() == null
					&& dh.getWhetherfmhy() == null) {
				layout.removeViewInLayout(la1);
			}
			if (dh.getDiabetes() == null && dh.getArrhythmia() == null && dh.getRenalfailure() == null
					&& dh.getWhetherjjm() == null) {
				layout.removeViewInLayout(la2);
			}
			if (dh.getHypertension() == null && dh.getCongestion() == null && dh.getLongdrinking() == null
					&& dh.getWhetherxyccd() == null) {
				layout.removeViewInLayout(la3);
			}
			if (dh.getCoronaryheartdisease() == null && dh.getLongsmoking() == null && dh.getTakesedatives() == null) {
				layout.removeViewInLayout(la4);
			}
		}

		// 病情程度
		String degree = null;
		if (uh != null) {
			if (uh.getHudegree() != null) {
				if (uh.getHudegree().contains("重度OSAHS") || uh.getHudegree().contains("Severe OSAHS")) {
					degree = MyApplication.getContext().getString(R.string.severe_osahs);
				} else if (uh.getHudegree().contains("中度OSAHS") || uh.getHudegree().contains("Moderate OSAHS")) {
					degree = MyApplication.getContext().getString(R.string.moderate_osahs);
				} else if (uh.getHudegree().contains("轻度OSAHS") || uh.getHudegree().contains("Mild OSAHS")) {
					degree = MyApplication.getContext().getString(R.string.light_osahs);
				} else {
					Log.d("TAG", "uh.getHudegree()");
				}
				if (degree != null) {
					if (uh.getHudegree().contains("且重度低氧血症") || (uh.getHudegree().contains("with Severe Hypoxemia"))) {
						degree = degree + MyApplication.getContext().getString(R.string.and_severe_hypoxia);
					} else if (uh.getHudegree().contains("且中度低氧血症")
							|| (uh.getHudegree().contains("with Moderate Hypoxemia"))) {
						degree = degree + MyApplication.getContext().getString(R.string.and_moderate_hypoxia);
					} else if (uh.getHudegree().contains("且轻度低氧血症")
							|| (uh.getHudegree().contains("with Mild Hypoxemia"))) {
						degree = degree + MyApplication.getContext().getString(R.string.and_mild_hypoxia);
					} else {
						Log.d("TAG", "uh.getHudegree()");
					}
				} else {
					if ("重度低氧血症".equals(uh.getHudegree()) || "Severe Hypoxemia".equalsIgnoreCase(uh.getHudegree())) {
						degree = MyApplication.getContext().getString(R.string.severe_hypoxia);
					} else if ("中度低氧血症".equals(uh.getHudegree())
							|| "Moderate Hypoxemia".equalsIgnoreCase(uh.getHudegree())) {
						degree = MyApplication.getContext().getString(R.string.moderate_hypoxia);
					} else if ("轻度低氧血症".equals(uh.getHudegree())
							|| "Mild Hypoxemia".equalsIgnoreCase(uh.getHudegree())) {
						degree = MyApplication.getContext().getString(R.string.mild_hypoxia);
					} else {
						Log.d("TAG", "uh.getHudegree()");
					}
				}
				uOSAHS.setText(degree);
			} else {
				uOSAHS.setText(mContext.getString(R.string.no_input));
			}
		}
		if (ep != null) {
			ess_c_d_b.setText(String.valueOf(ep.getSumscore()));
			int essrzss = ep.getSumscore();
			// if (essrzss>=9) {
			// ess_r_j_s_s.setText(mContext.getString(R.string.daytime_sleepiness));
			// }else {
			// ess_r_j_s_s.setText(mContext.getString(R.string.daytime_sleepiness_was_not_obvious));
			// }
			if (essrzss < 8) {
				ess_r_j_s_s.setText(mContext.getString(R.string.normal_daytime_sleepiness));
			} else if (essrzss >= 8 && essrzss <= 11) {
				ess_r_j_s_s.setText(mContext.getString(R.string.mild_daytime_sleepiness));
			} else if (essrzss >= 12 && essrzss <= 15) {
				ess_r_j_s_s.setText(mContext.getString(R.string.moderate_daytime_sleepiness));
			} else if (essrzss >= 16 && essrzss <= 24) {
				ess_r_j_s_s.setText(mContext.getString(R.string.severe_daytime_sleepiness));
			}
		}
		if (um != null) {
			ESSzf.setText(String.valueOf(um.getUserbmi()));
			int fat = um.getUserbmi();
			if (fat < 18.5) {
				ess_z_b_x_s.setText(mContext.getString(R.string.too_light));
			} else if (fat >= 18.5 && fat < 25) {
				ess_z_b_x_s.setText(mContext.getString(R.string.normal));
			} else if (fat >= 25 && fat < 28) {
				ess_z_b_x_s.setText(mContext.getString(R.string.overweight));
			} else if (fat >= 28 && fat < 32) {
				ess_z_b_x_s.setText(mContext.getString(R.string.obesity));
			} else if (fat >= 32) {
				ess_z_b_x_s.setText(mContext.getString(R.string.very_fat));
			}
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
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
}
